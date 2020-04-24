package remoteserver;

import remoteserver.GUI.AppTray;

public class Main {

    public static void main(String[] args) {
        AppTray app = AppTray.getInstance();
        try {
            app.run();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }
}
