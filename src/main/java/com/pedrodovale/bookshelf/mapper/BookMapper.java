package com.pedrodovale.bookshelf.mapper;

import com.pedrodovale.bookshelf.model.BookDto;
import com.pedrodovale.bookshelf.repository.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public abstract class BookMapper implements Converter<Book, BookDto> {

  @Mapping(source = "author.name", target = "authorName")
  public abstract BookDto convert(Book source);
}
