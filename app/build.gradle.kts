plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "br.com.arml.cep"
    compileSdk = 37

    defaultConfig {
        applicationId = "br.com.arml.cep"
        minSdk = 26
        targetSdk = 37
        versionCode = 7
        versionName = "3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("androidTest") {
            assets.directories.add("schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources.excludes.add("META-INF/gradle/incremental.annotation.processors")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Adaptive Layout
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.compose.adaptive.layout)
    implementation(libs.androidx.compose.adaptive.navigation)
    implementation(libs.androidx.compose.adaptive.navigation.suite)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.google.errorprone.annotations)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Hilt
    implementation(libs.android.hilt)
    implementation(libs.androidx.compose.hilt)
    ksp(libs.android.hilt.compiler)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Jetpack Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.windowsizeclass)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))

    // Networking (Retrofit + Moshi)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.converter.moshi)
    implementation(libs.squareup.moshi)
    implementation(libs.squareup.moshi.kotlin)
    implementation(libs.squareup.logging.interceptor)

    // Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Shared Test Dependencies
    val sharedTestLibs = listOf(
        libs.google.truth,
        libs.mockk,
        libs.androidx.ui.test.junit
    )
    sharedTestLibs.forEach {
        testImplementation(it)
        androidTestImplementation(it)
    }

    // Test Dependencies
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android Test Dependencies
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.jetbrains.kotlin.test)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Debug Dependencies
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
}