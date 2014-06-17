
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

    private int[][] map;//main map
    private static Enemy[] enemiesOnMap;//all enemies
    private BufferedImage bg, bg2, dialogPic; //for each room and dialog    
    private JPanel mainPanel, grid, grid1;//cardlayout panels, switching each room

    private CardLayout cl = new CardLayout();
    private static Player player1;//player
    private Graphics g;
    private static Timer t1, temp;//time the enemies and player
    private boolean inDialog = false;
    private int currentDialog = 0;
    private int numDialog;
    private String diaLevel;

    //for different map
    private int level, current;

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

        //other map
        grid1 = new ImagePanel(bg2);

        mainPanel = new JPanel(cl);

        level = 1;
        current = 0;
        mainPanel.add(grid, "0");
        mainPanel.add(grid1, "1");

        this.setContentPane(mainPanel);
        pack();

        this.addKeyListener(new Controller());

        // Array of creatures
        enemiesOnMap = new Enemy[10];

    }

    public void createPlayer() {

        player1 = new Player(1);
    }

    public void createEnemies(int n) {

        for (int i = 0; i < n; i++) {
            enemiesOnMap[i] = new Enemy();

        }
        repaint();
    }

    public static void main(String[] args) {
        Map map = new Map();
        map.createPlayer();
        map.createEnemies(10);
        map.setVisible(true);

        Timer t1 = new Timer(300, player1);
        t1.start();
        for (int i = 0; i < 10; i++) {
            temp = new Timer(400, enemiesOnMap[i]);
            temp.addActionListener(map);
            temp.setInitialDelay(500);
            temp.start();
        }
        map.showDialog("0", 2);

    }

    public void showDialog(String level, int num) {
        diaLevel = level;
        numDialog = num;
        inDialog = true; // boolean to pause timer when in dialog
        currentDialog = 0;
        dialogPic = null;
        try {
            dialogPic = ImageIO.read(Map.class.getResource(level + "" + currentDialog + ".png")); // load first dialog picture
        } catch (IOException e) {
        }

        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (!inDialog) {

            repaint();

        }
    }

    class Controller implements KeyListener {

        public void keyPressed(KeyEvent e) {
            if (inDialog) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) // update currentDialog
                {
                    currentDialog++;
                    if (currentDialog != numDialog) {

                        try {
                            dialogPic = ImageIO.read(Map.class.getResource(diaLevel + "" + currentDialog + ".png")); // currentDialog is updated through Controller
                        } catch (IOException q) {
                        }
                        repaint();

                    } else {
                        inDialog = false;
                    }

                    System.out.print("lol");
                }
            } else {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    player1.move(-2, enemiesOnMap);
                    //showDialog("0", 2);

                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    player1.move(1, enemiesOnMap);
                    //going back after finishing a quest
                    if (current != 0 && player1.getX() == 580 && player1.getY() < 280 && player1.getY() > 200) {
                        if (isQuestDone()) {
                            player1.setLocation(0, player1.getY());
                            //~~~reused code
                            for (int i = 0; i < 10; i++) {
                                enemiesOnMap[i] = new Enemy();
                            }

                            cl.show(mainPanel, "" + 0);
                            current = 0;
                            level++;
                            g.dispose();
                            g = bg.getGraphics();
                        } else {
                            // prompts that quest is not done
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    player1.move(2, enemiesOnMap);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    player1.move(-1, enemiesOnMap);

                }
                if (current == 0 && player1.getX() == 20 && player1.getY() < 320 && player1.getY() > 240) {
                    player1.setLocation(580, player1.getY());
                    cl.show(mainPanel, "" + level);
                    current = level;
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

    public boolean isQuestDone() {
        if (level == 1) {
            boolean temp = true;

            for (int i = 0; i < 10 && temp; i++) {
                if (enemiesOnMap[i].isAlive()) {
                    temp = false;
                }
            }

            return temp;

        }

        return false;
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

            {
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
}
