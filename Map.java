import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.lang.Math;


public class Map extends JFrame
{
    private int[] [] map;
    private Enemy [] enemiesOnMap;
    private Player player;
    private BufferedImage bg;
    private JPanel grid;
  

    public Map ()
    {
	// GUI stuff
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	bg = new BufferedImage (600, 600, BufferedImage.TYPE_INT_RGB);
	grid = new ImagePanel (bg);
	grid.setPreferredSize(new Dimension(600,600));
	this.setContentPane(grid);
	pack();
	
	// Array of creatures 
	enemiesOnMap = new Enemy [10];

    }
    
    public void createEnemies(int n)
    {
	for (int i = 0; i < n; i++)
	{
	    enemiesOnMap[i] = new Enemy();
	    Graphics g = bg.getGraphics();
	    
	    g.drawImage(enemiesOnMap[i].getIcon(), enemiesOnMap[i].getX(), enemiesOnMap[i].getY(), null);
	    g.dispose();
	    
	}
	repaint();
    }
    
    public static void main (String [] args)
    {
	Map map = new Map();
	map.setVisible(true);
	map.createEnemies(10);
    }
}

class ImagePanel extends JPanel
{
    public BufferedImage image;

    public ImagePanel (BufferedImage i)
    {
	super ();
	image = i;


    }


    public void paintComponent (Graphics g)
    {
	super.paintComponent (g);


	g.drawImage (image, 0, 0, getWidth (), getHeight (), null);






    }
}
