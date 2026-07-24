package view.terminalView;

import controller.ProfileMenuController;
import models.enums.commands.ProfileMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class ProfileMenuView implements View {
    private static ProfileMenuView instance;
    private ProfileMenuController controller;
    private String result;

    public ProfileMenuView(ProfileMenuController controller) {
        this.controller = controller;
    }

    public static ProfileMenuView getInstance(ProfileMenuController controller) {
        if (instance == null) {
            instance = new ProfileMenuView(controller);
            return instance;
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {
        boolean commandFound = false;
        for (ProfileMenuCommands profileMenuCommands : ProfileMenuCommands.values()) {
            Matcher matcher = profileMenuCommands.matcher(command);
            if (matcher.matches()) {
                commandFound = true;
                switch (profileMenuCommands) {
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        break;
                    case MENU_EXIT:
                        controller.exitMenu();
                        System.out.println("main menu");
                        break;
                    case MENU_PROFILE_CHANGE_USERNAME:
                        result = controller.changeUsername(matcher.group(1));
                        System.out.println(result);
                        break;
                    case MENU_PROFILE_CHANGE_NICKNAME:
                        result = controller.changeNickname(matcher.group(1));
                        System.out.println(result);
                        break;
                    case MENU_PROFILE_CHANGE_PASSWORD:
                        result = controller.changePassword(matcher.group(2), matcher.group(1));
                        System.out.println(result);
                        break;
                    case MENU_PROFILE_CHANGE_EMAIL:
                        result = controller.changeEmail(matcher.group(1));
                        System.out.println(result);
                        break;
                    case MENU_PROFILE_SHOW_INFO:
                        result = controller.showInfo();
                        System.out.println(result);
                        break;
                }
                break;
            }
        }
        if (!commandFound) {
            System.out.println("invalid command in profile menu");
        }
    }


}
