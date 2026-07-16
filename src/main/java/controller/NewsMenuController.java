package controller;

import models.core.App;
import models.core.News;
import models.core.User;
import models.core.UserDataManager;
import view.terminalView.GameMenuView;

import java.util.List;

public class NewsMenuController implements MenuController{
    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {

    }

    public List<News> showNewsUnread(){
        User user = App.getCurrentUser();
        return user.getUnreadNews();
    }

    public List<News> showNews(){
        User user = App.getCurrentUser();
        return user.getAllNews();
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: news menu");
    }
}
