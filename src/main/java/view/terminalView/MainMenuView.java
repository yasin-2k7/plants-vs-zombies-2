package view.terminalView;

import view.View;

public class MainMenuView implements View{
    @Override
    public void processCommand(String command) {
        command = command.trim();

        for (MainMenuCommands cmd : MainMenuCommands.values()) {
            Matcher matcher = cmd.matcher(command);
            if (matcher.matches()) {
                switch (cmd) {
                    case MENU_ENTER:
                        String menuName = matcher.group(1);
                        System.out.println(controller.enterMenu(menuName));
                        return;
                    case MENU_SHOW_CURRENT:
                        System.out.println(controller.showCurrentMenu());
                        return;
                    case MENU_LOGOUT:
                        System.out.println(controller.logout());
                        return;
                    default:
                        break;
                }
            }
        }
        System.out.println("Unknown command in Main menu.");
    }
}
