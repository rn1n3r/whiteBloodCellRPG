
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
    private Creature npc1 = new Creature(3);
    private CardLayout cl = new CardLayout();
    private static Player player1;//player
    private Graphics g;
    private static Timer t1, temp;//time the enemies and player
    private boolean inDialog = false;
    private boolean notShown = true;
    private int currentDialog = 0;
    private int numDialog;
    private String diaLevel;

    //for different levels (quests)
    private int level;

    //for different maps
    private int stage = 1;

    public Map() {

        // GUI stuff
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        npc1.setLocation(280, 0);
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

        mainPanel.add(grid, "1");
        mainPanel.add(grid1, "2");

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

        Timer t1 = new Timer(30, player1);
        t1.start();
        for (int i = 0; i < 10; i++) {
            temp = new Timer(400, enemiesOnMap[i]);
            temp.addActionListener(map);
            temp.setInitialDelay(500);
            temp.start();
        }

    }

    public void showDialog(String clevel, int num) {
        diaLevel = clevel;
        numDialog = num;
        inDialog = true; // boolean to pause timer when in dialog
        currentDialog = 0;
        dialogPic = null;
        try {
            dialogPic = ImageIO.read(Map.class.getResource(clevel + "" + currentDialog + ".png")); // load first dialog picture
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
                    triggerDialog();
                    //showDialog("0", 2);

                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    player1.move(1, enemiesOnMap);
                    triggerDialog();
                    //going back after finishing a quest

                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    player1.move(2, enemiesOnMap);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    triggerDialog();
                    player1.move(-1, enemiesOnMap);

                }
                // first portal, check quests
                if (player1.getX() == 20 && player1.getY() < 320 && player1.getY() > 240) {
                    if (level == 2) {
                        
                        if (level > stage) {
                            notShown = true;
                            player1.setLocation(580, player1.getY());
                            stage++;
                            cl.show(mainPanel, "" + stage);

                            g.dispose();
                            g = bg2.getGraphics();

                        }
                    } 

                }

                if (isQuestDone()) {
                    triggerDialog();
                }

                //showAll();
                repaint();
            }
        }

        public void triggerDialog() {
            if (isQuestDone() && notShown) {
                notShown = false;

                showDialog("" + level, 2);
                level++;
            } else if (level == 1 && player1.getX() >= 280 && player1.getX() <= 320 && player1.getY() <= 40 && player1.getY() >= 0) {
                showDialog("0", 3);
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

            int enemiesKilled = 0;
            for (int i = 0; i < 10 && temp; i++) {
                if (!enemiesOnMap[i].isAlive()) {
                    enemiesKilled++;
                }
            }
            if (enemiesKilled < 1) {
                temp = false;
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
                player1.show(g);
                if (inDialog) {
                    g.drawImage(dialogPic, 0, 450, null);
                }

                npc1.show(g);

            }
        }
    }
}
