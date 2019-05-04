package org.remoteserver;

import javax.swing.*;
import java.awt.*;

public final class AppTray {

    private static final SystemTray tray = SystemTray.getSystemTray();
    private static final String imagePath = "tray-icon.png";
    private static TrayIcon trayIcon;
    private static boolean isRunning = false;

    private AppTray() {}

    public static void run() {
        if (isRunning) return;
        isRunning = true;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(
                    null, "Your system does not support system tray feature", "Program stopped", JOptionPane.ERROR_MESSAGE);
            System.exit(2);
        }

        PopupMenu menu = createMenu();
        if (menu == null) System.exit(2);
        initTray(menu);
    }

    private static PopupMenu createMenu() {
        try {
            PopupMenu popupMenu = new PopupMenu();
            MenuItem[] menuItems = createMenuItems();
            for (MenuItem item : menuItems) {
                popupMenu.add(item);
            }
            return popupMenu;
        } catch (Exception e) {
            return null;
        }
    }

    private static MenuItem[] createMenuItems() {
        // Menu Items
        MenuItem close = new MenuItem("Close");
        MenuItem about = new MenuItem("About");
        MenuItem connectedDevices = new MenuItem("Connected devices");

        // Event binding
        close.addActionListener(e -> System.exit(1));
        about.addActionListener(e -> JOptionPane.showMessageDialog(
                    null, "Remote Control App\nCreated by Arkadiusz Siekiera", "About", JOptionPane.INFORMATION_MESSAGE)
        );

        // TODO: implement app window with connected devices
        connectedDevices.addActionListener(e -> JOptionPane.showMessageDialog(null, "Not implemented yet"));

        // the order matters
        MenuItem[] items = { connectedDevices, about, close };
        return  items;
    }

    private static void initTray(PopupMenu menu) {
        Image image = null;
        try {
            image = Toolkit.getDefaultToolkit().getImage(imagePath);
        } catch (Exception e) {}

        trayIcon = new TrayIcon(image, "Remote Control", menu);
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (Exception e) {
            System.exit(2);
        }
        // TODO: implement app window with connected devices
        trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null, "Not implemented yet"));
    }

}
