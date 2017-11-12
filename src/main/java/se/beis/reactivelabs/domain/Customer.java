package se.beis.reactivelabs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Generated;

@Document
@Data
public class Customer {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
