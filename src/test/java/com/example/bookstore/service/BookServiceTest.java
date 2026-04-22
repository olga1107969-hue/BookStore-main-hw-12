package com.example.bookstore.service;

import com.example.bookstore.dto.request.CreateBookRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.exception.DuplicateIsbnException;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Unit Tests")
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private CreateBookRequest testRequest;

    @BeforeEach
    void setUp() {
        testBook = new Book(
                1L,
                "Clean Code",
                "Robert Martin",
                "9780132350884",
                new BigDecimal("45.99"),
                10
        );

        testRequest = new CreateBookRequest(
                "Effective Java",
                "Joshua Bloch",
                "9780134685991",
                new BigDecimal("49.99"),
                5
        );
    }

    // === ТЕСТ 1: Получение всех книг ===
    @Test
    @DisplayName("Получение всех книг (возвращает список)")
    void findAll_ReturnsListOfBooks() {
        // Arrange
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Clean Code");
        verify(bookRepository).findAll();
    }

    // ========== ТЕСТ 2: Получение книги по ID ==========

    @Test
    @DisplayName("Получение книги по существующему ID возвращает книгу")
    void findById_ExistingId_ReturnsBook() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        Book result = bookService.findById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @DisplayName("Получение книги по несуществующему ID выбрасывает исключение")
    void findById_NonExistingId_ThrowsBookNotFoundException() {
        // Arrange
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> bookService.findById(999L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("999");
    }

    // ========== ТЕСТ 3: Создание книги ==========

    @Test
    @DisplayName("Создание книги с уникальным ISBN возвращает книгу")
    void create_UniqueIsbn_ReturnsBook() {
        // Arrange
        when(bookRepository.existsByIsbn(testRequest.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setId(2L);
            return book;
        });

        // Act
        Book result = bookService.create(testRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Effective Java");
        assertThat(result.getIsbn()).isEqualTo("9780134685991");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Создание книги с существующим ISBN выбрасывает исключение")
    void create_DuplicateIsbn_ThrowsDuplicateIsbnException() {
        // Arrange
        when(bookRepository.existsByIsbn(testRequest.getIsbn())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> bookService.create(testRequest))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessageContaining("9780134685991");

        verify(bookRepository, never()).save(any());
    }

    // ========== ТЕСТ 4: Удаление книги ==========

    @Test
    @DisplayName("Удаление существующей книги выполняется успешно")
    void delete_ExistingBook_DeletesSuccessfully() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        doNothing().when(bookRepository).delete(testBook);

        // Act
        bookService.delete(1L);

        // Assert
        verify(bookRepository).delete(testBook);
    }

}
