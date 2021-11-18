package com.eleonoralion.servingwebcontent.entity;

import java.time.LocalDateTime;

public class UserMessage {

    Long user_id;
    String username;
    Long message_id;
    String text;
    String tag;
    LocalDateTime date_time;

    public UserMessage() {
    }

    public UserMessage(Long user_id, String username, Long message_id, String text, String tag, LocalDateTime date_time) {
        this.user_id = user_id;
        this.username = username;
        this.message_id = message_id;
        this.text = text;
        this.tag = tag;
        this.date_time = date_time;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDateTime getDate_time() {
        return date_time;
    }

    public void setDate_time(LocalDateTime date_time) {
        this.date_time = date_time;
    }
}
