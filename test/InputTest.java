import com.TETOSOFT.graphics.ScreenManager;
import com.TETOSOFT.input.GameAction;
import com.TETOSOFT.input.InputManager;
import java.awt.DisplayMode;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.*;

public class InputTest extends TestCase {
    public void testActionKeys() throws Exception {
        GameAction moveRight = new GameAction("moveRight");
        GameAction moveLeft = new GameAction("moveLeft");
        
        ScreenManager screenManager = new ScreenManager();
        screenManager.setFullScreen(new DisplayMode(800, 600, 32, 0));
        
        InputManager inputManager = new InputManager(screenManager.
            getFullScreenWindow());
        
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        
        Robot robot = new Robot();
        
        assertTrue("Failed to detect action '" + moveRight.getName() + "'",
            keyPress(robot, KeyEvent.VK_RIGHT, moveRight) == true);
        Thread.sleep(100);
        assertTrue("Failed to detect action '" + moveLeft.getName() + "'",
            keyPress(robot, KeyEvent.VK_LEFT, moveLeft) == true);
    }
    
    private boolean keyPress(Robot robot, int keycode, GameAction action) throws InterruptedException {
        new Thread(() -> {
            robot.keyPress(keycode);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(InputTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            robot.keyRelease(keycode);
        }).start();
        Thread.sleep(100);
        return action.isPressed();
    }
}
