import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;


public class Creature
{
    protected BufferedImage sprite;
    protected int hp, speed, locX, locY;
    
    public Creature (int type)
    {
	hp = 100;
	speed = 10;
	locX = locY = 0;
	try
	{
	    sprite = ImageIO.read (Creature.class.getResource(type + ".png"));
	}
	catch (IOException e)
	{
            System.out.print("Something wrong");
	}
    }
    

    public void show(Graphics g){
	g.drawImage(sprite, locX, locY, null);
    }
}
