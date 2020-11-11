package se.ecutb.uppgift2.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class Book implements Serializable {


    private static final long serialVersionUID = 6961039772557106283L;
    @Id
    private String id;
    @NotEmpty(message = "A book needs to have ISBN!")
    private String isbn;
    @NotEmpty(message = "The book must have title...")
    private String title;
    @NotEmpty(message = "There needs to be an author!")
    private String author;
    @NotEmpty(message = "Book lacks genre!")
    private String genre;
    @NotEmpty(message = "Need to know when the book was released!")
    private String year;
    @NotNull
    private boolean isAvailable;
}
