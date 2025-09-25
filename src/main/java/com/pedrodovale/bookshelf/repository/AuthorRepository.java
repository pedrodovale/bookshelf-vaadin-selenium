package com.pedrodovale.bookshelf.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
  Optional<Author> findByNameIgnoreCase(String authorName);
}
