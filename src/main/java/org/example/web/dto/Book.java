package org.example.web.dto;

import jakarta.validation.constraints.Digits;

public class Book {
    private int id;
    private String author;
    private String title;

    // @Digits проверка что числовое поле или параметр метода имеет указанное количество цифр в целой и/или дробной части.
    @Digits(integer = 4, fraction = 0)
    private Integer size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }
}
