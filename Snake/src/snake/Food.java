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
import java.awt.geom.Ellipse2D;
import javax.swing.*;

/**
 *
 * @author okan.uregen
 */
public class Food extends JLabel implements MainProperties {

    public Food() {
        setBounds(350, 350, 15, 15);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g = (Graphics2D) grphcs;
        Ellipse2D e = new Ellipse2D.Double(1, 1, 13, 13);
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        g.draw(e);
        g.setColor(Color.RED);
        g.fill(e);

    }

    public void setPos(int x, int y) {
        setBounds(x, y, 15, 15);
    }

}
