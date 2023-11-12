package uk.brent.slater.lendinglibrary.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.brent.slater.lendinglibrary.model.Book

@Repository
interface BookRepository : JpaRepository<Book, String> {

    fun findByAuthorIgnoreCase(author: String): List<Book>
    fun findByTitleIgnoreCase(title: String): List<Book>
    fun findByIsbn(isbn: String): Book?
    fun countByIsBorrowedTrue(): Long
}