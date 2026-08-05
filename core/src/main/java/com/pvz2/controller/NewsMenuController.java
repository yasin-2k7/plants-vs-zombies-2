package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.core.News;
import com.pvz2.models.core.User;

import java.util.List;

public class NewsMenuController implements MenuController {
    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {
        //needs edit
//        AppView.setCurrentScreen(MainMenuView.getInstance());
    }

    public void showNewsUnread() {
        User user = App.getCurrentUser();
        List<News> unreadNews = user.getUnreadNews();
        for (News news : unreadNews) {
            //needs edit
//            NewsMenuView.getInstance().showResult(news.getTitle());
//            NewsMenuView.getInstance().showResult("------------------------------------------------\n");
//            NewsMenuView.getInstance().showResult(news.getMessage());
            news.markAsRead();
        }
    }

    public void showNews() {
        User user = App.getCurrentUser();
        List<News> allNews = user.getAllNews();
        for (News news : allNews) {
            //needs edit
//            NewsMenuView.getInstance().showResult(news.getTitle());
//            NewsMenuView.getInstance().showResult("------------------------------------------------\n");
//            NewsMenuView.getInstance().showResult(news.getMessage());
            news.markAsRead();
        }
    }

    public void showCurrentMenu() {
        //needs edit
//        GameMenuView.getInstance().showResult("Current menu: news menu");
    }
}
