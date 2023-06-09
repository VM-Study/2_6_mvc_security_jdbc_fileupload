package org.example.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // определяем репозиторий
    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    // возвращаем список всех книг из репозитория
    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }


    // сохранение книги в репозиторий
    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(int bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }
}
