package uk.brent.slater.lendinglibrary.repository

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.brent.slater.lendinglibrary.model.Book

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var repositoryUnderTest: BookRepository

    val book = Book("ISBN", "Test Book", "Test Author", isBorrowed = false, isReference = false)

    @BeforeEach
    fun setup() {

        entityManager.clear();
        entityManager.persist(book);
        entityManager.flush();

    }

    @AfterEach
    fun cleanup() {
        entityManager.clear()
        entityManager.flush()
    }

    @Test
    @DisplayName("Successfully find book by author")
    fun successfullyRetrieveByAuthor() {
        // test for case insensitivity too
        val findLowercase = repositoryUnderTest.findByAuthorIgnoreCase("test author")
        val findUppercase  = repositoryUnderTest.findByAuthorIgnoreCase("TEST AUTHOR")
        assertEquals(1, findLowercase.size)
        assertEquals(1, findUppercase.size)
        assertEquals(book, findLowercase[0])
        assertEquals(book, findUppercase[0])
    }

    @Test
    @DisplayName("Fail to find book by author")
    fun failedRetrieveByAuthor() {
        val result = repositoryUnderTest.findByAuthorIgnoreCase("Fake Author")
        assertEquals(0, result.size)
    }

    @Test
    @DisplayName("Successfully find book by title")
    fun successfullyRetrieveByTitle() {
        // test for case insensitivity too
        val findLowercase = repositoryUnderTest.findByTitleIgnoreCase("test book")
        val findUppercase  = repositoryUnderTest.findByTitleIgnoreCase("TEST BOOK")
        assertEquals(1, findLowercase.size)
        assertEquals(1, findUppercase.size)
        assertEquals(book, findLowercase[0])
        assertEquals(book, findUppercase[0])
    }

    @Test
    @DisplayName("Fail to find book by title")
    fun failedRetrieveByTitle() {
        val result = repositoryUnderTest.findByAuthorIgnoreCase("Fake Book")
        assertEquals(0, result.size)
    }

    @Test
    @DisplayName("Successfully find book by isbn")
    fun successfullyRetrieveByIsbn() {
        val result = repositoryUnderTest.findByIsbn("ISBN")
        assertEquals(book, result)
    }

    @Test
    @DisplayName("Fail to find book by isbn")
    fun failedRetrieveByIsbn() {
        val result = repositoryUnderTest.findByIsbn("Something Else")
        assertNull(result)
    }

    @Test
    @DisplayName("With one borrowed book and one not borrowed count returns 2 but countByIsBorrowed returns 1")
    fun countByIsBorrowed() {
        entityManager.persist(Book("BorrowedISBN", "Popular Book",
            "Popular Author", isBorrowed = true, isReference = false));
        assertEquals(2, repositoryUnderTest.count())
        assertEquals(1, repositoryUnderTest.countByIsBorrowedTrue())
    }

}