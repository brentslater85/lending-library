package uk.brent.slater.lendinglibrary.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import uk.brent.slater.lendinglibrary.exception.BookAlreadyInLibraryException
import uk.brent.slater.lendinglibrary.exception.BookNotInLibraryException
import uk.brent.slater.lendinglibrary.exception.UnableToBorrowException
import uk.brent.slater.lendinglibrary.model.Book

class LibraryTest {

    private lateinit var underTest : Library

    private val book0 = Book("0000000000", "Book 0", "Author0", isBorrowed = false , isReference = false );
    private val book1 = Book("1111111111", "Book 1", "Author0", isBorrowed = false , isReference = false )
    // deliberaty 2 written by same author
    private val book2 = Book("2222222222", "Book 2", "Author1", isBorrowed = false , isReference = false )
    private val book3 = Book("3333333333", "Book 3", "Author2", isBorrowed = true , isReference = false )
    private val book4 = Book("4444444444", "Book 4", "Author3", isBorrowed = false , isReference = false )
    private val book5 = Book("5555555555", "Book 5", "Author4", isBorrowed = false , isReference = false )
    private val book6 = Book("6666666666", "Book 6", "Author5", isBorrowed = false , isReference = true)
    private val book7 = Book("7777777777", "Book 7", "Author6", isBorrowed = false , isReference = true )
    private val book8 = Book("8888888888", "Book 8", "Author7", isBorrowed = false , isReference = false )
    private val book9 = Book("9999999999", "Book 9", "Author8", isBorrowed = true , isReference = false )

    @BeforeEach
    fun setUp() {
        underTest = Library();
        underTest.addBook(book0)
        underTest.addBook(book1)
        underTest.addBook(book2)
        underTest.addBook(book3)
        underTest.addBook(book4)
        underTest.addBook(book5)
        underTest.addBook(book6)
        underTest.addBook(book7)
        underTest.addBook(book8)
        underTest.addBook(book9)
    }


    @Test
    @DisplayName("Get by author, 2 books written by Author0 returned returned")
    fun getByAuthor() {
        val queryString = "Author0"

        val result = underTest.getByAuthor(queryString)

        assertEquals(2, result.size)
        assertTrue(result.containsAll(listOf(book0, book1)))
    }

    @Test
    @DisplayName("Fail to add book if ISBN already exists")
    fun AddBookAlreadyExists() {
        val result = assertThrows<BookAlreadyInLibraryException> {underTest.addBook(book0)}
        assertEquals("Book with ISBN ${book0.isbn} already exists", result.message)
    }

    @Test
    @DisplayName("Get by author, No book")
    fun getByAuthorNoBook() {
        val queryString = "Another author"
        val result = assertThrows<BookNotInLibraryException> {underTest.getByAuthor(queryString)}
        assertEquals("No book found with Author: $queryString", result.message)
    }

    @Test
    @DisplayName("Get by title, book returned")
    fun getByTitle() {
        val queryString = "Book 5"

        val result = underTest.getByTitle(queryString)

        assertEquals(1, result.size)
        assertEquals(book5, result[0])
    }

    @Test
    @DisplayName("Get by title, No book")
    fun getByTitleNoBook() {
        val queryString = "Other title"

        val result = assertThrows<BookNotInLibraryException> {underTest.getByTitle(queryString)}
        assertEquals("No book found with Title: $queryString", result.message)
    }

    @Test
    @DisplayName("Get by ISBN, book returned")
    fun getByIsbn() {
        val queryString = "2222222222"

        val result = underTest.getByIsbn(queryString)

        assertEquals(book2, result)
    }

    @Test
    @DisplayName("Get by ISBN, No book")
    fun getByIsbnNoBook() {
        val queryString = "Other ISBN"

        val result = assertThrows<BookNotInLibraryException> {underTest.getByIsbn(queryString)}
        assertEquals("No book found with ISBN: $queryString", result.message)
    }

    @Test
    @DisplayName("Borrow book")
    fun borrow() {
        val queryString = "1111111111"
        val bookInitially = underTest.getByIsbn("1111111111");
        assertFalse(bookInitially.isBorrowed);

        underTest.borrow(queryString)
        val bookNow = underTest.getByIsbn("1111111111");

        assertTrue(bookNow.isBorrowed);
    }

    @Test
    @DisplayName("Fail to borrow reference book")
    fun borrowReference() {
        val queryString = "6666666666" // isbn of a reference book

        val result = assertThrows<UnableToBorrowException> {underTest.borrow(queryString)}
        assertEquals("Reference books are unable to be borrowed", result.message)
    }

    @Test
    @DisplayName("Fail to borrow borrowed book")
    fun borrowBorrowed() {
        val queryString = "3333333333" // isbn of a borrowed book

        val result = assertThrows<UnableToBorrowException> {underTest.borrow(queryString)}
        assertEquals("Book already borrowed", result.message)
    }

    @Test
    @DisplayName("Testing the count borrowed books functionality")
    fun countBorrowed() {
        assertEquals(2, underTest.getCountBorrowed())
        underTest.borrow(book0.isbn)
        assertEquals(3, underTest.getCountBorrowed())
    }
}