package org.example.app.services;

import org.example.web.dto.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// к BookRepository добавляем имплементацию ApplicationContextAware для доступа к applicationContext
@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);
    private ApplicationContext context;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    // возвращаем копию листа всех книг
    public List<Book> retrieveAll() {
        logger.info("list all books");
        String sql = "SELECT id, author, title, size FROM books";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
    }

    @Override
    public void store(Book book) {
        // для передачи параметров используется объект MapSqlParameterSource,
        // который устанавливает значения параметров запроса с помощью метода addValue().
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());

        // Для выполнения операции записи в базу данных используется метод update объекта jdbcTemplate.
        // В качестве аргументов методу передаются SQL-запрос и объект типа SqlParameterSource,
        // который используется для передачи параметров запроса.
        // значения передаются в виде ":переменная"
        jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)", parameterSource);
        logger.info("store new book: {}", book);
    }

    @Override
    public boolean removeItemById(int bookIdToRemove) {
        // для передачи параметра идентификатора книги используется объект MapSqlParameterSource,
        // который устанавливает значение параметра запроса с помощью метода addValue().
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);

        // Для выполнения операции удаления из базы данных используется метод update объекта jdbcTemplate.
        // В качестве аргументов методу передаются SQL-запрос и объект типа SqlParameterSource,
        // который используется для передачи параметров запроса.
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
        logger.info("remove book completed");
        return true;
    }

    // для implements ApplicationContextAware
    // получаем applicationContext
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
