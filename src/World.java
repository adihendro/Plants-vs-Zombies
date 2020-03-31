import java.awt.event.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.imageio.ImageIO;

public class World extends JPanel implements ActionListener{
    private float posZombieX=950f, posZombieY=133f; //just for testing
    
    //width and height for (p)eashooter, (s)unflower, (r)epeater
    private int pwidth=62, pheight=66, swidth=pwidth, sheight=pheight+5, rwidth=pwidth+2, rheight=pheight+2;
    private Timer timer; //set timer

    //img: 0.background, 1.sun, 2.sunflower, 3.peashooter, 4.repeater, 5.sungif, 6.peagif, 7.repgif, 
    //8.zombie, 9.zombief, 10.pea, 11.wasted, 12.try again, 13.sun_g, 14.pea_g, 15.rep_g
    private Image[] img = new Image[16];
    private Rectangle r_sunflower, r_peashooter, r_repeater, r_pea, r_try, r_end, r_zombie; //rectangle for plants menu and others
    private Shape[][] field = new Shape[5][9]; //rectangle array with 5 rows and 9 columns for field area
    private Point mouse = new Point(); //point for mouse position
    private Point[][] plant_f = new Point[5][9]; //array for plants coordinate

    private int choice=0, xp, yp, i, j; //for paint plant chooser
    private long startTime, elapsed; //for timer
    private boolean play=true, bsun=true;

    private Player player;
    private Plant<Integer> plant = new Plant<Integer>(0, 0, 0); // isinya list of plant
    private Sun sun = new Sun();
    public static List<Plant<Integer>> plants = new ArrayList<Plant<Integer>>(); // List of plant dengan type dinyatakan dalam bentuk integer
    public static List<Sun> suns = new ArrayList<Sun>();

    // public static List<Pea<Integer>> peas = new ArrayList<Pea<Integer>>();

    // private List<Integer> suns = new ArrayList<Integer
    //public class ListMap<K, V> implements Map<K, V> {

