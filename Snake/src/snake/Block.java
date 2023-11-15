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
import javax.swing.*;

/**
 *
 * @author okan.uregen
 */
public class Block extends JLabel implements MainProperties{

    int bw = 25;
    public int mDir = UP;
    public Block() {
        setBounds(100, 100, bw, bw);

    }

    public Block addBlock() {
        Block B = new Block();
        B.setBounds(this.getX(), this.getY(), bw, bw);
        B.mDir=-this.mDir;
        B.move();
        B.mDir = this.mDir;
        return B;
    }

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g = (Graphics2D) grphcs;
        Rectangle r = new Rectangle(2, 2, getWidth()-4, getHeight()-4);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.draw(r);
        g.setColor(new Color(96, 191, 0));
        g.fill(r);

    }

    public void moveUp() {
        this.setBounds(this.getX(), this.getY() - this.getHeight(), this.getWidth(), this.getHeight());
    }

    public void moveDown() {
        this.setBounds(this.getX(), this.getY() + this.getHeight(), this.getWidth(), this.getHeight());
    }

    public void moveRight() {
        this.setBounds(this.getX() + this.getWidth(), this.getY(), this.getWidth(), this.getHeight());
    }

    public void moveLeft() {
        this.setBounds(this.getX() - this.getWidth(), this.getY(), this.getWidth(), this.getHeight());
    }

    public void move() {
        if (mDir == MainProperties.LEFT) {
            moveLeft();
        }
        if (mDir == MainProperties.RIGHT) {
            moveRight();
        }
        if (mDir == MainProperties.UP) {
            moveUp();
        }
        if (mDir == MainProperties.DOWN) {
            moveDown();
        }
    }

}
