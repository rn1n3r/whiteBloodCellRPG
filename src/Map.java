
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
    private BufferedImage bg, bg2, bg3, dialogPic; //for each room and dialog    
    private JPanel mainPanel, grid, grid1, grid2;//cardlayout panels, switching each room
    private Creature npc1 = new Creature(3); //guide 1
    private Creature npc2 = new Creature (2); //guide 2
    private Creature npc3 = new Creature (4); //commander t
    private CardLayout cl = new CardLayout();
    private static Player player1;//player
    private Graphics g;
    private static Timer t1;//time the enemies and player
    private static Timer[] temp = new Timer[10];
    private boolean inDialog = false;
    private boolean notShown = true;
    private int currentDialog = 0;
    private int numDialog;
    private String diaLevel;

    //for different levels (quests)
    private int level = 1;
    //for different maps
    private int stage = 1;

    public Map() {

        // GUI stuff
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        npc1.setLocation(280,0);//set location of npc's
        npc2.setLocation(280,0);
        npc3.setLocation(280,0);
        try {
            bg = ImageIO.read(Map.class.getResource("bkgr-1.png"));//load different backgrounds
            bg2 = ImageIO.read(Map.class.getResource("bkgr-2.png"));
            bg3 = ImageIO.read(Map.class.getResource("bkgr-3.png"));
        } catch (IOException e) {
        }

        grid = new ImagePanel(bg);//copies maps onto JPanels
        grid1 = new ImagePanel(bg2);
        grid2 = new ImagePanel (bg3);
        
        g = bg.getGraphics();//graphics for drawing
        grid.setPreferredSize(new Dimension(600, 600));

        mainPanel = new JPanel(cl);

        mainPanel.add(grid, "1");//maps to rotation
        mainPanel.add(grid1, "2");
        mainPanel.add(grid2, "3");

        this.setContentPane(mainPanel);
        pack();

        this.addKeyListener(new Controller());

        // Array of creatures
        enemiesOnMap = new Enemy[10];

    }

    public void createPlayer() {//creates players
        player1 = new Player(1);
    }

    public void createEnemies(int n) {//creates enemies
        for (int i = 0; i < n; i++) {
            enemiesOnMap[i] = new Enemy();
        }
        repaint();
    }

    public static void main(String[] args) {//main method, handles timers
        Map map = new Map();//makes a new game
        map.createPlayer();
        map.createEnemies(10);
        map.setVisible(true);
        
        //controls speed of player
        Timer t1 = new Timer(30, player1);
        t1.start();
        for (int i = 0; i < 10; i++) {
            temp[i] = new Timer(2000, enemiesOnMap[i]);//controls speed of enemies
            temp[i].addActionListener(map);
            temp[i].setInitialDelay(500);
            temp[i].start();
        }

    }

    public void showDialog(String clevel, int num) {//shows dialogues
        showDialog(clevel, num, 0);
    }
    
    public void showDialog (String clevel, int num, int cDia){//overload method
        currentDialog = cDia;
        diaLevel = clevel;
        numDialog = num;
        inDialog = true; // boolean to pause timer when in dialog
        currentDialog = cDia;
        dialogPic = null;
        try {
            dialogPic = ImageIO.read(Map.class.getResource(clevel + "" + currentDialog + ".png")); // load first dialog picture
        } catch (IOException e) {
        }

        repaint();
    }

    public void actionPerformed(ActionEvent e) {//prevents the image from not updating
        if (!inDialog) {
            repaint();
        }
    }

    class Controller implements KeyListener {//controls

        public void keyPressed(KeyEvent e) {
            if (inDialog) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) // update currentDialog
                {
                    currentDialog++;
                    if (currentDialog != numDialog) {//continues to next dialogue

                        try {
                            dialogPic = ImageIO.read(Map.class.getResource(diaLevel + "" + currentDialog + ".png")); // currentDialog is updated through Controller
                        } catch (IOException q) {
                        }
                        repaint();

                    } else {
                        inDialog = false;//finished dialogue
                    }

                    System.out.print("lol");//~~~ remove
                }
            } else {
                if (e.getKeyCode() == KeyEvent.VK_W) {//go up
                    player1.move(-2, enemiesOnMap);
                    triggerDialog();
                    //showDialog("0", 2);

                }
                if (e.getKeyCode() == KeyEvent.VK_D) {//go right
                    player1.move(1, enemiesOnMap);
                    triggerDialog();
                    //going back after finishing a quest

                }
                if (e.getKeyCode() == KeyEvent.VK_S) {//go down
                    player1.move(2, enemiesOnMap);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {//go right
                    triggerDialog();
                    player1.move(-1, enemiesOnMap);

                }
                
                // check if player is on portal
                if (player1.getX() == 20 && player1.getY() < 320 && player1.getY() > 240) {
                    if (level == 2 || level == 3) {//second level
                        if (level > stage) {//if the portal is open to nextstage
                            notShown = true;
                            player1.setLocation(580, player1.getY());
                            stage++;//go to next stage
                            cl.show(mainPanel, "" + stage);

                            g.dispose();
                            
                            for(int i = 0; i < 10; i ++){//respawn enemies
                                if(!enemiesOnMap[i].isAlive())
                                    enemiesOnMap[i].respawn();
                            }
                            
                            if(level == 2){
                                g = bg2.getGraphics();
                                for(int i = 0; i < 10; i ++)//changes speed of enemesi
                                    temp[i].setDelay(200);
                            }
                            else{
                                g = bg3.getGraphics();
                                for(int i = 0; i < 10; i ++)
                                    temp[i].setDelay(100);
                            }
                        }
                    } 

                }
                //check if player has finished the quest
                if (isQuestDone()) {
                    triggerDialog();
                }

                //showAll();
                repaint();
            }
        }
        //dialogue for the 
        public void triggerDialog() {
            if (isQuestDone() && notShown) {
                
                notShown = false;
                if (level == 1){//first level
                    showDialog("1", 2);
                }
                if (level == 2){//second level
                    showDialog("2",1);
                }
                
                if (level == 3){//third elvel
                    showDialog("3",3);
                }
                level++;
                
                
                //npc location for first level
            } else if (level == 1 && player1.getX() >= 280 && player1.getX() <= 320 && player1.getY() <= 40 && player1.getY() >= 0) {
                showDialog("0", 3);
            }//npc location for first level
            else if (level == 2 && player1.getX() >= 280 && player1.getX() <= 320 && player1.getY() <= 40 && player1.getY() >= 0) {
                showDialog("a",2);
            }
            //npc location for first level
            else if (level == 3 && player1.getX() >= 280 && player1.getX() <= 320 && player1.getY() <= 40 && player1.getY() >= 0) {
                showDialog("c",4);
            }
            
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    public boolean isQuestDone() {//check if quest is done
        boolean temp = true;
        if (level == 1) {//first quest
            int enemiesKilled = 0;
            for (int i = 0; i < 10; i++) {
                if (!enemiesOnMap[i].isAlive()) {
                    enemiesKilled++;//counts enemies killed
                }
            }
            if (enemiesKilled < 1) {
                temp = false;//not finished quest
            }
        }
        else if (level == 2){ //second quest

            int enemiesKilled = 0;
            for (int i = 0; i < 10; i++) {
                if (!enemiesOnMap[i].isAlive()) {
                    enemiesKilled++;//counts enemies killed
                }
            }
            if (enemiesKilled < 5) {
                temp = false;//not done
            }
        }
        else if (level == 3){ //third quest
            int enemiesKilled = 0;
            for (int i = 0; i < 10; i++) {
                if (!enemiesOnMap[i].isAlive()) {//counts enemies killed
                    enemiesKilled++;
                }
            }
            if (enemiesKilled < 10) {
                temp = false;//not done, need 10 kills
            }
        }

        return temp;
    }

    class ImagePanel extends JPanel//draw images on the screen
    {

        public BufferedImage image;

        public ImagePanel(BufferedImage i) {//makes a buffered image

            super();
            image = i;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);//double buffering

            {
                for (int i = 0; i < 10; i++)//draw enemies
                {
                    if (enemiesOnMap[i].isAlive()) {//shows the enemies
                        enemiesOnMap[i].show(g);
                    }
                }
                player1.show(g);
                if (inDialog) {
                    g.drawImage(dialogPic, 0, 450, null);
                }
                if (stage == 1){//different npc's for different stages
                    npc1.show(g);
                }
                else if (stage == 2){
                    npc2.show(g);
                }
                else if (stage == 3){
                    npc3.show(g);
                }

            }
        }
    }
}
