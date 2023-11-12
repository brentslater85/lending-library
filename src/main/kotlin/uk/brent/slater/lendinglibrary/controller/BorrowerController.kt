package uk.brent.slater.lendinglibrary.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.brent.slater.lendinglibrary.model.Book
import uk.brent.slater.lendinglibrary.service.BorrowerService

@RestController
@RequestMapping("/api/borrower/")
class BorrowerController(private val borrowerService: BorrowerService) {
    

    @GetMapping("author/{author}")
    fun getByAuthor(@PathVariable author: String): List<Book> {
        return borrowerService.getByAuthor(author)
    }

    @GetMapping("title/{title}")
    fun getByTitle(@PathVariable title: String): List<Book> {
        return borrowerService.getByTitle(title)
    }

    @GetMapping("isbn/{isbn}")
    fun getByIsbn(@PathVariable isbn: String): Book {
        return borrowerService.getByIsbn(isbn)
    }

    @GetMapping("borrow/{isbn}")
    fun borrow(@PathVariable isbn: String): Book {
        return borrowerService.borrow(isbn)
    }

}
