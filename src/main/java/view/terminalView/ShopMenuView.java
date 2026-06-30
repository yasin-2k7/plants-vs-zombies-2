package view.terminalView;

import models.core.App;
import models.core.User;
import models.enums.commands.ShopMenuCommands;
import models.shop.ShopList;
import view.View;

import java.util.regex.Matcher;

public class ShopMenuView implements View {
    private ShopList shopList = new ShopList();

    @Override
    public void processCommand(String command) {
        command = command.trim();

        if (ShopMenuCommands.SHOP_LIST.matcher(command).matches()) {
            showShopList();
        }
        else if (ShopMenuCommands.SHOP_DAILY.matcher(command).matches()) {
            showDailyOffer();
        }
        else if (ShopMenuCommands.SHOP_BUY.matcher(command).matches()) {
            Matcher m = ShopMenuCommands.SHOP_BUY.matcher(command);
            if (m.find()) {
                String itemId = m.group(1);
                int count = Integer.parseInt(m.group(2));
                String plantTypeName = (m.group(3) != null) ? m.group(3) : null;
                buyItem(itemId, count, plantTypeName);
            }
        }
        else if (ShopMenuCommands.MENU_EXIT.matcher(command).matches()) {
            exitToMainMenu();
        }
        else {
            System.out.println("Unknown command in Shop menu.");
        }
    }

    private void showShopList() {
        System.out.println("--- Permanent Items ---");
        shopList.getPermanentItems().forEach(i ->
                System.out.println(i.getName() + ": " + i.getCoinCost() + " coins")
        );
        System.out.println("--- Daily Offer ---");
        if (shopList.getDailyOffer().isAvailableToday()) {
            System.out.println(shopList.getDailyOffer().getName() + " costs " +
                    shopList.getDailyOffer().getCoinCost() + " coins");
        } else {
            System.out.println("No daily offer available today.");
        }
    }

    private void showDailyOffer() {
        if (shopList.getDailyOffer().isAvailableToday()) {
            System.out.println("Daily Offer: " + shopList.getDailyOffer().getName() +
                    " - " + shopList.getDailyOffer().getCoinCost() + " coins");
        } else {
            System.out.println("No daily offer available today.");
        }
    }

    private void buyItem(String itemId, int count, String plantTypeName) {
        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }
        shopList.buy(itemId, null);
    }

    private void exitToMainMenu() {
        System.out.println("Exiting shop. Returning to main menu.");
        // App.setCurrentMenu(new MainMenuController().getView());
    }
}