    public World(){
        startTime=System.currentTimeMillis(); //get current time
        timer = new Timer(25, this); //set up timer
        timer.start();
        
        player = new Player();
        sun.start(5);
        
        getImg(); //load image from disk
        init();

        addMouseListener(new MListener1()); //listen to mouse click

        this.addMouseMotionListener(new MouseMotionAdapter() { //listen to mouse motion
            public void mouseMoved(MouseEvent e) {
                mouse.setX(e.getX());
                mouse.setY(e.getY());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        elapsed=(System.currentTimeMillis()-startTime)/1000; // count time in seconds
        timer.start();

        Zombie zombie = new Zombie("Zombie");
        posZombieX-=zombie.getzombieSpeed(); // INI ZOMBIE JALAN YA GAIS

        r_zombie = new Rectangle(Math.round(posZombieX)+41, Math.round(posZombieY)+55, 20, 40); //testing

        repaint();
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit t=Toolkit.getDefaultToolkit();  
        //load gif image from disk
        img[5]=t.getImage("../Assets/Sunflower.gif");
        img[6]=t.getImage("../Assets/Peashooter.gif");
        img[7]=t.getImage("../Assets/Repeater.gif");
        img[8]=t.getImage("../Assets/Zombie.gif");
        img[9]=t.getImage("../Assets/Zombief.gif");
        img[10]=t.getImage("../Assets/Pea.gif");
        g.drawImage(img[0], 0, 0, 1024, 626, this); //draw background
        
        //not enough sunflower points
        if(player.getCredits()<150){ //<150
            g.drawImage(img[15], 41, 434, rwidth+1, rheight, this); //draw repeater g
            if(player.getCredits()<100){ //<100
                g.drawImage(img[14], 41, 320, pwidth+2, pheight, this); //draw peashooter g
                if(player.getCredits()<50){ //<50
                    g.drawImage(img[13], 42, 196, swidth, sheight, this); //draw sunflower g
                }
            }
        }
           
        Graphics2D g2 = (Graphics2D) g;

        //draw sunflower points
        player.draw(g2);
        
        //make a transparent plant selection following mouse movement
            if(choice==1){ //sunflower
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f)); //set alpha 0.7
                g2.drawImage(img[2], mouse.getX()-swidth/2, mouse.getY()-sheight/2, swidth, sheight, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f)); //set alpha back to 1
            }else if(choice==2){ //peashooter
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
                g2.drawImage(img[3], mouse.getX()-pwidth/2, mouse.getY()-pheight/2, pwidth, pheight, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            }else if(choice==3){ //repeater
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
                g2.drawImage(img[4], mouse.getX()-(rwidth+8)/2, mouse.getY()-(rheight+8)/2, rwidth+4, rheight+4, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            }

        //draw plant
        for(Plant plant: plants){
            xp=plant_f[plant.X()][plant.Y()].getX();
            yp=plant_f[plant.X()][plant.Y()].getY();
            if(plant.getType().equals(1)){ //sunflower gif
                g.drawImage(img[5], xp-swidth/2, yp-sheight/2, swidth, sheight, this);
            }else if(plant.getType().equals(2)){ //peashooter gif
                g.drawImage(img[6], xp-(pwidth+2)/2, yp-(pheight+2)/2, pwidth+2, pheight+2, this);
            }else if(plant.getType().equals(3)){ //repeater gif
                g.drawImage(img[7], xp-(rwidth+20)/2, yp-(rheight+9)/2, rwidth+26, rheight+13, this);
            }
        }

        if(!r_zombie.intersects(field[1][6].getBounds2D())){ //testing
            g.drawImage(img[8], Math.round(posZombieX), Math.round(posZombieY), pwidth+11, pheight+53, this); //zombie
        }
        
        g.drawImage(img[9],900,129,this); //zombief
        
        // for(i=0;i<5;i++){
        //     for(j=0;j<9;j++){
        //     }
        // }

        // g.drawImage(img[8], Math.round(posZombieX), plant_field[2][6].getY()-82, pwidth+11, pheight+53, this); //zombie
        // g.drawImage(img[10], plant_field[i][j].getX()+23, plant_field[i][j].getY()-19, this);
        // r_pea = new Rectangle(plant_field[i][j].getX()+23, plant_field[i][j].getY()-19, 20, 20);

        //draw falling sun
        for(Sun sun: suns){
            if(sun.getY()<sun.getLimit()){ //sun falls
                g.drawImage(img[1],sun.getX(),sun.getY(),80,80,this);
                sun.setE(new Ellipse2D.Float(sun.getX(), sun.getY(), 80, 80));
                sun.lower();
            }else if(sun.getY()<(sun.getLimit()+150)){ //sun waits a while until gone
                g.drawImage(img[1],sun.getX(),sun.getLimit(),80,80,this);
                sun.setE(new Ellipse2D.Float(sun.getX(), sun.getLimit(), 80, 80));
                sun.lower();
            }else{ //falling sun gone
                sun.setE(new Ellipse2D.Float(-85, -85, 80, 80));
                suns.remove(sun);
            }
        }
        
        // show plant menu rectangle
        // g2.setColor(Color.RED);
        // g2.fill(r_sunflower);
        // g2.setColor(Color.YELLOW);
        // g2.fill(r_peashooter);
        // g2.setColor(Color.ORANGE);
        // g2.fill(r_repeater);
        
        g2.setColor(Color.WHITE);
        // g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
        // g2.fill(r_zombie);

        // show rectangle field
        // for(int i=0;i<9;i++){
            // for(int j=0;j<5;j++){
                // g2.fill(field[i][j]);
        //     }
        // }
        g2.drawString(mouse.print(), 10, 20); //testing coordinate


        //game over

        // if(zombie.getX < 245){
        //     play=false;
            // r_end = new Rectangle(0, 0, 1024, 626);
            // g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            // g2.fill(r_end);
            // g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            // g.drawImage(img[11],365,130,this); //game over text
            // g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            // g2.fill(r_try);
            // g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            // g.drawImage(img[12],390,350,250,28,this); //try again text

        // }
        

        g.dispose();
    }
    

    private class MListener1 extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) { //if mouse pressed
            if(play){ //the game is playing
                A: for(Sun sun: suns){
                    if(sun.getE().contains(e.getPoint())){ //click falling sun
                        suns.remove(sun);
                        player.addSunCredits(); //add 25 sun points;
                        bsun=false;
                        break A;
                    }
                }
                if(bsun){
                    // check if mouse clicked plants
                    if (r_sunflower.contains(e.getPoint())) { //click sunflower
                        if(player.getCredits()>=50){ //>=50
                            choice= (choice==1) ? 0:1;
                        }
                    }else if(r_peashooter.contains(e.getPoint())) { //click peashooter
                        if(player.getCredits()>=100){ //>=100
                            choice= (choice==2) ? 0:2;
                        }
                    }else if(r_repeater.contains(e.getPoint())) { //click repeater
                        if(player.getCredits()>=150){ //>=150
                            choice= (choice==3) ? 0:3;
                        }
                    }else if(choice!=0){ //to click field
                        B: for(i=0;i<5;i++){
                            for(j=0;j<9;j++){
                                if(field[i][j].contains(e.getPoint())){ //plant the plant in field
                                    if(plant.put(i,j,choice)){
                                        player.plantType(choice);
                                        choice=0;
                                    }
                                    break B;
                                }
                            }
                        }
                        if(i==5){ //not selected a plant-able area
                            choice=0;
                        }
                    }
                }else{bsun=true;}
            }else{ //the game is not playing
                if (r_try.contains(e.getPoint())) { //click try again
                    play=true;
                }
            }
        }
    }

