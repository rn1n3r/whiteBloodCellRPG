import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;


public class Enemy extends Creature
{
    
    public Enemy ()
    {
	super(0);
	locX = (int)(Math.random()*30)* 20;
	locY = (int)(Math.random()*30)* 20;
	
	while (locY == 0)//enemy doesn't spawn at player location
	    locY =  (int)(Math.random()*30)* 20;
	
    }
    
    public int getX()
    {
	return locX;
    }
    
    public int getY()
    {
	return locY;
    }
    

}
