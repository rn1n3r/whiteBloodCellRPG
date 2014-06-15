import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.lang.Math;


public class Map extends JFrame implements ActionListener
{
    private int[] [] map;
    private static Enemy[] enemiesOnMap;
    private BufferedImage bg, bg2;
    private JPanel mainPanel, grid, grid1;
    private CardLayout cl = new CardLayout();
    private static Player player1;
    Graphics g;
    private static Timer t1,temp;
    private boolean inDialog = false;
    private int currentDialog = 0;
    
    public Map ()
    {

	// GUI stuff
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	bg = new BufferedImage (600, 600, BufferedImage.TYPE_INT_RGB);
	grid = new ImagePanel (bg);
	g = bg.getGraphics ();
	grid.setPreferredSize (new Dimension (600, 600));
        
        //second map
        try
        {
            bg2 = ImageIO.read (Map.class.getResource("grdi1.png"));
        }
        catch (IOException e){}
        grid1 = new ImagePanel(bg2);
        
        mainPanel = new JPanel (cl);
        mainPanel.add(grid, "1");
        mainPanel.add(grid1, "2");
        
	this.setContentPane (mainPanel);
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
	map.createEnemies (10);
	map.setVisible (true);
	
	Timer t1 = new Timer (300, player1);
	t1.start();
	for(int i = 0; i < 10; i ++){
	    temp = new Timer (100, enemiesOnMap[i]);
	    temp.addActionListener(map);
	    temp.setInitialDelay(500);
	    temp.start();
	}
        map.showDialog("0",2);
    }
    
    public void showAll(){//show everything and updates the screen
	g.setColor(Color.black);
	g.fillRect(0, 0, getWidth(), getHeight());//clears screen
	player1.show (g);// draws player
	
	for (int i = 0 ; i < 10 ; i++)//draw enemies
	    if(enemiesOnMap[i].isAlive())
		enemiesOnMap [i].show (g);
    }
    
    public void showDialog(String level, int numDialog)
    {
        inDialog = true; // boolean to pause timer when in dialog
        currentDialog = 0; 
        BufferedImage dialogPic = null;
        try
        {
            dialogPic =ImageIO.read (Map.class.getResource(level + "" +currentDialog + ".png")); // load first dialog picture
        }
        catch (IOException e){}
        
        g.drawImage(dialogPic, 0, 450, null); // draw it
                
        while (currentDialog != numDialog) // until all the dialogs have been cycled through
        {
           try
           {
                dialogPic =ImageIO.read (Map.class.getResource(level + "" +currentDialog + ".png")); // currentDialog is updated through Controller
                g.drawImage(dialogPic, 0, 450, null);
                repaint();
           }
           catch (IOException e){}
        }
        
        inDialog = false;
        repaint();
    }
    

    public void actionPerformed (ActionEvent e)
    {
        if (inDialog);
        else
        {
            showAll();
            repaint();
        }
    }

    class Controller implements KeyListener
    {
	public void keyPressed (KeyEvent e)
	{
            if (inDialog)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) // update currentDialog
                {
                    currentDialog++;
                    System.out.print("lol");
                }
            }
            
            else
            {
                if (e.getKeyCode () == KeyEvent.VK_W)
                    player1.move (-2, enemiesOnMap);
                if (e.getKeyCode () == KeyEvent.VK_D)
                    player1.move (1, enemiesOnMap);
                if (e.getKeyCode () == KeyEvent.VK_S)
                    player1.move (2, enemiesOnMap);
                if (e.getKeyCode () == KeyEvent.VK_A)
                    player1.move (-1, enemiesOnMap);
                
                if (player1.getX() <0)
                {
                    player1.setLocation(580,player1.getY())
                    cl.show(mainPanel, "2");
                    g = bg2.getGraphics();
                }

                showAll();
                repaint ();
            }
	}

	public void keyReleased (KeyEvent e)
	{
	}

	public void keyTyped (KeyEvent e)
	{
	}
    }


    class ImagePanel extends JPanel//draw images on the screen
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


