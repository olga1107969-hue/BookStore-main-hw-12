package com.example.bookstore.service;

import com.example.bookstore.entity.Author; // Проверь этот импорт!
import com.example.bookstore.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void testFindById() {
        Author author = new Author(1L, "George Orwell");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author result = authorService.findById(1L);
        assertEquals("George Orwell", result.getName());
    }
}
