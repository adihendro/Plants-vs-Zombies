import java.awt.event.*;  
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.imageio.ImageIO;

public class World extends JPanel implements ActionListener{
    private float posZombieX=950f, posZombieY=133f; //just for testing
    private int sunX, sunY=-85; //falling sun x and y position
    //width and height for (p)eashooter, (s)unflower, (r)epeater
    private int pwidth=62, pheight=66, swidth=pwidth, sheight=pheight+5, rwidth=pwidth+2, rheight=pheight+2;
    private Timer timer; //set timer
    private int delay=25; //repaint time

    //img: 0.background, 1.sun, 2.sunflower, 3.peashooter, 4.repeater, 5.sungif, 
    //6.peagif, 7.repgif, 8.zombie, 9.zombief, 10.pea, 11.wasted, 12.try again
    private Image[] img = new Image[13];
    private Rectangle r_sunflower, r_peashooter, r_repeater, r_pea, r_try, r_zombie; //rectangle for plants menu and others
    private Ellipse2D e_sun; //ellipse for falling sun
    private Shape[][] field = new Shape[5][9]; //rectangle array with 5 rows and 9 columns for field area
    private Point mouse = new Point(); //point for mouse position
    private Point[][] plant_field = new Point[5][9]; //array for placing plants
    private Font font;

    private int choice=0, trans=0, i=0,j=0, xa,ya; //for paint plant chooser
    private long startTime, elapsed, sun_elapsed; //for timer
    private boolean bsun=false, play=true;
    // private List<Integer> suns = new ArrayList<Integer>(); //store sunX data

    //public class ListMap<K, V> implements Map<K, V> {


