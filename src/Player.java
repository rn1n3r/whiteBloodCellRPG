
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;

public class Player extends Creature implements ActionListener {

    private int power;//for attack power
    private boolean canMove;

    public Player(int type) {
        super(type);
        locX = 0;
        locY = 0;

        power = 40;
        canMove = true;
    }

    public void move(int i, Enemy[] enemiesOnMap) {
        boolean moving = true;

        for (int j = 0; j < enemiesOnMap.length; j++) {
            if (enemiesOnMap[j].getX() == locX + (i % 2) * 20 && enemiesOnMap[j].getY() == locY + (i / 2) * 20 && enemiesOnMap[j].isAlive()) {
                moving = false;
                attack(enemiesOnMap[j]);
            }
        }

        if (moving && canMove) {
            locX += (i % 2) * 20; //increase the position to move according to controls
            locY += (i / 2) * 20;
            canMove = false;
        }

	//super.move();
	/*//prevents the x values to go out of the screen
         if (locX > 580)
         locX = 580;
         else if (locX < 0)
         locX = 0;
         //prevents the y values to go over the screen
         if (locY > 580)
         locY = 580;
         else if (locY < 0)
         locY = 0;*/
    }

    public void setLocation(int x, int y) {
        locX = x;
        locY = y;
    }

    public void attack(Enemy enemy) {
        enemy.loseHealth(power);
    }

    public void actionPerformed(ActionEvent e) {
        canMove = true;
    }

}
