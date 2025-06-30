package org.example.testingproject.repositories;

import org.example.testingproject.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b join fetch b.author")
    List<Book> findAll();

    Optional<Book> findByTitle(String title);
}