    public World(){
        startTime=System.currentTimeMillis(); //get current time
        timer = new Timer(delay, this); //set up timer
        timer.start();
        
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

        posZombieX-=0.35; //testing
        
        if(elapsed>1 && elapsed%6==0){ //drop falling sun every 6 seconds
            bsun=true;
        }

        r_zombie = new Rectangle(Math.round(posZombieX)+41, Math.round(posZombieY)+55, 20, 40); //testing

        repaint();
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit t=Toolkit.getDefaultToolkit();  
        //load gif image from disk
        img[5]=t.getImage("Assets/Sunflower.gif");
        img[6]=t.getImage("Assets/Peashooter.gif");
        img[7]=t.getImage("Assets/Repeater.gif");
        img[8]=t.getImage("Assets/Zombie.gif");
        img[9]=t.getImage("Assets/Zombief.gif");
        img[10]=t.getImage("Assets/Pea.gif");
        g.drawImage(img[0], 0, 0, 1024, 626, this); //draw background
           
        Graphics2D g2 = (Graphics2D) g;
        //sunflower points
        g2.setFont(font); 
        g2.setColor(Color.BLACK);

        g2.drawString("0", 66, 166);
        
        // make a transparent plant selection following mouse movement
        if(trans==1){ //transparent plant
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
        }else if(trans==2){ //select field and plant a plant
            if(choice==1){ //sunflower gif
                g.drawImage(img[5], plant_field[xa][ya].getX()-swidth/2, plant_field[xa][ya].getY()-sheight/2, swidth, sheight, this);
            }else if(choice==2){ //peashooter gif
                g.drawImage(img[6], plant_field[xa][ya].getX()-(pwidth+2)/2, plant_field[xa][ya].getY()-(pheight+2)/2, pwidth+2, pheight+2, this);
            }else if(choice==3){ //repeater gif
                g.drawImage(img[7], plant_field[xa][ya].getX()-(rwidth+20)/2, plant_field[xa][ya].getY()-(rheight+9)/2, rwidth+26, rheight+13, this);
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
        g.drawImage(img[8], Math.round(posZombieX), plant_field[2][6].getY()-82, pwidth+11, pheight+53, this); //zombie
        // g.drawImage(img[10], plant_field[i][j].getX()+23, plant_field[i][j].getY()-19, this);
        // r_pea = new Rectangle(plant_field[i][j].getX()+23, plant_field[i][j].getY()-19, 20, 20);

            
        if(bsun){ //drop sun every 6 seconds
            if(sunY<470){ //sun falls
                g.drawImage(img[1],sunX,sunY,80,80,this);
                e_sun = new Ellipse2D.Float(sunX, sunY, 80, 80);
                sunY+=1;
            }else{ //sun reach bottom
                sunY=-85;
                sunX=(int)(Math.random() * (900-270+1)+270); //generate sunX from x=270 to x=900
                bsun=false;
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
                // g3.fill(field[i][j]);
        //     }
        // }
        g2.drawString(mouse.print(), 10, 20); //testing coordinate


        //game over
        // if(zombie.getX < 245){
        //     play=false;
            g.drawImage(img[11],365,130,this); //game over text
            g.drawImage(img[12],390,350,250,28,this); //try again text

        // }
        

        g.dispose();
    }
    

    private class MListener1 extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) { //if mouse pressed
            if(play){ //the game is playing
                if(e_sun.contains(e.getPoint())){ //click falling sun
                    sunY=470;
                    //add sun points;
                }else{ // check if mouse clicked plants
                    if (r_sunflower.contains(e.getPoint())) { //click sunflower
                        choice= (choice==1) ? 0:1;
                        trans=1;
                    }else if(r_peashooter.contains(e.getPoint())) { //click peashooter
                        choice= (choice==2) ? 0:2;
                        trans=1;
                    }else if(r_repeater.contains(e.getPoint())) { //click repeater
                        choice= (choice==3) ? 0:3;
                        trans=1;
                    }else if(trans==1 && (choice==1 || choice==2 || choice==3)){ //click field
                        trans=2;
                        for(i=0;i<5;i++){
                            for(j=0;j<9;j++){
                                if(field[i][j].contains(e.getPoint())){
                                    xa=i;
                                    ya=j;
                                    i=10;j=9; //break
                                }
                            }
                        }
                        if(i==5){trans=0;} //not selected a plant-able area
                    }
                }
            }else{ //the game is not playing
                if (r_try.contains(e.getPoint())) { //click try again
                    play=true;
                }
            }
        }
    }
    // public void mouseClicked(MouseEvent e) {}  
    // public void mouseEntered(MouseEvent e) {}  
    // public void mouseExited(MouseEvent e) {}  
    // public void mouseReleased(MouseEvent e) {}  


    private void getImg(){
        try{ //try to load image and font
            img[0]=ImageIO.read(new File("Assets/Background.jpg"));
            img[1]=ImageIO.read(new File("Assets/Sun.png"));
            img[2]=ImageIO.read(new File("Assets/Sunflower.png"));
            img[3]=ImageIO.read(new File("Assets/Peashooter.png"));
            img[4]=ImageIO.read(new File("Assets/Repeater.png"));
            img[11]=ImageIO.read(new File("Assets/Wasted.png"));
            img[12]=ImageIO.read(new File("Assets/Tryagain.png"));
            font = new Font("Assets/Chalkboard.ttc", Font.BOLD, 22); //load font
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
        r_pea = new Rectangle(0, 0, 20, 20);
        r_try = new Rectangle(0, 0, 20, 20);
        //create ellipse for sun
        e_sun = new Ellipse2D.Float(sunX, sunY, 80, 80);

        //generate falling sunX from x=270 to x=900
        sunX=(int)(Math.random() * (900-270+1)+270); 
        
        //create rectangle clickable area for field
        int[] fw = {0,90,165,250,330,410,492,570,651,749}; //field width
        int[] fh = {0,118,215,323,405,516}; //field height
        for(i=0;i<5;i++){
            for(j=0;j<9;j++){
                //set rectangle field area
                field[i][j] = new Rectangle(245+fw[j], 50+fh[i], fw[j+1]-fw[j], fh[i+1]-fh[i]);
                //set point for plant field
                plant_field[i][j] = new Point(296+j*81,117+i*98);
            }
        }
    }
}
