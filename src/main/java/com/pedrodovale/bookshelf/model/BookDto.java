package com.pedrodovale.bookshelf.model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
  private UUID id;
  private String name;
  private String authorName;
  private LocalDate publicationDate;
  private LocalDate readDate;
  private Integer rating;
}
