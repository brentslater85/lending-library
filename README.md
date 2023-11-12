# lending-library
This was the first Kotlin project that I've attempted.

Implements a lending library where an owner can add books, it is assumed that there is only one copy of each book
Ananonymous users are able to search the library by title (case insensitive), author (case insensitive) or ISBN (Case insensitive). I have assumed that ISBN is unique and the owener cannot enter two books with the same ISBN

The owner can get a count of the number of books that are borrowed.

Extensions would be to 
* implement security on the Owner API
* implement user accounts for borrowers
* allow borrowers to return books
* allow more than one copy of each book
* Integration tests
