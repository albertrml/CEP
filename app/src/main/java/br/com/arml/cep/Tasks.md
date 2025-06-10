1) List of Cep Search Entry:
       a - Check whether the cep is already in the database;
       b - If not, Request the entry from API and Save it in the database;
2) List of Favorite Cep Search Entry:
    a - Request from the database all the favorite entries;
3) Mark a Entry as Favorite:
    a - Select the entry is not marked as saved
    b - Mark the entry as saved
    c - Save the entry in the database
4) Unmark a Entry as Favorite:
    a - Select the entry is marked as saved
    b - Unmark the entry as saved
    c - Save the entry in the database
5) Filter the Cep Search Entry:
    a - get a query string
    b - filter the database by the query string
6) Filter the Favorite Cep Search Entry:
    a - get a query string
    b - filter the database by the query string and marked as favorite
7) Filter the Cep Search Entry by Initial Timestamp:
    a - get a initial timestamp
    b - filter the database by the initial timestamp
8) Filter the Cep Search Entry by End Timestamp:
    a - get a end end timestamp
    b - filter the database by the end timestamp
9) Filter the Cep Search Entry by range Timestamp:
    a - get a start and end timestamp
    b - filter the database by the range timestamp
10) Delete a Cep Search Entry:
    a - get a cep search entry is not marked as favorite
    b - delete the entry from the database
11) Delete a List Cep Search Entry:
    a - get a list of cep search entry is not marked as favorite
    b - For each item in the list, delete the entry from the database


PlaceEntry(cep (k), address, favorite, note) - From API, where cep requests address
HistoryEntry(id, cep (fk), timestamp) - From database, where cep requests timestamp

PlaceEntry (1) <-> HistoryEntry (n)
PlaceEntry (1) <-> FavoriteEntry (1)


Cep Screen
1) Search an address by the CEP:
    A) The user enters a valid cep and presses the search button.
    B) The app searches for the corresponding CEP in the database:
        a) if it exists, the app gets the address and creates new entries into the place and history tables.
        b) if it doesn't exist, the app requests the corresponding address from API by sending the CEP:
            I  - If it doesn't exist, show an error message.
            II - Otherwise, the app creates new entries into place and history tables from the received address.

The Log Screen
1) Show a list of log entries
    A) The user selects the log screen
    B) The app gets all entries from the log table
    C) The app shows them.
2) Delete a single unwanted log entry.
    A) The user selects a log entry to delete.
    B) The app deletes this log entry from the log table
3) Delete a List of unwanted log entries.
    A) The user clicks on the list to delete button.
    B) When the user toggles on an item to delete, the confirmation button shows up on the screen
    C) After choosing the items, the user clicks the confirmation button.
    D) The app deletes the list from the log table.
4) Filter the log entries by Cep
    A) The user sends a query cep and clicks on the search button
    B) The app fetches all corresponding entries and shows them
5) Filter the log entries by Initial Timestamp
    A) The user sends an initial timestamp and clicks on the search button
    B) The app fetches all corresponding entries and shows them
6) Filter the log entries by End Timestamp
    A) The user sends a final timestamp and clicks on the search button
    B) The app fetches all corresponding entries and shows them
7) Filter the log entries by range Timestamp
    A) The user sends an initial and a final timestamp and clicks the search button.
    B) The app fetches all corresponding entries and shows them

Favorite Screen
1) Show a list of favorite entries
    A) The user selects the favorite screen
    B) The app fetches all favorite place entries
2) Unmark a favorite place entry as a favorite
    A) The user toggles off the favorite button of an item from the favorite list.
    B) The app alerts the user that the note information will be lost if the place entry is unmarked and asks for permission to proceed with the action.
    a) If the user allows it, the app updates the corresponding place entry from the place table with an unfavorite mark and null as a note.
    b) Otherwise, the app returns to the favorite screen.
3) Mark a place entry as a favorite
    A) In the favorite screen, the user selects  the unwanted place tab
    B) The app shows all unwanted place entries
    C) The user to toggle on the favorite button of an item from the unwanted place list
    D) The app updates the corresponding place entry from the place table with a favorite mark and a new note, in which the title and content are zip code and empty, respectively.
4) Delete a favorite place entry
    A) The user long clicks on a favorite place entry
    B) The app asks the user wishes to proceed with the delete operation for this item.
        a) If yes, the app removes the favorite place from the database.
        b) Otherwise, the app returns to the favorite screen.
5) Delete all unwanted place entries
    A) The user clicks on the clear button in the unwanted place entries
    B) The app alerts the user that it will remove all unwanted place entries from the database as the app proceeds.
        a) If the user allows the app to proceed, it removes all place entries that it is not marked as favorites.
        b) Otherwise, it returns to the unwanted place list
6) Edit a Favorite Entry
    A) The user clicks on a favorite place entry
    B) The app opens the favorite place detail screen.
    C) The user edits the title and content associated with the favorite place entry
    D) The user clicks on the save button
    E) The app updates the favorite place entry and returns to the favorite screen.
7) Filter the favorite entries by Cep
    A) The user sends a query cep and clicks on the search button
    B) The app fetches all corresponding entries and shows them
8) Filter the favorite entries by Initial Timestamp
    A) The user sends an initial timestamp and clicks on the search button
    B) The app fetches all corresponding entries and shows them
9) Filter the favorite entries by End Timestamp
    A) The user sends a final timestamp and clicks on the search button
    B) The app fetches all corresponding entries and shows them
10) Filter the favorite entries by range Timestamp
    A) The user sends an initial and a final timestamp and clicks the search button.
    B) The app fetches all corresponding entries and shows them

