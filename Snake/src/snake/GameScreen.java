/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author okan.uregen
 */
public class GameScreen extends JLabel implements KeyListener, ActionListener, MainProperties {

    
    GameStateManager mf;
    private static ArrayList<Integer> AllScores = new ArrayList<>();
    File scores = new File("scores.txt");
    private Timer timer = null;
    private final Block head;
    private int Score = 0;
    protected static int tempScore = 0;
    
    private int h1 = 0, h2 = 0, h3 = 0;
    boolean crash = false;

    private Food f;
    private static ArrayList<Block> blocks = new ArrayList<>();

    public GameScreen(GameStateManager mf) {
        this.mf = mf;
        readFromFile();
        readFromList();

        setFocusable(true);
        addKeyListener(this);
        head = new Block();

        blocks.add(head);
        add(head);

        f = new Food();
        add(f);

        timer = new Timer(125, this);
        timer.start();

    }

    public static ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void addNewBlock() {
        Block B = blocks.get(blocks.size() - 1).addBlock();
        blocks.add(B);
        add(B);
        if (timer.getDelay() > 30) {
            if (blocks.size() > 50) {
                timer.setDelay(30);
            } else if (blocks.size() >= 40) {
                timer.setDelay(timer.getDelay() - 4);
            } else if (blocks.size() >= 20) {
                timer.setDelay(timer.getDelay() - 3);
            } else if (blocks.size() >= 5) {
                timer.setDelay(timer.getDelay() - 2);
            } else {
                timer.setDelay(timer.getDelay() - 1);
            }
        }

        Score++;
    }

    public boolean crash() {

        for (int i = 3; i < blocks.size(); i++) {
            if (head.getX() == blocks.get(i).getX() && head.getY() == blocks.get(i).getY()) {
                crash = true;
                tempScore = Score;
                break;
            }
        }
        return crash;
    }

    @Override
    public void paint(Graphics p) {

        super.paint(p);
        Graphics2D g = (Graphics2D) p;
        Rectangle rect = new Rectangle(5, 5, getWidth() - 10, getHeight() - 10);

        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(10));
        g.draw(rect);

        //SCORE TABLE
        Rectangle score = new Rectangle(650, 50, 90, 120);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.draw(score);
        g.drawString("Score: " + Score * 10, 658, 70);
        g.drawString("HIGH SCORES", 658, 98);
        g.drawString("1. " + h1, 658, 120);
        g.drawString("2. " + h2, 658, 138);
        g.drawString("3. " + h3, 658, 156);
        repaint();

    }

    @Override
    public void keyPressed(KeyEvent ke) {

        if (ke.getKeyCode() == KeyEvent.VK_UP && head.mDir != MainProperties.DOWN) {
            head.mDir = MainProperties.UP;
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN && head.mDir != MainProperties.UP) {
            head.mDir = MainProperties.DOWN;
        }
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT && head.mDir != MainProperties.LEFT) {
            head.mDir = MainProperties.RIGHT;
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT && head.mDir != MainProperties.RIGHT) {
            head.mDir = MainProperties.LEFT;
        }

    }

    public void jump(Block b) {

        if (b.getX() <= 0 && b.mDir == MainProperties.LEFT) {
            b.setBounds(this.getWidth() + 6, b.getY(), b.bw, b.bw); //+6 is for the same coefficient value with food
        }

        if (b.getX() + b.getWidth() >= getWidth() + 6 && b.mDir == MainProperties.RIGHT) {
            b.setBounds(-25, b.getY(), b.bw, b.bw);
        }
        if (b.getY() <= 0 && b.mDir == MainProperties.UP) {
            b.setBounds(b.getX(), this.getHeight() + 10, b.bw, b.bw);
        }
        if (b.getY() >= this.getHeight() && b.mDir == MainProperties.DOWN) {
            b.setBounds(b.getX(), -25, b.bw, b.bw);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        for (int i = 0; i < blocks.size(); i++) {
            jump(blocks.get(i));
        }

        if (!crash()) {
            for (int i = 0; i < blocks.size(); i++) {
                blocks.get(i).move();

            }

            for (int i = blocks.size() - 1; i > 0; i--) {
                blocks.get(i).mDir = blocks.get(i - 1).mDir;

            }
        } else {
            JOptionPane.showMessageDialog(null, "SCORE: " + Score * 10, "GAME OVER", -1);
            timer.stop();
            if (Score * 10 >= h1) {
                h3 = h2;
                h2 = h1;
                h1 = Score * 10;
            } else if (Score * 10 >= h2) {
                h3 = h2;
                h2 = Score * 10;
            } else if (Score * 10 >= h3) {
                h3 = Score * 10;
            }

            writeToFile();
            blocks.clear();
            mf.changeScreen(GAME_OVER);

        }

        if (head.getX() == f.getX() && head.getY() == f.getY()) {
            addNewBlock();
            addFood();

        }

    }

    public void addFood() {
        boolean check = true;
        int ranX = 0;
        int ranY = 0;
        while (check) {
            check = false;
            ranX = (((int) (Math.random() * (int) ((getWidth() / 25) - 1))) + 1) * 25;
            ranY = (((int) (Math.random() * (int) ((getHeight() / 25) - 1))) + 1) * 25;

            for (int i = 0; i < blocks.size(); i++) {
                if (ranX == blocks.get(i).getX() && ranY == blocks.get(i).getY()) {
                    check = true;
                    break;
                }
            }
        }
        f.setPos(ranX, ranY);
    }

    public static void readFromFile() {
        File f = new File("scores.txt");
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            AllScores = (ArrayList<Integer>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Cannot read from the specified file");
        } catch (ClassNotFoundException ex) {
            System.out.println("the class doesnt exist");
        }

    }

    public void writeToFile() {
        File f = new File("scores.txt");
        AllScores.removeAll(AllScores);

        AllScores.add(h1);
        AllScores.add(h2);
        AllScores.add(h3);

        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AllScores); //write the object
            oos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Cannot write into the specified file");
        }

    }

    public void readFromList() {

        for (int i = 0; i < AllScores.size(); i++) {
            if (AllScores.get(i) >= h1) {
                h1 = AllScores.get(i);
            } else if (AllScores.get(i) >= h2) {
                h2 = AllScores.get(i);
            } else if (AllScores.get(i) >= h3) {
                h3 = AllScores.get(i);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

}
