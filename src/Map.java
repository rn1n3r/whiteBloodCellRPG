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
    private Enemy[] enemiesOnMap;
    private Player player;
    private BufferedImage bg;
    private JPanel grid;
    private Player player1;
    Graphics g;

    public Map ()
    {

	// GUI stuff
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	bg = new BufferedImage (600, 600, BufferedImage.TYPE_INT_RGB);
	grid = new ImagePanel (bg);
	g = bg.getGraphics ();
	grid.setPreferredSize (new Dimension (600, 600));
	this.setContentPane (grid);
	pack ();

	this.addKeyListener (new Controller ());

	// Array of creatures
	enemiesOnMap = new Enemy [10];

    }


    public void createEnemies (int n)
    {
	player1 = new Player (1);
	player1.show (g);

	for (int i = 0 ; i < n ; i++)
	{
	    enemiesOnMap [i] = new Enemy ();
	    enemiesOnMap [i].show (g);
	}
	repaint ();
    }


    public static void main (String[] args)
    {
	Map map = new Map ();
	map.setVisible (true);
	map.createEnemies (10);
    }
    
    public void showAll(){//show everything and updates the screen
	g.setColor(Color.black);
	g.fillRect(0, 0, getWidth(), getHeight());//clears screen
	player1.show (g);// draws player
	
	for (int i = 0 ; i < 10 ; i++)//draw enemies
	    enemiesOnMap [i].show (g);
    }


    class Controller implements KeyListener
    {
	public void keyPressed (KeyEvent e)
	{
	    if (e.getKeyCode () == KeyEvent.VK_W)
		player1.move (-2);
	    if (e.getKeyCode () == KeyEvent.VK_D)
		player1.move (1);
	    if (e.getKeyCode () == KeyEvent.VK_S)
		player1.move (2);
	    if (e.getKeyCode () == KeyEvent.VK_A)
		player1.move (-1);
    
	    showAll();
	    repaint ();
	}

	public void keyReleased (KeyEvent e)
	{
	}

	public void keyTyped (KeyEvent e)
	{
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
}

