import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;


public class Player extends Creature
{

    public Player (int type)
    {
	super (type);
	locX = 0;
	locY = 0;
    }


    public void move (int i)
    {
	locX += (i % 2) * 20;
	locY += (i / 2) * 20;

    }




}
