package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.miniGame.MiniGameFactory;
import com.pvz2.models.miniGame.MiniGameLevels;
import com.pvz2.models.miniGame.MiniGames;
import com.pvz2.models.quest.Quest;
import com.pvz2.models.quest.QuestPriority;
import com.pvz2.models.quest.types.DailyQuest;
import com.pvz2.models.quest.types.EpicChallengeQuest;
import com.pvz2.models.quest.types.MainQuest;
import com.pvz2.models.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TravelLogMenuController implements MenuController {

    private String currentPage = "daily";

    @Override
    public void changeMenu() {
    }

    @Override
    public void exitMenu() {
        //needs edit
//        AppView.setCurrentScreen(MainMenuView.getInstance());
    }

    public void showCurrentMenu() {
        //needs edit
//        GameMenuView.getInstance().showResult("Current menu: travel log");
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
            //needs edit
//            GameMenuView.getInstance().showResult("No user logged in.");
            return;
        }

        List<Quest> allQuests = new ArrayList<>();
        allQuests.addAll(user.getQuestManager().getActiveQuests());
        allQuests.addAll(user.getQuestManager().getCompletedQuests());

        switch (currentPage) {
            case "daily":
                displayQuests(allQuests, DailyQuest.class);
                break;
            case "main":
                displayQuests(allQuests, MainQuest.class);
                break;
            case "epic":
                displayQuests(allQuests, EpicChallengeQuest.class);
                break;
            case "minigame":
                displayMinigames();
                break;
            default:
                //needs edit
//                GameMenuView.getInstance().showResult("Unknown page.");
        }
    }

    private void displayQuests(List<Quest> allQuests, Class<? extends Quest> type) {
        List<Quest> filtered = allQuests.stream()
                .filter(type::isInstance)
                .sorted((q1, q2) -> q1.getPriority().compareTo(q2.getPriority()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            //needs edit
//            GameMenuView.getInstance().showResult("No " + type.getSimpleName() + " quests available.");
            return;
        }

        //needs edit
//        GameMenuView.getInstance().showResult("===== " + type.getSimpleName() + " Quests =====");
        for (Quest q : filtered) {
            String status = q.isCompleted() ? "[✓ COMPLETED]" : "[✗ IN PROGRESS]";
            String priorityIcon = getPriorityIcon(q.getPriority());
            if (q.isCompleted()) {
                //needs edit
//                GameMenuView.getInstance().showResult("✅ " + q.getDescription() + " " + status);
            } else {
                //needs edit
//                GameMenuView.getInstance().showResult(priorityIcon + " " + q.getDescription() + " " + status);
            }
        }
        //needs edit
//        GameMenuView.getInstance().showResult("================================");
    }

    private String getPriorityIcon(QuestPriority priority) {
        switch (priority) {
            case CRITICAL:
                return "🔥 CRITICAL";
            case HIGH:
                return "⭐ HIGH";
            case MEDIUM:
                return "● MEDIUM";
            case LOW:
                return "○ LOW";
            default:
                return "";
        }
    }

    //needs edit
    private void displayMinigames() {
//        GameMenuView.getInstance().showResult("===== Minigames =====");
//        GameMenuView.getInstance().showResult("1. Beghouled");
//        GameMenuView.getInstance().showResult("2. Bowling");
//        GameMenuView.getInstance().showResult("3. Vase Breaker");
//        GameMenuView.getInstance().showResult("4. IZombie");
//        GameMenuView.getInstance().showResult("4. Zombotany");
//        GameMenuView.getInstance().showResult("=====================");
    }

    public void selectMinigame(String minigameName, int level) {
        MiniGames selected = parseMinigameName(minigameName);
        if (selected == null) {
            //needs edit
//            GameMenuView.getInstance().showResult("Invalid minigame name. Available: beghouled, bowling, vasebreaker," +
//                    " izombie, zombotany.");
            return;
        }

        boolean isUnlocked = false;
        if (level != 1){
            for (MiniGameLevels miniGameLevels : App.getCurrentUser().getMiniGameLevels()){
                if (miniGameLevels.miniGame == selected && miniGameLevels.level == level-1) isUnlocked = true;
                break;
            }
        }
        else {
            isUnlocked = true;
        }
        if (!isUnlocked){
//            GameMenuView.getInstance().showResult("this mini game is locked!");
            return;
        }

        try {
            GameWorld world = MiniGameFactory.createMiniGameLevel(selected, level);
            App.setCurrentGame(world);

            //needs edit
            if (world.getLevelSetup().requirePlantSelection()) {
//                AppView.setCurrentScreen(PlantMenuView.getInstance());
//                GameMenuView.getInstance().showResult(
//                        "Select your plants for " + selected.name() + " - Level " + level + "!");
            } else {
//                AppView.setCurrentScreen(GameMenuView.getInstance());
//                GameMenuView.getInstance().showResult("Starting " + selected.name() + " - Level " + level + "!");
            }
        } catch (IllegalArgumentException e) {
//            GameMenuView.getInstance().showResult(e.getMessage());
        }
    }

    private MiniGames parseMinigameName(String name) {
        return switch (name.toLowerCase()) {
            case "beghouled" -> MiniGames.BEGHOULED;
            case "bowling" -> MiniGames.BOWLING;
            case "vasebreaker", "vase_breaker" -> MiniGames.VASE_BREAKER;
            case "izombie", "i_zombie" -> MiniGames.I_ZOMBIE;
            case "zombotany" -> MiniGames.ZOMBOTANY;
            default -> null;
        };
    }
}
