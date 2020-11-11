package se.ecutb.uppgift2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = -7088578071719641964L;
    @Id
    private String id;
    @NotEmpty(message = "Firstname can not be empty")
    @Size(min = 3, max = 10, message = "Firstname length is not valid")
    private String firstName;
    @NotEmpty(message = "Lastname can not be empty")
    @Size(min = 3, max = 10, message = "Lastname length not valid")
    private String lastName;
    @Past(message = "Birthday can not be present or in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate birthDate;
    @Indexed(unique = true)
    @Size(min = 4, max = 10, message = "Username length invalid")
    @NotBlank(message = "Username must contain a value")
    private String userName;
    private String passWord;
    @Email(message = "E-mail address not valid")
    private String email;
    @Pattern(regexp = "([0-9]){2,4}-([0-9]){5,8}", message = "Phone number not valid")
    private String phoneNum;
    private List<String> acl;
}
