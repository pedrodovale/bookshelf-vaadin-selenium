package com.pedrodovale.bookshelf.mapper;

import com.pedrodovale.bookshelf.model.BookDto;
import com.pedrodovale.bookshelf.repository.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public abstract class BookDtoMapper implements Converter<BookDto, Book> {

  @Mapping(source = "authorName", target = "author.name")
  public abstract Book convert(BookDto source);
}
