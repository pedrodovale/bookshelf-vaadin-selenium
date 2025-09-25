package com.pedrodovale.bookshelf.repository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_BOOK")
@Getter
@Setter
public class Book {

  @Id
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @Column(name = "ID", nullable = false)
  private UUID id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID")
  private Author author;

  @Column(name = "NAME")
  private String name;

  @Column(name = "PUBLICATION_DATE")
  private LocalDate publicationDate;

  @Column(name = "READ_DATE")
  private LocalDate readDate;

  @Column(name = "RATING")
  private Integer rating;
}
