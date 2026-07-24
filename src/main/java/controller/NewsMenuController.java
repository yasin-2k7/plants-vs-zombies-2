package controller;

import models.core.App;
import models.core.News;
import models.core.User;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.MainMenuView;
import view.terminalView.NewsMenuView;

import java.util.List;

public class NewsMenuController implements MenuController {
    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {
        AppView.setCurrentScreen(MainMenuView.getInstance());
    }

    public void showNewsUnread() {
        User user = App.getCurrentUser();
        List<News> unreadNews = user.getUnreadNews();
        for (News news : unreadNews) {
            NewsMenuView.getInstance().showResult(news.getTitle());
            NewsMenuView.getInstance().showResult("------------------------------------------------\n");
            NewsMenuView.getInstance().showResult(news.getMessage());
            news.markAsRead();
        }
    }

    public void showNews() {
        User user = App.getCurrentUser();
        List<News> allNews = user.getAllNews();
        for (News news : allNews) {
            NewsMenuView.getInstance().showResult(news.getTitle());
            NewsMenuView.getInstance().showResult("------------------------------------------------\n");
            NewsMenuView.getInstance().showResult(news.getMessage());
            news.markAsRead();
        }
    }

    public void showCurrentMenu() {
        GameMenuView.getInstance().showResult("Current menu: news menu");
    }
}
