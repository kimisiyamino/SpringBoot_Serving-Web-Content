package com.eleonoralion.servingwebcontent.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Поле Text пустое!")
    @Size(min = 4, max = 200, message = "Длинна поля Text должна быть от 4 до 200 символов")
    @Column
    private String text;

    @NotBlank(message = "Поле Tag пустое!")
    @Size(min = 1, max = 100, message = "Длинна поля Tag должна быть от 1 до 100 символов")
    @Column
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @Column
    @DateTimeFormat(pattern="HH:mm:ss dd-MM-yyyy")
    private LocalDateTime dateTime;

    public Message() {
    }

    public Message(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }

    public Message(String text, String tag, User author, LocalDateTime dateTime) {
        this.text = text;
        this.tag = tag;
        this.author = author;
        this.dateTime = dateTime;
    }

    public String getUsername(){
        if(author == null){
            return "<deleted user>";
        }
        return author.getUsername();
    }

    public String getFormatDateTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public User getAuthor() {
        if (author == null){
            return new User("<deleted user>");
        }
        return author;
    }

    public void setAuthor(User author) {
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

    @Override
    public String toString() {
        return "MessageModel{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", tag='" + tag + '\'' +
                ", author=" + author +
                ", dateTime=" + dateTime +
                '}';
    }
}
