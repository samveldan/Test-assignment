package org.example.testingproject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.testingproject.enums.Genre;

@Entity
@Table(name = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "author")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private int year;
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    public Book(String title, int year, Genre genre, Author author) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.author = author;
    }
}
