package com.example.bookstore.service;

import com.example.bookstore.entity.Author;
import com.example.bookstore.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow();
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
