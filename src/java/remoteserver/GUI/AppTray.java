package remoteserver.GUI;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public final class AppTray {

    private final SystemTray tray;

    private final String imagePath;

    private final String tooltip;

    private TrayIcon trayIcon;

    private static AppTray appTray;

    private AppTray() {
        imagePath = System.getProperty("user.dir") + "\\src\\resources\\tray-icon.png";
        tray = SystemTray.getSystemTray();
        tooltip = "Remote Control";
    }

    public static AppTray getInstance() {
        if (appTray == null) {
            appTray = new AppTray();
        }

        return appTray;
    }

    public void run() throws Exception {
        if (GraphicsEnvironment.isHeadless() || !SystemTray.isSupported()) {
            throw new Exception();
        }

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        PopupMenu menu = createMenu();
        initTray(menu);
    }

    private PopupMenu createMenu() {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem[] items = createMenuItems();
        for (MenuItem item : items) {
            popupMenu.add(item);
        }
        return popupMenu;
    }

    private MenuItem[] createMenuItems() {
        MenuItem close = new MenuItem("Close");
        MenuItem about = new MenuItem("About");
        MenuItem connectedDevices = new MenuItem("Connected devices");

        // TODO: change event listeners
        close.addActionListener(e -> System.exit(1));
        about.addActionListener(e -> JOptionPane.showMessageDialog(
            null,
            "Remote Control App\nCreated by Arkadiusz Siekiera",
            "About", JOptionPane.INFORMATION_MESSAGE)
        );

        // the order matters when we display items
        return new MenuItem[]{connectedDevices, about, close};
    }

    private void initTray(PopupMenu menu) throws Exception {
        Image image = Toolkit.getDefaultToolkit().getImage(imagePath);
        trayIcon = new TrayIcon(image, tooltip, menu);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);

        // TODO: implement app window with connected devices
        trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null, "Not implemented yet"));
    }

}
