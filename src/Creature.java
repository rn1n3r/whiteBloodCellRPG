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
	}
    }
    
    public int getX()
    {
	return locX;
    }
    
    public int getY()
    {
	return locY;
    }
    public void show(Graphics g){
	g.drawImage(sprite, locX, locY, null);
    }
    
    public void move(){
	    //prevents the x values to go out of the screen
        
	if (locX > 580)
	    locX = 580;
	if (locX < 0)
	    locX = 0;
	//prevents the y values to go over the screen
	if (locY > 580)
	    locY = 580;
	else if (locY < 0)
	    locY = 0;
      
    }
}
