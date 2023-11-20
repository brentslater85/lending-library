package uk.brent.slater.lendinglibrary.service

import uk.brent.slater.lendinglibrary.exception.BookAlreadyInLibraryException
import uk.brent.slater.lendinglibrary.exception.BookNotInLibraryException
import uk.brent.slater.lendinglibrary.exception.UnableToBorrowException
import uk.brent.slater.lendinglibrary.model.Book

class Library {

    private val books = mutableListOf<Book>();

    fun addBook(book: Book) {
        val existingBook = filterByIsbn(book.isbn)

        if(existingBook.isEmpty()) {
            books.add(book);
        } else {
            throw BookAlreadyInLibraryException("Book with ISBN ${book.isbn} already exists")
        }
    }

    fun getByAuthor(author: String): List<Book> {
        val filteredBooks = books.filter {
            book -> book.author == author
        }
        if(filteredBooks.isEmpty()) {
            throw BookNotInLibraryException("No book found with Author: $author")
        }
        return filteredBooks
    }

    fun getByTitle(title: String): List<Book> {
        // returns a list because 2 books could have the same name but be different books
        val filteredBooks = books.filter {
                book -> book.title == title
        }
        if(filteredBooks.isEmpty()) {
            throw BookNotInLibraryException("No book found with Title: $title")
        }
        return filteredBooks
    }

    fun getByIsbn(isbn: String): Book {
        val filteredBooks = filterByIsbn(isbn)
        if(filteredBooks.isEmpty()) {
            throw BookNotInLibraryException("No book found with ISBN: $isbn")
        }
        return filteredBooks[0]
    }

    fun borrow(isbn: String) {
        val book = getByIsbn(isbn)

        if(book.isBorrowed) {
            throw UnableToBorrowException("Book already borrowed")
        }
        if(book.isReference) {
            throw UnableToBorrowException("Reference books are unable to be borrowed")
        }
        book.isBorrowed = true
    }

    fun getCountBorrowed() : Int {
        val borrowedBooks = books.filter { book ->
            book.isBorrowed
        }
        return borrowedBooks.size
    }

    private fun filterByIsbn(isbn: String): List<Book> {
        return books.filter {
                book -> book.isbn == isbn
        }
    }

}
