
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class Map extends JFrame implements ActionListener {

    private int[][] map;
    private static Enemy[] enemiesOnMap;
    private BufferedImage bg, bg2, dialogPic;
    private JPanel mainPanel, grid, grid1;
    private CardLayout cl = new CardLayout();
    private static Player player1;
    private Graphics g;
    private static Timer t1, temp;
    private boolean inDialog = false;
    private int currentDialog = 0;

    public Map() {

        // GUI stuff
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            bg = ImageIO.read(Map.class.getResource("bkgr-1.png"));
            bg2 = ImageIO.read(Map.class.getResource("bkgr-2.png"));
        } catch (IOException e) {
        }

        grid = new ImagePanel(bg);
        g = bg.getGraphics();
        grid.setPreferredSize(new Dimension(600, 600));

        //second map
        grid1 = new ImagePanel(bg2);

        mainPanel = new JPanel(cl);
        mainPanel.add(grid, "1");
        mainPanel.add(grid1, "2");

        this.setContentPane(mainPanel);
        pack();

        this.addKeyListener(new Controller());

        // Array of creatures
        enemiesOnMap = new Enemy[10];

    }

    public void createEnemies(int n) {
        player1 = new Player(1);

        for (int i = 0; i < n; i++) {
            enemiesOnMap[i] = new Enemy();

        }
        repaint();
    }

    public static void main(String[] args) {
        Map map = new Map();
        map.createEnemies(10);
        map.setVisible(true);

        Timer t1 = new Timer(300, player1);
        t1.start();
        for (int i = 0; i < 10; i++) {
            temp = new Timer(100, enemiesOnMap[i]);
            temp.addActionListener(map);
            temp.setInitialDelay(500);
            temp.start();
        }
        map.showDialog("0", 2);
    }

    public void showDialog(String level, int numDialog) {
        inDialog = true; // boolean to pause timer when in dialog
        currentDialog = 0;
        dialogPic = null;
        try {
            dialogPic = ImageIO.read(Map.class.getResource(level + "" + currentDialog + ".png")); // load first dialog picture
        } catch (IOException e) {
        }

        while (currentDialog != numDialog) // until all the dialogs have been cycled through
        {
            try {
                dialogPic = ImageIO.read(Map.class.getResource(level + "" + currentDialog + ".png")); // currentDialog is updated through Controller

                repaint();
            } catch (IOException e) {
            }
        }

        inDialog = false;
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (inDialog); else {

            repaint();
        }
    }

    class Controller implements KeyListener {

        public void keyPressed(KeyEvent e) {
            if (inDialog) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) // update currentDialog
                {
                    currentDialog++;
                    System.out.print("lol");
                }
            } else {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    player1.move(-2, enemiesOnMap);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    player1.move(1, enemiesOnMap);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    player1.move(2, enemiesOnMap);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    player1.move(-1, enemiesOnMap);
                }

                if (player1.getX() < 0) {
                    player1.setLocation(580, player1.getY())
                    cl.show(mainPanel, "2");
                    g.dispose();
                    g = bg2.getGraphics();
                }

                //showAll();
                repaint();
            }
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    class ImagePanel extends JPanel//draw images on the screen
    {

        public BufferedImage image;

        public ImagePanel(BufferedImage i) {

            super();
            image = i;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            for (int i = 0; i < 10; i++)//draw enemies
            {
                if (enemiesOnMap[i].isAlive()) {
                    enemiesOnMap[i].show(g);
                }
            }
            if (inDialog) {
                g.drawImage(dialogPic, 0, 450, null);
            }
            player1.show(g);
        }
    }
}
