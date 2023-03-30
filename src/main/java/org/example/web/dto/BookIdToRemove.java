package org.example.web.dto;

import jakarta.validation.constraints.NotEmpty;

// получает только id от как у объекта Book
public class BookIdToRemove {

    // @NotEmpty Аннотация используется для проверки, что поле или параметр метода не null
    // и имеет длину больше нуля (не пусто)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
