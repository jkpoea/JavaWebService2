package se.ecutb.uppgift2.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import se.ecutb.uppgift2.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

}
