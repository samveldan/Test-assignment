package org.example.testingproject.repositories;

import org.example.testingproject.models.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String author);

    @Query("select a from Author a left join fetch a.books where a.id = :id")
    Optional<Author> findByIdWithBooks(@Param("id") Long id);
}
