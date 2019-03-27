package org.remoteserver;

import org.remoteserver.exceptions.RobotException;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;


/**
    This class allows you to emit user input from keyboard and mouse
 */
public class SystemInput {


    private Robot robot = null;

    public enum MouseButton {
        LPM, PPM, SCROLL_WHEEL
    }

    /**
     * Create class Robot instance
     * @throws RobotException if system does not allow to control input or you have not permission
     */
    public SystemInput() throws RobotException  {
        try {
            robot = new Robot();
        }
        catch(AWTException e) {
            throw new RobotException("System does not allow to control input", RobotException.ErrorCode.NOT_ALLOWED);
        }
        catch (SecurityException e) {
            throw new RobotException("You have not permission to control input", RobotException.ErrorCode.NO_PERMISSION);
        }
    }


    /**
     Press and Release Key from keyboard
     @param keyCode Int from KeyEvent (e.g. KeyEvent.VK_A)
     @return true if keyCode is valid, false otherwise
    */
    public boolean keyPress(int keyCode) {
        if (!isRobotInitialized()) return false;

        try {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void mouseClick(MouseButton button) {
        if (!isRobotInitialized()) return;

        int buttonKey;

        if (button == MouseButton.LPM) {
            buttonKey = InputEvent.BUTTON1_DOWN_MASK;
        } else if (button == MouseButton.SCROLL_WHEEL) {
            buttonKey = InputEvent.BUTTON2_DOWN_MASK;
        } else {
            buttonKey = InputEvent.BUTTON3_DOWN_MASK;
        }

        robot.mousePress(buttonKey);
        robot.mouseRelease(buttonKey);
    }

    /**
     * @param button 1 = LPM, 2 = SCROLL_WHEEL, 3 = PPM
     */
    public void mouseClick(int button) {
        if (!isRobotInitialized()) return;

        int buttonKey;
        switch (button) {
            case 1:
                buttonKey = InputEvent.BUTTON1_DOWN_MASK;
                break;
            case 2:
                buttonKey = InputEvent.BUTTON2_DOWN_MASK;
                break;
            case 3:
                buttonKey = InputEvent.BUTTON3_DOWN_MASK;
                break;
            default:
                buttonKey = 0;
        }

        if (buttonKey != 0) {
            robot.mousePress(buttonKey);
            robot.mouseRelease(buttonKey);
        }
    }

    private boolean isRobotInitialized() {
        return robot == null ? false : true;
    }
}
