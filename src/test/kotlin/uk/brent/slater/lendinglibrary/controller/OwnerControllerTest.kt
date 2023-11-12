package uk.brent.slater.lendinglibrary.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import uk.brent.slater.lendinglibrary.exception.BookAlreadyInLibraryException
import uk.brent.slater.lendinglibrary.model.Book
import uk.brent.slater.lendinglibrary.service.OwnerService

@WebMvcTest(OwnerController::class)
class OwnerControllerTest(@Autowired val mockMvc : MockMvc) {

    @MockkBean
    lateinit var ownerService: OwnerService;

    val mapper = jacksonObjectMapper()

    private val book = Book("ISBN", "Test Book", "Test Author", isBorrowed = false , isReference = false )

    @Test
    @DisplayName("Successfully add book")
    fun addBook() {
        every { ownerService.addBook(any()) } returns book

        mockMvc.perform(MockMvcRequestBuilders.post("/api/owner")
                .content(mapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath(".isbn").value("ISBN"))

        verify(exactly = 1) { ownerService.addBook(any()) }

    }

    @Test
    @DisplayName("Book already exists")
    fun addBookConflict() {
        every { ownerService.addBook(any()) } throws BookAlreadyInLibraryException("Book already in library")

        mockMvc.perform(MockMvcRequestBuilders.post("/api/owner")
            .content(mapper.writeValueAsString(book))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isConflict())

        verify(exactly = 1) { ownerService.addBook(any()) }
    }

    @Test
    fun numberBorrowed() {
        every { ownerService.getCountBorrowed() } returns 4

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/owner/numberBorrowed")
            .content(mapper.writeValueAsString(book))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        assertEquals("4", result.response.contentAsString)

        verify(exactly = 1) { ownerService.getCountBorrowed() }
    }
}