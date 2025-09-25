package com.pedrodovale.bookshelf.service;

import com.pedrodovale.bookshelf.model.BookDto;
import com.pedrodovale.bookshelf.repository.Author;
import com.pedrodovale.bookshelf.repository.AuthorRepository;
import com.pedrodovale.bookshelf.repository.Book;
import com.pedrodovale.bookshelf.repository.BookRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookshelfService {

  private final ConversionService conversionService;
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;

  @Transactional
  public void add(BookDto bookDto) {
    Book book = Objects.requireNonNull(conversionService.convert(bookDto, Book.class));

    String authorName = bookDto.getAuthorName();
    authorRepository
        .findByNameIgnoreCase(authorName)
        .ifPresentOrElse(
            book::setAuthor,
            () -> {
              Author author = new Author();
              author.setName(authorName);
              book.setAuthor(author);
            });

    bookRepository.save(book);
  }

  public List<BookDto> getAll() {
    return bookRepository.findAll(Sort.by(Sort.Direction.DESC, "readDate")).stream()
        .map(book -> conversionService.convert(book, BookDto.class))
        .collect(Collectors.toList());
  }

  public BookDto get(UUID bookId) {
    return conversionService.convert(
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("book not found: " + bookId)),
        BookDto.class);
  }

    public boolean existsByName(String bookName) {
        return bookRepository.existsByName(bookName);
    }
}
