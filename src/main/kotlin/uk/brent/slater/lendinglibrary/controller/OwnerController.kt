package uk.brent.slater.lendinglibrary.controller

import org.springframework.web.bind.annotation.*
import uk.brent.slater.lendinglibrary.model.Book
import uk.brent.slater.lendinglibrary.service.OwnerService

@RestController
@RequestMapping("/api/owner")
class OwnerController(private val ownerService: OwnerService) {

    @PostMapping
    fun addBook(@RequestBody book : Book): Book {
        return ownerService.addBook(book)
    }

    @GetMapping("/numberBorrowed")
    fun numberBorrowed() : Long {
        return ownerService.getCountBorrowed();
    }


}