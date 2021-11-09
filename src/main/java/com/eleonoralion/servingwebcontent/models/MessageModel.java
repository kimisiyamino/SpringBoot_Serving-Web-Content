package com.eleonoralion.servingwebcontent.models;

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
    @Size(min = 4, max = 10, message = "Длинна поля Text длжна быть от 4 до 10 символов")
    private String text;

    @NotBlank(message = "Поле Tag пустое!")
    @Size(min = 4, max = 10, message = "Длинна поля Tag длжна быть от 4 до 10 символов")
    private String tag;

    public MessageModel() {
    }

    public MessageModel(String text, String tag) {
        this.text = text;
        this.tag = tag;
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
