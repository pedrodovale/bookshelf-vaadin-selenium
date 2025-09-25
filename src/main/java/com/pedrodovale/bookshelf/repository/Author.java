package com.pedrodovale.bookshelf.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_AUTHOR")
@Getter
@Setter
public class Author {

  @Id
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @Column(name = "ID", nullable = false)
  private UUID id;

  @Column(name = "NAME")
  private String name;
}
