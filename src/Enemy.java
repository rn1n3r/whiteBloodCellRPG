import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;


public class Enemy extends Creature implements ActionListener
{
    protected int defense;// multiplier for defence
    protected boolean dead;
    
    public Enemy ()
    {
	super(0);
	locX = (int)(Math.random()*30)* 20;
	locY = (int)(Math.random()*30)* 20;
	defense = 0;
	dead = false;
	
	while (locY == 0)//enemy doesn't spawn at player location
	    locY =  (int)(Math.random()*30)* 20;
    }
    
    public void move(){//doesn't prevent overlap of enemies
	int moving = ((int)(Math.random()*3) - 1) * 20;
	if(moving == 0)
	    locY += ((int)(Math.random()*3) - 1) * 20;
	else
	    locX += moving;
	
	super.move();
    }
    
    public int getX()
    {
	return locX;
    }
    
    public int getY()
    {
	return locY;
    }
    
    public boolean isAlive(){
	return !dead;//check if enemy is alive
    }
    
    //damages enemy
    public void loseHealth(int power){
	int damage = power - defense;
	if(damage < 1) //make base damage equal to 1
	    damage = 1;
	
	hp -= damage; //loses hp
	
	if(hp < 0){//killed
	    dead = true;
	    hp = 0;
	}
    }
    
    public void actionPerformed (ActionEvent e){
	this.move();
    }
}