    private void getImg(){
        try{ //try to load image and font
            img[0]=ImageIO.read(new File("../Assets/Background.jpg"));
            img[1]=ImageIO.read(new File("../Assets/Sun.png"));
            img[2]=ImageIO.read(new File("../Assets/Sunflower.png"));
            img[3]=ImageIO.read(new File("../Assets/Peashooter.png"));
            img[4]=ImageIO.read(new File("../Assets/Repeater.png"));
            img[11]=ImageIO.read(new File("../Assets/Wasted.png"));
            img[12]=ImageIO.read(new File("../Assets/Tryagain.png"));
            img[13]=ImageIO.read(new File("../Assets/Sunflower_g.png"));
            img[14]=ImageIO.read(new File("../Assets/Peashooter_g.png"));
            img[15]=ImageIO.read(new File("../Assets/Repeater_g.png"));
        } catch(IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString()); //show error dialog
        }
    }

    private void init(){
        //create rectangle for plant menu, peas, and try again
        r_sunflower = new Rectangle(30, 185, pwidth+25, pheight+55);
        r_peashooter = new Rectangle(30, 190+pheight+50, pwidth+25, pheight+52);
        r_repeater = new Rectangle(30, 190+2*pheight+95, pwidth+25, pheight+53);
        r_pea = new Rectangle(-25, -25, 20, 20);
        r_try = new Rectangle(389, 347, 253, 35);
        
        //create rectangle clickable area for field
        int[] fw = {0,90,165,250,330,410,492,570,651,749}; //field width
        int[] fh = {0,118,215,323,405,516}; //field height
        for(i=0;i<5;i++){
            for(j=0;j<9;j++){
                //set rectangle field area
                field[i][j] = new Rectangle(245+fw[j], 50+fh[i], fw[j+1]-fw[j], fh[i+1]-fh[i]);
                
                //set point for plant field
                plant_f[i][j] = new Point(296+j*81,117+i*98);
                plant.setP(i, j);
            }
        }
    }
}
