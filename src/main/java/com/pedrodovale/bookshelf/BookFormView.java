package com.pedrodovale.bookshelf;

import static com.vaadin.flow.data.binder.ValidationResult.error;
import static com.vaadin.flow.data.binder.ValidationResult.ok;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.pedrodovale.bookshelf.model.BookDto;
import com.pedrodovale.bookshelf.service.BookshelfService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

@PageTitle("Bookshelf")
@Route("books/edit")
public class BookFormView extends VerticalLayout implements HasUrlParameter<String> {

  public static final String VIEW_COMPONENT_ID_BOOK_NAME = "book-name";
  public static final String VIEW_COMPONENT_ID_AUTHOR_NAME = "author-name";
  public static final String VIEW_COMPONENT_ID_PUBLICATION_DATE = "publication-date";
  public static final String VIEW_COMPONENT_ID_READ_DATE = "read-date";
  public static final String VIEW_COMPONENT_ID_RATING = "rating";

  private final BookshelfService bookshelfService;
  private Binder<BookDto> binder;

  public BookFormView(BookshelfService bookshelfService) {
    this.bookshelfService = bookshelfService;

    setAlignItems(Alignment.CENTER);

    H3 title = getTitle();
    FormLayout bookForm = getBookForm();

    Button addButton = getAddButton();
    Button cancelButton = getCancelButton();
    HorizontalLayout actionButtonsLayout =
        new HorizontalLayout(JustifyContentMode.CENTER, addButton, cancelButton);

    add(title, new HorizontalLayout(JustifyContentMode.CENTER, bookForm), actionButtonsLayout);
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    event
        .getLocation()
        .getQueryParameters()
        .getSingleParameter("id")
        .ifPresent(bookId -> binder.setBean(bookshelfService.get(UUID.fromString(bookId))));
  }

  private static H3 getTitle() {
    return new H3("Add a new book to the shelf");
  }

  private static Button getCancelButton() {
    Button cancelButton = new Button("Cancel", new Icon(VaadinIcon.CLOSE_SMALL));
    cancelButton.addClickListener(
        e -> {
          UI.getCurrent().navigate(MainView.class);
        });
    return cancelButton;
  }

  private FormLayout getBookForm() {

    FormLayout bookForm = new FormLayout();
    bookForm.setWidth("50%");

    TextField bookId = new TextField();

    TextField bookName = new TextField();
    bookName.setId(VIEW_COMPONENT_ID_BOOK_NAME);
    bookName.setRequiredIndicatorVisible(true);

    TextField authorName = new TextField();
    authorName.setId(VIEW_COMPONENT_ID_AUTHOR_NAME);
    authorName.setRequiredIndicatorVisible(true);

    DatePicker publicationDate = new DatePicker();
    publicationDate.setId(VIEW_COMPONENT_ID_PUBLICATION_DATE);

    DatePicker readDate = new DatePicker();
    readDate.setId(VIEW_COMPONENT_ID_READ_DATE);
    readDate.setValue(LocalDate.now());

    Select<Integer> rating = new Select<>();
    rating.setId(VIEW_COMPONENT_ID_RATING);
    rating.setItems(Arrays.asList(1, 2, 3, 4, 5));
    rating.setMaxWidth("70px");

    bookForm.addFormItem(bookName, "Book Name");
    bookForm.addFormItem(authorName, "Author Name");
    bookForm.addFormItem(publicationDate, "Publication Date");
    bookForm.addFormItem(readDate, "Read Date");
    bookForm.addFormItem(rating, "Rating");

    // Binder for the book DTO
    binder = new Binder<>(BookDto.class);
    binder.forField(bookId).withConverter(getUuidConverter()).bind(BookDto::getId, BookDto::setId);
    binder
        .forField(bookName)
        .asRequired("is required")
        .withValidator(
            (name, valueContext) ->
                bookshelfService.existsByName(name) ? error("already exists") : ok())
        .bind(BookDto::getName, BookDto::setName);
    binder
        .forField(authorName)
        .asRequired("is required")
        .bind(BookDto::getAuthorName, BookDto::setAuthorName);
    binder.forField(publicationDate).bind(BookDto::getPublicationDate, BookDto::setPublicationDate);
    binder.forField(readDate).bind(BookDto::getReadDate, BookDto::setReadDate);
    binder.forField(rating).bind(BookDto::getRating, BookDto::setRating);

    return bookForm;
  }

  private Button getAddButton() {
    Button addButton = new Button("Save");
    addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addButton.addClickListener(
        e -> {
          BookDto bookDto = new BookDto();
          if (binder.writeBeanIfValid(bookDto)) {
            try {
              bookshelfService.add(bookDto);
              UI.getCurrent().navigate(MainView.class);
              Notification successfulNotification =
                  Notification.show(
                      "Book saved successfully", 5000, Notification.Position.BOTTOM_CENTER);
              successfulNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception exception) {
              Notification successfulNotification =
                  Notification.show("Error saving Book", 5000, Notification.Position.BOTTOM_CENTER);
              successfulNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
          } else {
            binder.validate();
          }
        });
    return addButton;
  }

  private static Converter<String, UUID> getUuidConverter() {
    return new Converter<>() {
      @Override
      public Result<UUID> convertToModel(String value, ValueContext context) {
        if (isBlank(value)) {
          return Result.ok(null);
        }
        return Result.ok(UUID.fromString(value));
      }

      @Override
      public String convertToPresentation(UUID value, ValueContext context) {
        if (value == null) {
          return null;
        }
        return value.toString();
      }
    };
  }
}
