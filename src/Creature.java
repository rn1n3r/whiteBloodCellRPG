
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;

public class Creature { //used for everything that moves including player and npc

    protected BufferedImage sprite; 
    protected int hp, speed, locX, locY;

    public Creature(int type) { //"type" refers to the name of the image used for the sprite
        hp = 100;
        speed = 10;
        locX = locY = 0; //sets sprite in top left corner of the screen (600 x 600)
        try {
            sprite = ImageIO.read(Creature.class.getResource(type + ".png"));
        } catch (IOException e) {
        }
    }

    public int getX() { //gets the creature's x coordinate in pixels
        return locX;
    }

    public int getY() { //gets the creature's y coordinate in pixels
        return locY;
    }

    public void show(Graphics g) { //draws image of stuff
        g.drawImage(sprite, locX, locY, null);
    }
    public void setLocation(int x, int y) { //sets custom location of sprite
                                            //according to the top left hand corner
                                            //of the 40x40 png image
        locX = x;
        locY = y;
    }

    public void move() {
	    //prevents the x values to go out of the screen

        if (locX > 580) {
            locX = 580;
        }
        if (locX < 0) {
            locX = 0;
        }
        //prevents the y values to go over the screen
        if (locY > 580) {
            locY = 580;
        } else if (locY < 0) {
            locY = 0;
        }
    }
            
    public void collisionCheck(Creature creature){
        if (this.getX() == creature.getX()|| this.getY() == creature.getY()){
            //move the player away?
            this.setLocation(this.getX()-40, this.getY()-40);
        }
    }

}
