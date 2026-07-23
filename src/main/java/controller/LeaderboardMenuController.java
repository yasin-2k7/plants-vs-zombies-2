package controller;

import models.core.User;
import models.core.UserDataManager;
import models.enums.LeaderboardSortField;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.LeaderboardMenuView;
import view.terminalView.MainMenuView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardMenuController implements MenuController{
    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {
        AppView.setCurrentScreen(MainMenuView.getInstance());
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: leaderboard menu");
    }

    public List<User> getSortedLeaderboard(List<User> allUsers, LeaderboardSortField field, boolean ascending) {
        List<User> sortedList = new ArrayList<>(allUsers);
        Comparator<User> comparator = field.getComparator();

        if (!ascending) {
            comparator = comparator.reversed();
        }

        comparator = comparator.thenComparing(User::getUsername);

        sortedList.sort(comparator);

        return sortedList;
    }

    public void showList(String field, boolean ascending){
        LeaderboardSortField sortField = null;
        for (LeaderboardSortField leaderboardSortField : LeaderboardSortField.values()){
            if (field.equalsIgnoreCase(leaderboardSortField.name())){
                sortField = leaderboardSortField;
                break;
            }
        }
        if (sortField == null){
            LeaderboardMenuView.getInstance().showResult("invalid field!");
            return;
        }

        List<User> sortedUsers = getSortedLeaderboard(UserDataManager.loadAllUsers(), sortField, ascending);
        LeaderboardMenuView.getInstance().showResult("===============================================================================================\n" +
                "                                \uD83C\uDFC6 LEADERBOARD \uD83C\uDFC6\n" +
                "===============================================================================================\n" +
                "| Rank | Username       | Last Stage         | Mini-Games | Daily Q. | Normal Q. | High Score |\n" +
                "+------+----------------+--------------------+------------+----------+-----------+------------+");

        int i = 1;
        for (User user : sortedUsers){
            String row = String.format("| %-4d | %-14s | %-17s | %-10d | %-8d | %-9d | %-10d |",
                    i++, user.getUsername(), "SEASON " + user.getUnlockedChapter() + " - " + "LEVEL " + user.getUnlockedLevel(), user.getMiniGameLevels().size(), user.getDailyQuestsCount(), user.getNormalQuestsCount(), user.getMaxMupoint());
            LeaderboardMenuView.getInstance().showResult(row);
        }

        LeaderboardMenuView.getInstance().showResult("===============================================================================================\n" +
                "* Sorted by: " + sortField.name() + (ascending? " (Ascending)" : " (Descending)"));
    }
}
