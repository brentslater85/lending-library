package uk.brent.slater.lendinglibrary.service

import org.springframework.stereotype.Service
import uk.brent.slater.lendinglibrary.exception.BookNotInLibraryException
import uk.brent.slater.lendinglibrary.exception.UnableToBorrowException
import uk.brent.slater.lendinglibrary.model.Book
import uk.brent.slater.lendinglibrary.repository.BookRepository

@Service
class BorrowerService(private val repository: BookRepository) {

    fun getByAuthor(author: String): List<Book> {
        val books = repository.findByAuthorIgnoreCase(author)
        if(books.isEmpty()) {
            throw BookNotInLibraryException("No book found with Author: $author")
        }
        return books
    }

    fun getByTitle(title: String): List<Book> {
        val books = repository.findByTitleIgnoreCase(title)
        if (books.isEmpty()) {
            throw BookNotInLibraryException("No book found with Title: $title")
        }
        return books
    }

    fun getByIsbn(isbn: String): Book {
        return repository.findByIsbn(isbn) ?: throw BookNotInLibraryException("No book found with ISBN: $isbn")
    }

    fun borrow(isbn: String): Book {
        val book = getByIsbn(isbn)

        if(book.isBorrowed) {
            throw UnableToBorrowException("Book already borrowed")
        }
        if(book.isReference) {
            throw UnableToBorrowException("Reference books are unable to be borrowed")
        }
        book.isBorrowed = true
        return repository.save(book)
    }

}
