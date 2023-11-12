package uk.brent.slater.lendinglibrary.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import uk.brent.slater.lendinglibrary.exception.BookAlreadyInLibraryException
import uk.brent.slater.lendinglibrary.model.Book
import uk.brent.slater.lendinglibrary.repository.BookRepository

class OwnerServiceTest {

    private val bookRepository : BookRepository = mockk();
    private val underTest = OwnerService(bookRepository)

    private val book = Book("Isbn12345", "Test Book", "Test Author", isBorrowed = false , isReference = false )

    @Test
    @DisplayName("Book with matching ISBN not already present so adds the book, calling save")
    fun addBook() {
        every { bookRepository.findByIsbn(book.isbn) } returns null
        every { bookRepository.save(book)} returns book

        assertEquals(book, underTest.addBook(book))

        verify(exactly = 1) { bookRepository.findByIsbn(book.isbn)}
        verify(exactly = 1) { bookRepository.save(book)}
    }
    @Test
    @DisplayName("Book with matching ISBN exists so exception")
    fun addBookException() {
        every { bookRepository.findByIsbn(book.isbn) } returns book

        val result = assertThrows<BookAlreadyInLibraryException> { underTest.addBook(book) }

        assertEquals("Book with ISBN Isbn12345 already exists", result.message)

        verify(exactly = 1) { bookRepository.findByIsbn(book.isbn)}
        verify(exactly = 0) { bookRepository.save(book)}
    }

    @Test
    fun getCountBorrowed() {
        every { bookRepository.countByIsBorrowedTrue() } returns 4

        assertEquals(4, underTest.getCountBorrowed())
        verify(exactly = 1) { bookRepository.countByIsBorrowedTrue()}
    }
}