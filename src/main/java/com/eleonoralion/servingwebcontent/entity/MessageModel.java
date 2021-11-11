package com.eleonoralion.servingwebcontent.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Поле Text пустое!")
    @Size(min = 4, max = 200, message = "Длинна поля Text должна быть от 4 до 200 символов")
    private String text;

    @NotBlank(message = "Поле Tag пустое!")
    @Size(min = 1, max = 100, message = "Длинна поля Tag должна быть от 1 до 100 символов")
    private String tag;

    private String author;

    public MessageModel() {
    }

    public MessageModel(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }

    public MessageModel(String text, String tag, String author) {
        this.text = text;
        this.tag = tag;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTag() {
        return tag;
    }
}
