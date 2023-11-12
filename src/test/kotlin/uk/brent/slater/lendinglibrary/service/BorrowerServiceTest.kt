package uk.brent.slater.lendinglibrary.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import uk.brent.slater.lendinglibrary.repository.BookRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import uk.brent.slater.lendinglibrary.exception.BookNotInLibraryException
import uk.brent.slater.lendinglibrary.exception.UnableToBorrowException
import uk.brent.slater.lendinglibrary.model.Book

class BorrowerServiceTest {

    private val bookRepository : BookRepository = mockk();
    private val underTest = BorrowerService(bookRepository)

    private val book = Book("ISBN", "Test Book", "Test Author", isBorrowed = false , isReference = false )

    @Test
    @DisplayName("Get by author, book returned")
    fun getByAuthor() {
        val queryString = "Test Author"
        every { bookRepository.findByAuthorIgnoreCase(queryString)} returns listOf(book)

        val result = underTest.getByAuthor(queryString)

        verify(exactly = 1) { bookRepository.findByAuthorIgnoreCase(queryString)}
        assertEquals(1, result.size)
        assertEquals(book, result[0])
    }

    @Test
    @DisplayName("Get by author, No book")
    fun getByAuthorNoBook() {
        val queryString = "Test Author"
        every { bookRepository.findByAuthorIgnoreCase(queryString)} returns emptyList();
        val result = assertThrows<BookNotInLibraryException> {underTest.getByAuthor(queryString)}
        assertEquals("No book found with Author: $queryString", result.message)
        verify(exactly = 1) { bookRepository.findByAuthorIgnoreCase(queryString)}

    }

    @Test
    @DisplayName("Get by title, book returned")
    fun getByTitle() {
        val queryString = "Test title"
        every { bookRepository.findByTitleIgnoreCase(queryString)} returns listOf(book)

        val result = underTest.getByTitle(queryString)

        verify(exactly = 1) { bookRepository.findByTitleIgnoreCase(queryString)}
        assertEquals(1, result.size)
        assertEquals(book, result[0])
    }

    @Test
    @DisplayName("Get by title, No book")
    fun getByTitleNoBook() {
        val queryString = "Test title"
        every { bookRepository.findByTitleIgnoreCase(queryString)} returns emptyList();
        val result = assertThrows<BookNotInLibraryException> {underTest.getByTitle(queryString)}
        assertEquals("No book found with Title: $queryString", result.message)
    }

    @Test
    @DisplayName("Get by ISBN, book returned")
    fun getByIsbn() {
        val queryString = "isbn"
        every { bookRepository.findByIsbn(queryString)} returns book

        val result = underTest.getByIsbn(queryString)

        verify(exactly = 1) { bookRepository.findByIsbn(queryString)}
        assertEquals(book, result)
    }

    @Test
    @DisplayName("Get by ISBN, No book")
    fun getByIsbnNoBook() {
        val queryString = "isbn"
        every { bookRepository.findByIsbn(queryString)} returns null

        val result = assertThrows<BookNotInLibraryException> {underTest.getByIsbn(queryString)}
        assertEquals("No book found with ISBN: $queryString", result.message)
    }

    @Test
    @DisplayName("Borrow book")
    fun borrow() {
        val queryString = "isbn"
        every { bookRepository.findByIsbn(queryString)} returns book
        every { bookRepository.save(book)} returns book

        val result = underTest.borrow(queryString)
        assertEquals(book, result);
        verify(exactly = 1) { bookRepository.findByIsbn(queryString)}
        verify(exactly = 1) { bookRepository.save(book)}
    }

    @Test
    @DisplayName("Fail to borrow reference book")
    fun borrowReference() {
        val queryString = "Ref"
        val referenceBook = Book("Ref", "ReferenceBook", "Test Author",
            isBorrowed = false , isReference = true )
        every { bookRepository.findByIsbn(queryString)} returns referenceBook

        val result = assertThrows<UnableToBorrowException> {underTest.borrow(queryString)}
        assertEquals("Reference books are unable to be borrowed", result.message)
        verify(exactly = 1) { bookRepository.findByIsbn(queryString)}
        verify(exactly = 0) { bookRepository.save(any())}
    }

    @Test
    @DisplayName("Fail to borrow borrowed book")
    fun borrowBorrowed() {
        val queryString = "Borrowed"
        val borrowedBook = Book("Borrowed", "Borrowed Book", "Test Author",
            isBorrowed = true , isReference = false )
        every { bookRepository.findByIsbn(queryString)} returns borrowedBook

        val result = assertThrows<UnableToBorrowException> {underTest.borrow(queryString)}
        assertEquals("Book already borrowed", result.message)
        verify(exactly = 1) { bookRepository.findByIsbn(queryString)}
        verify(exactly = 0) { bookRepository.save(any())}
    }
}