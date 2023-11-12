package uk.brent.slater.lendinglibrary.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import uk.brent.slater.lendinglibrary.model.Book
import uk.brent.slater.lendinglibrary.service.BorrowerService
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import uk.brent.slater.lendinglibrary.exception.BookNotInLibraryException
import uk.brent.slater.lendinglibrary.exception.UnableToBorrowException

@WebMvcTest(BorrowerController::class)
class BorrowerControllerTest(@Autowired val mockMvc : MockMvc) {

    @MockkBean
    lateinit var borrowerService: BorrowerService;

    private val book = Book("ISBN", "Test Book", "Test Author", isBorrowed = false , isReference = false )

    @Test
    @DisplayName("Successfully get by author")
    fun getByAuthor() {
        val queryString = "author";
        every { borrowerService.getByAuthor(queryString) } returns listOf(book)

        mockMvc.perform(get("/api/borrower/author/$queryString"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath(".isbn").value("ISBN"))

        verify(exactly = 1) { borrowerService.getByAuthor(queryString) }

    }

    @Test
    @DisplayName("Failed get by author")
    fun getByAuthor404() {
        val queryString = "author";
        every { borrowerService.getByAuthor(queryString) } throws BookNotInLibraryException("No book found");

        mockMvc.perform(get("/api/borrower/author/$queryString"))
            .andExpect(status().isNotFound())

        verify(exactly = 1) { borrowerService.getByAuthor(queryString) }
    }

    @Test
    @DisplayName("Successfully get by author")
    fun getByTitle() {
        val queryString = "Title";
        every { borrowerService.getByTitle(queryString) } returns listOf(book)

        mockMvc.perform(get("/api/borrower/title/$queryString"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath(".isbn").value("ISBN"))

        verify(exactly = 1) { borrowerService.getByTitle(queryString) }

    }

    @Test
    @DisplayName("Failed get by author")
    fun getByTitle404() {
        val queryString = "Title";
        every { borrowerService.getByTitle(queryString) } throws BookNotInLibraryException("No book found");

        mockMvc.perform(get("/api/borrower/title/$queryString"))
            .andExpect(status().isNotFound())

        verify(exactly = 1) { borrowerService.getByTitle(queryString) }
    }

    @Test
    @DisplayName("Successfully get by ISBN")
    fun getByIsbn() {
        val queryString = "isbn";
        every { borrowerService.getByIsbn(queryString) } returns book

        mockMvc.perform(get("/api/borrower/isbn/$queryString"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath(".isbn").value("ISBN"))

        verify(exactly = 1) { borrowerService.getByIsbn(queryString) }

    }

    @Test
    @DisplayName("Failed get by ISBN")
    fun getByIsbn404() {
        val queryString = "isbn";
        every { borrowerService.getByIsbn(queryString) } throws BookNotInLibraryException("No book found");

        mockMvc.perform(get("/api/borrower/isbn/$queryString"))
            .andExpect(status().isNotFound())
        verify(exactly = 1) { borrowerService.getByIsbn(queryString) }

    }
    @Test
    @DisplayName("Successfully borrow book")
    fun borrow() {
        val queryString = "isbn";
        every { borrowerService.borrow(queryString) } returns book

        mockMvc.perform(get("/api/borrower/borrow/$queryString"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath(".isbn").value("ISBN"))

        verify(exactly = 1) { borrowerService.borrow(queryString) }

    }

    @Test
    fun borrowUnable() {
        val queryString = "isbn";
        every { borrowerService.borrow(queryString) } throws UnableToBorrowException("Already Borrowed");

        mockMvc.perform(get("/api/borrower/borrow/$queryString"))
            .andExpect(status().isForbidden())

        verify(exactly = 1) { borrowerService.borrow(queryString) }

    }


}