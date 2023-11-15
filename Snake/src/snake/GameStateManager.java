/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 *
 * @author okan.uregen
 */
public class GameStateManager extends JFrame implements MainProperties, KeyListener {

    protected static int currentScreen = -1; //in consttructor, specify the state as mainscreen
    private int mWidth = S_WIDTH;
    private int mHeight = S_HEIGHT;
    private static boolean check = true; //for the just one frame
    MainMenu ms;
    GameScreen gs;
    GameOver go;

    private GameStateManager() {
        setFocusable(true);
        addKeyListener(this);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDimension();
        setResizable(false);
        changeScreen(MAIN_MENU);

    }

    public void changeScreen(int x) {
        if (x != currentScreen) {
            switch (x) {
                case MAIN_MENU:
                    removeScreen();
                    ms = new MainMenu(this);
                    add(ms);
                    validate();
                    currentScreen = MAIN_MENU;
                    break;
                case GAME_SCREEN:
                    removeScreen();
                    gs = new GameScreen(this);
                    add(gs);
                    validate();
                    currentScreen = GAME_SCREEN;
                    break;
                case GAME_OVER:
                    removeScreen();
                    go = new GameOver(this);
                    add(go);
                    validate();
                    currentScreen = GAME_OVER;
                    break;
            }
        }
    }

    public void removeScreen() {
        switch (currentScreen) {
            case MAIN_MENU:
                this.remove(ms);
                break;
            case GAME_SCREEN:
                this.remove(gs);
                break;
            case GAME_OVER:
                this.remove(go);
                break;
        }
        repaint();
    }

    public int getmWidth() {
        return mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public static GameStateManager getMain() { // for the just one frame 

        if (check) {
            check = false;
            return new GameStateManager();
        } else {
            return null;
        }

    }

    public void setDimension() { //to get middle of the window
        Dimension dm = Toolkit.getDefaultToolkit().getScreenSize(); //keeps size of window

        int x = (dm.width - mWidth) / 2;
        int y = (dm.height - mHeight) / 2;

        this.setBounds(x, y, mWidth, mHeight);

    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
      if(currentScreen==GAME_SCREEN){
          gs.keyPressed(ke);
      }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

}
