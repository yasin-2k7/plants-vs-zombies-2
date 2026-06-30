package models.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class UserDataManager {
    private static final String USERS_DIR = "users/";
    private static final String CURRENT_USER_FILE = "users/current_user.txt";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        File dir = new File(USERS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static boolean saveUser(User user){
        if(user == null || user.getUsername() == null) return false;

        File userFile = new File(USERS_DIR + user.getUsername() + ".json");

        try (FileWriter writer = new FileWriter(userFile)) {
            gson.toJson(user, writer);
            return true;
        } catch (IOException e) {
            System.err.println("save error: " + e.getMessage());
            return false;
        }
    }

    public static User loadUser(String username){
        File userFile = new File(USERS_DIR + username + ".json");

        if(!userFile.exists()){
            return null;
        }

            try (FileReader reader = new FileReader(userFile)) {
                User user = gson.fromJson(reader, User.class);
                if (user != null) {
                    user.afterLoad(); // برای اطمینان از اینکه آبجکت‌های داخلی null نیستند
                }
                return user;
            } catch (IOException e) {
                System.err.println("read error: " + e.getMessage());
                return null;
            }
    }

    public static boolean userExists(String username){
        return new File(USERS_DIR + username + ".json").exists();
    }

    public static void saveLoggedInUser(String username){
        try (FileWriter writer = new FileWriter(CURRENT_USER_FILE)) {
            writer.write(username);
        } catch (IOException e) {
            System.err.println("save logged in error: " + e.getMessage());
        }
    }

    public static String getLoggedInUsername(){
        File file = new File(CURRENT_USER_FILE);
        if(!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String username = reader.readLine();
            return (username != null) ? username.trim() : null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void clearLoggedInUser() {
        File file = new File(CURRENT_USER_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
