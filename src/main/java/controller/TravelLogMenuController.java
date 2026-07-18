// فایل: controller/TravelLogMenuController.java
package controller;

import models.core.App;
import models.core.User;
import models.quest.Quest;
import models.quest.QuestPriority;
import models.quest.types.DailyQuest;
import models.quest.types.MainQuest;
import models.quest.types.EpicChallengeQuest;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.MainMenuView;
import view.terminalView.TravelLogMenuView;

import java.util.List;
import java.util.stream.Collectors;

public class TravelLogMenuController implements MenuController {

    private String currentPage = "daily";

    @Override
    public void changeMenu() {
    }

    @Override
    public void exitMenu() {
        AppView.setCurrentScreen(MainMenuView.getInstance());
    }

    public void showCurrentMenu() {
        GameMenuView.getInstance().showResult("Current menu: travel log");
    }

    public String changePage(String pageName) {
        if (pageName.equalsIgnoreCase("daily") || pageName.equalsIgnoreCase("main")
                || pageName.equalsIgnoreCase("epic") || pageName.equalsIgnoreCase("minigame")) {
            this.currentPage = pageName.toLowerCase();
            return "Switched to " + pageName + " page.";
        }
        return "Invalid page name. Available pages: daily, main, epic, minigame.";
    }

    public void displayCurrentPage() {
        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("No user logged in.");
            return;
        }

        List<Quest> quests = user.getQuestManager().getActiveQuests();

        switch (currentPage) {
            case "daily":
                displayQuests(quests, DailyQuest.class);
                break;
            case "main":
                displayQuests(quests, MainQuest.class);
                break;
            case "epic":
                displayQuests(quests, EpicChallengeQuest.class);
                break;
            case "minigame":
                displayMinigames();
                break;
            default:
                System.out.println("Unknown page.");
        }
    }

    private void displayQuests(List<Quest> allQuests, Class<? extends Quest> type) {
        List<Quest> filtered = allQuests.stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("No " + type.getSimpleName() + " quests available.");
            return;
        }

        // مرتب‌سازی بر اساس اولویت (بحرانی > بالا > متوسط > کم)
        filtered.sort((q1, q2) -> q1.getPriority().compareTo(q2.getPriority()));

        System.out.println("===== " + type.getSimpleName() + " Quests =====");
        for (Quest q : filtered) {
            String status = q.isCompleted() ? "[✓ COMPLETED]" : "[✗ IN PROGRESS]";
            String priorityIcon = getPriorityIcon(q.getPriority());
            System.out.println(priorityIcon + " " + q.getDescription() + " " + status);
        }
        System.out.println("================================");
    }

    private String getPriorityIcon(QuestPriority priority) {
        switch (priority) {
            case CRITICAL: return "🔥 CRITICAL";
            case HIGH: return "⭐ HIGH";
            case MEDIUM: return "● MEDIUM";
            case LOW: return "○ LOW";
            default: return "";
        }
    }

    private void displayMinigames() {
        System.out.println("===== Minigames =====");
        System.out.println("1. Beghouled");
        System.out.println("2. Bowling");
        System.out.println("3. Conveyor");
        System.out.println("(Coming soon...)");
        System.out.println("=====================");
    }
}