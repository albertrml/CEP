# Roadmap: Database Refactoring & Cache Lifecycle (v3.0)

This document outlines the architectural shift from a **ZIP-Centric** to an **Address-Centric** 
data model. The goal is to preserve user data (Notes/Logs) even if official municipal ZIP code 
indexing changes, and to implement a self-cleaning cache mechanism.

## 📋 Architectural Overview
- **Primary Change**: Migration from Natural Key (`zipcode`) to Surrogate Key (`id`) in `PlaceEntity`.
- **Identity Logic**: Physical addresses (Street, City, UF) define the record's identity, while 
the ZIP code becomes a mutable attribute.
- **Cache Management**: Implementation of TTL (Time-To-Live) for non-favorite records.

---

## 🛠 Feature Breakdown & Tasks

### 1. Database Schema Evolution (Core Refactor)
*Consolidates PK migration and metadata addition.*

**User Story:**
As a developer, I want to use a surrogate ID for Places so that internal relationships remain stable 
even if external ZIP codes change.

**Acceptance Criteria (Gherkin):**
- **Scenario**: Successful foreign key migration
  - **Given** an existing Place with ZIP `11111-111` and linked Notes
  - **When** the database migrates to version 3
  - **Then** a new unique `id` must be generated for the Place
  - **And** all linked Notes must point to this new `id` instead of the ZIP string.

**Tasks:**
- [ ] Update `PlaceEntity`:
    - Set `@PrimaryKey(autoGenerate = true) val id: Long`.
    - Set `zipcode` as a standard column with a `UNIQUE` index.
    - Add `created_at: Long` (default: `System.currentTimeMillis()`).
    - Add `ttl: Long` (default: 30 days in millis).
- [ ] Update `FavoriteEntity` & `LogEntity`:
    - Replace `zipcode_place: String` with `place_id: Long`.
    - Update `@ForeignKey` to reference `PlaceEntity(id)`.
- [ ] Update `PlaceWithNotes` relation to use `place_id` in the `Junction`.

---

### 2. Address-Centric Identity Logic
*Decoupling physical identity from the ZIP code.*

**User Story:**
As a user, I want my notes to follow my street address so that if the post office changes my ZIP 
code, I don't lose my data.

**Tasks:**
- [ ] Implement `CacheDao.findPlaceByPhysicalAddress(street, city, uf)`:
    - Should use normalized search fields to find matches regardless of current ZIP.
- [ ] Refactor `SearchRepository.updateCache(cep)`:
    - **Logic**: Search API -> Get Address -> Check DB for same Address (not ZIP) -> Update ZIP of 
  existing record if found OR Create new record.

---

### 3. Cache Lifecycle Management
*Automated cleanup of obsolete records.*

**User Story:**
As a system, I want to delete old non-favorite search results so that the device storage is 
used efficiently.

**Acceptance Criteria:**
- **Scenario**: Auto-cleanup of expired records
  - **Given** a non-favorite record created 31 days ago
  - **When** the app performs a cleanup routine
  - **Then** this record should be deleted.
- **Scenario**: Protection of favorite records
  - **Given** a favorite record created 60 days ago
  - **When** the app performs a cleanup routine
  - **Then** this record must **not** be deleted.

**Tasks:**
- [ ] Add `CacheDao.deleteExpiredCache(currentTime)`:
    - `DELETE FROM Places WHERE is_favorite = 0 AND (created_at + ttl) < :currentTime`.
- [ ] Trigger cleanup routine in `SearchRepository` or `AppDatabase` callback.

---

## 🧪 TDD Execution Plan

1.  **Red Phase**:
    - Write Room `MigrationTest` to verify that data survives the schema change.
    - Write unit tests for `updateCache` covering the "ZIP changed, Address stayed" scenario.
2.  **Green Phase**:
    - Increment `AppDatabase` version.
    - Implement `Migration` object with `ALTER TABLE` logic (or table recreation).
    - Update Entities and DAOs.
3.  **Refactor Phase**:
    - Clean up Repository logic to leverage the new IDs.
    - Verify that no `zipcode` strings are used as Foreign Keys in the entire project.

---
