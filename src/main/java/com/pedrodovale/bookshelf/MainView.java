package com.pedrodovale.bookshelf;

import com.pedrodovale.bookshelf.model.BookDto;
import com.pedrodovale.bookshelf.service.BookshelfService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.util.List;

@PageTitle("Bookshelf")
@Route("books")
@RouteAlias("")
public class MainView extends VerticalLayout {

  private final BookshelfService bookshelfService;

  public MainView(BookshelfService bookshelfService) {
    this.bookshelfService = bookshelfService;

    setSizeFull();
    setAlignItems(FlexComponent.Alignment.CENTER);

    H3 title = getTitle();
    Grid<BookDto> bookGrid = getGrid();
    Button addBookToShelfButton = getAddButton();

    Div gridWrapper = new Div(addBookToShelfButton, bookGrid);
    gridWrapper.setWidth("50%");

    add(title, gridWrapper);
  }

  private H3 getTitle() {
    return new H3("Bookshelf list");
  }

  private Grid<BookDto> getGrid() {
    Grid<BookDto> bookGrid = new Grid<>(BookDto.class, false);
    bookGrid.setEmptyStateText("No books found");

    bookGrid.addColumn(BookDto::getId).setVisible(false);
    bookGrid.addColumn(BookDto::getName).setHeader("Name");
    bookGrid.addColumn(BookDto::getAuthorName).setHeader("Author");
    bookGrid.addColumn(BookDto::getPublicationDate).setHeader("Publication Date");
    bookGrid.addColumn(BookDto::getReadDate).setHeader("Read Date");
    bookGrid.addColumn(BookDto::getRating).setHeader("Rating");

    bookGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    bookGrid.addItemDoubleClickListener(
            e -> {
              UI.getCurrent().navigate("books/edit?id=" + e.getItem().getId());
            });

    List<BookDto> books = bookshelfService.getAll();
    bookGrid.setItems(books);

    // default sorting done in service
    bookGrid.setSortableColumns();

    return bookGrid;
  }

  private Button getAddButton() {
    Button addBookToShelfButton = new Button("Add Book", new Icon(VaadinIcon.PLUS));
    addBookToShelfButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addBookToShelfButton.addClickListener(
            event -> {
              UI.getCurrent().navigate(BookFormView.class);
            });
    return addBookToShelfButton;
  }
}

