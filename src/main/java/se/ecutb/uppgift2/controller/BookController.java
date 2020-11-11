package se.ecutb.uppgift2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.ecutb.uppgift2.entity.Book;
import se.ecutb.uppgift2.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    public BookService bookService;

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks(@RequestParam(required = false) String isbn,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String author,
                                                   @RequestParam(required = false) String genre,
                                                   @RequestParam(required = false) String year,
                                                   @RequestParam(required = false) boolean sortOnTitle,
                                                   @RequestParam(required = false) boolean sortOnAuthor,
                                                   @RequestParam(required = false) boolean sortOnGenre,
                                                   @RequestParam(required = false) boolean sortOnYear,
                                                   @RequestParam(required = false) boolean isAvailable){
        return ResponseEntity.ok(bookService.findAll(isbn, title, author, genre, year, sortOnTitle, sortOnAuthor,
                sortOnGenre, sortOnYear, isAvailable));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable String id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<Book> saveBook(@Validated @RequestBody Book book){
        return ResponseEntity.ok(bookService.save(book));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@PathVariable String id, @Validated @RequestBody Book book){

        bookService.update(id, book);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id){
        bookService.delete(id);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/{id}/borrow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrowBook(@PathVariable String id, @Validated @RequestBody Book book){
        bookService.borrowBook(id, book);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/{id}/return")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnBook(@PathVariable String id, @Validated @RequestBody Book book){
        bookService.returnBook(id, book);
    }



}
