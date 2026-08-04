package com.pvz2.view.terminalView;

import controller.ShopMenuController;
import com.pvz2.models.enums.commands.ShopMenuCommands;
import view.View;

import java.util.List;
import java.util.regex.Matcher;

public class ShopMenuView implements View {
    private static ShopMenuView instance;
    private ShopMenuController controller;

    public ShopMenuView(ShopMenuController controller) {
        this.controller = controller;
    }

    public static ShopMenuView getInstance() {
        if (instance == null) {
            instance = new ShopMenuView(new ShopMenuController());
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {
        command = command.trim();

        for (ShopMenuCommands shopMenuCommand : ShopMenuCommands.values()) {
            Matcher matcher = shopMenuCommand.matcher(command);
            if (matcher.matches()) {
                switch (shopMenuCommand) {
                    case SHOP_LIST:
                        List<String> list = controller.showShopList();
                        for (String s : list) {
                            System.out.println(s);
                        }
                        return;
                    case SHOP_DAILY:
                        System.out.println(controller.showDailyOffer());
                        return;
                    case SHOP_BUY:
                        String itemId = matcher.group(1);
                        int count = Integer.parseInt(matcher.group(2));
                        String plantTypeName =
                                (matcher.groupCount() >= 3 && matcher.group(3) != null) ? matcher.group(3) : null;
                        System.out.println(controller.buyItem(itemId, count, plantTypeName));
                        return;
                    case MENU_EXIT:
                        controller.exitMenu();
                        System.out.println("Exiting shop. Returning to greenhouse menu.");
                        return;
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        return;
                    default:
                        break;
                }
            }
        }
        System.out.println("Unknown command in Shop menu.");
    }


}
