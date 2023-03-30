package org.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    // Метод dataSource() создает объект DataSource, который представляет собой интерфейс для установки соединения
    // с базой данных и предоставления доступа к данным используется встроенная база данных H2.
    public DataSource dataSource(){
        // объект EmbeddedDatabaseBuilder позволяет создавать встроенную базу данных с настройками по умолчанию.
        return new EmbeddedDatabaseBuilder()
                // имя базы данных не должно быть уникальным
                .generateUniqueName(false)
                // задает имя базы данных
                .setName("book_store")
                // задает тип встроенной базы данных
                .setType(EmbeddedDatabaseType.H2)
                // добавляет скрипты для создания таблиц и наполнения базы данных
                .addDefaultScripts()
                // указывает кодировку скриптов
                .setScriptEncoding("UTF-8")
                // игнорирует ошибки при удалении таблиц
                .ignoreFailedDrops(true)
                // возвращает объект DataSource, который затем будет зарегистрирован в контейнере Spring
                .build();
    }

    @Bean
    // создает объект NamedParameterJdbcTemplate,
    // который облегчает выполнение операций с базой данных с использованием именованных параметров
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        // вызывается для получения объекта DataSource, который передается в конструктор NamedParameterJdbcTemplate
        // возвращает объект NamedParameterJdbcTemplate, который затем будет зарегистрирован в контейнере Spring.
        return new NamedParameterJdbcTemplate(dataSource());
    }
}
