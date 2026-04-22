package com.example.bookstore.service;

import com.example.bookstore.dto.request.CreateBookRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.exception.DuplicateIsbnException;
import com.example.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID " + id + " не найдена"));
    }

    @Transactional
    public Book create(CreateBookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateIsbnException("Книга с ISBM " + request.getIsbn() + " уже существует");
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());

        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Long id, CreateBookRequest request) {
        // TODO
        return null;
    }

    @Transactional
    public void delete(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }
}
