package com.pedrodovale.bookshelf;

import static com.pedrodovale.bookshelf.BookFormView.VIEW_COMPONENT_ID_AUTHOR_NAME;
import static com.pedrodovale.bookshelf.BookFormView.VIEW_COMPONENT_ID_BOOK_NAME;
import static com.pedrodovale.bookshelf.BookFormView.VIEW_COMPONENT_ID_PUBLICATION_DATE;
import static com.pedrodovale.bookshelf.BookFormView.VIEW_COMPONENT_ID_RATING;
import static com.pedrodovale.bookshelf.BookFormView.VIEW_COMPONENT_ID_READ_DATE;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookshelfIT {

  public static final String BASE_URL = "http://localhost:8080";
  private WebDriver webDriver;
  private JavascriptExecutor javascriptExecutor;

  @BeforeEach
  void setUp() {
    WebDriverManager.chromedriver().setup();
    webDriver = new ChromeDriver();
    javascriptExecutor = (JavascriptExecutor) webDriver;
  }

  @AfterEach
  void tearDown() {
    webDriver.quit();
  }

  @Test
  public void when_addNewBook_then_expect_newBookInGrid() {

    webDriver.get(BASE_URL + "/books");
    waitUntil(titleIs("Bookshelf"));

    WebElement addBookButton = webDriver.findElement(By.tagName("vaadin-button"));
    addBookButton.click();

    waitUntil(presenceOfElementLocated(By.tagName("vaadin-form-layout")));

    WebElement bookName = webDriver.findElement(By.id(VIEW_COMPONENT_ID_BOOK_NAME));
    bookName.sendKeys("A Study In Scarlet");

    WebElement authorName = webDriver.findElement(By.id(VIEW_COMPONENT_ID_AUTHOR_NAME));
    authorName.sendKeys("Arthur Conan Doyle");

    WebElement publicationDate = webDriver.findElement(By.id(VIEW_COMPONENT_ID_PUBLICATION_DATE));
    setElementValue(publicationDate, "1887-11-30");

    WebElement readDate = webDriver.findElement(By.id(VIEW_COMPONENT_ID_READ_DATE));
    setElementValue(readDate, LocalDate.now());

    WebElement rating = webDriver.findElement(By.id(VIEW_COMPONENT_ID_RATING));
    setElementValue(rating, 5);

    WebElement saveBookButton =
        webDriver.findElement(By.xpath("//vaadin-button[@theme='primary']"));
    saveBookButton.click();

    WebElement successNotification =
        waitUntil(presenceOfElementLocated(By.tagName("vaadin-notification")));
    assertThat(successNotification.getDomProperty("text")).isEqualTo("Book saved successfully");

    waitUntil(presenceOfElementLocated(By.tagName("vaadin-grid")));
    assertThat(
            getGridTableCell().stream().filter(cell -> cell.getText().equals("A Study In Scarlet")))
        .hasSize(1);
  }

  private <V> V waitUntil(Function<? super WebDriver, V> expectedCondition) {
    return new WebDriverWait(webDriver, ofSeconds(5), ofSeconds(1)).until(expectedCondition);
  }

  private void setElementValue(WebElement webElement, Object value) {
    javascriptExecutor.executeScript("arguments[0].value='" + value + "';", webElement);
  }

  /* the way to get the cell contents is not pretty because of the shadow content */
  private List<WebElement> getGridTableCell() {
    WebElement grid = webDriver.findElement(By.tagName("vaadin-grid"));

    // JavaScript command to access the shadow root and then the <table> in it
    WebElement table =
        (WebElement)
            javascriptExecutor.executeScript(
                "return arguments[0].shadowRoot.querySelector('table')", grid);
    return table.findElement(By.tagName("tbody")).findElements(By.tagName("td"));
  }
}
