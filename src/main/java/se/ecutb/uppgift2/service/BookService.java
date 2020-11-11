package se.ecutb.uppgift2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.ecutb.uppgift2.entity.Book;
import se.ecutb.uppgift2.repository.BookRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Cacheable(value = "bookCache")
    public List<Book> findAll(String isbn, String title, String author, String genre, String year,
                              boolean sortOnTitle, boolean sortOnAuthor, boolean sortOnGenre, boolean sortOnYear,
                              boolean isAvailable){
        List<Book> books = bookRepository.findAll();
        log.info("Searching through all books");
        log.info("Fresh data");

        if (isbn != null){
            books = books.stream()
                    .filter(book -> book.getIsbn().equalsIgnoreCase(isbn))
                    .collect(Collectors.toList());
        }

        if (title != null){
            books = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(title))
                    .collect(Collectors.toList());
        }

        if (author != null){
           books = books.stream()
                    .filter(book -> book.getAuthor().toLowerCase().contains(author))
                    .collect(Collectors.toList());
        }

        if (genre != null) {
          books =  books.stream()
                    .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }

        if (year != null){
         books = books.stream()
                    .filter(book -> book.getYear().contains(year))
                    .collect(Collectors.toList());
        }
        if (sortOnTitle){
            books.sort(Comparator.comparing(Book::getTitle));
        }

        if (sortOnAuthor){
            books.sort(Comparator.comparing(Book::getAuthor));
        }

        if (sortOnGenre){
            books.sort(Comparator.comparing(Book::getGenre));
        }

        if (sortOnYear){
            books.sort(Comparator.comparing(Book::getYear));
        }

        if (isAvailable){
          books = books.stream()
                    .filter(book -> book.isAvailable() == true)
                    .collect(Collectors.toList());
        }
        return books;
    }

    @Cacheable(value = "bookCache", key = "#id")
    public Book findById(String id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Could not find book by id %s", id)));
    }



    @CachePut(value = "bookCache", key = "#result.id")
    public Book save(Book book){
        return bookRepository.save(book);
    }

    @CachePut(value = "bookCache", key = "#id")
    public void update(String id, Book book){

        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find the book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find the user by id %s"));
        }
        book.setId(id);
        bookRepository.save(book);
    }
    @CacheEvict(value = "bookCache", key = "#id")
    public void delete(String id) {
        if(!bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find the user by id %s", id));
        }
        bookRepository.deleteById(id);
    }

    @CachePut(value = "bookCache", key = "#id")
    public void borrowBook(String id, Book book) {
        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find the book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find the user by id %s"));
        }
        if (!bookRepository.findById(id).get().isAvailable()) {
            log.error(String.format("Book %s is not available", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Book %s is not available"));
        }
        book.setId(id);
        book.setAvailable(false);
        bookRepository.save(book);
        }

        public void returnBook(String id, Book book) {
            if(!bookRepository.existsById(id)) {
                log.error(String.format("Could not find the book by id %s.", id));
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Could not find the user by id %s"));
            }
            book.setId(id);
            book.setAvailable(true);
            bookRepository.save(book);
    }


}
