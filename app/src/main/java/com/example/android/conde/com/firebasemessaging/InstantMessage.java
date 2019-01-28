package com.example.android.conde.com.firebasemessaging;

public class InstantMessage {
    private String message;
    private String author;

    public InstantMessage() {
    }

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
