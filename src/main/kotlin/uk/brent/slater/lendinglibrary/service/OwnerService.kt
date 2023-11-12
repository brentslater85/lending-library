package uk.brent.slater.lendinglibrary.service

import org.springframework.stereotype.Service
import uk.brent.slater.lendinglibrary.exception.BookAlreadyInLibraryException
import uk.brent.slater.lendinglibrary.model.Book
import uk.brent.slater.lendinglibrary.repository.BookRepository

@Service
class OwnerService(private val repository: BookRepository) {

    fun addBook(book: Book) : Book {
        val isbn = book.isbn
        if(repository.findByIsbn(isbn) == null ) {
            return repository.save(book);
        }
        throw BookAlreadyInLibraryException("Book with ISBN $isbn already exists")
    }

    fun getCountBorrowed() : Long {
        return repository.countByIsBorrowedTrue();
    }


}