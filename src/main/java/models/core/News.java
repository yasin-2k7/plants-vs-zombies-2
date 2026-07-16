package models.core;

import models.enums.NewsType;

import java.time.LocalDateTime;

public class News {
    private final String title;
    private final String message;
    private final NewsType type;
    private final LocalDateTime timestamp;
    private boolean read;

    public News(String title, String message, NewsType type) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NewsType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }
}