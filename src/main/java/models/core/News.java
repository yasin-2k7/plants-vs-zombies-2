package models.core;

import models.enums.NewsType;



public class News {
    private final String title;
    private final String message;
    private final NewsType type;

    private boolean read;

    public News(String title, String message, NewsType type) {
        this.title = title;
        this.message = message;
        this.type = type;

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



    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }
}