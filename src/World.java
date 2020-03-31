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
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 

public class World extends JPanel implements ActionListener{
    private static final long serialVersionUID = 1L;

    // width and height for (p)eashooter, (s)unflower, (r)epeater
    private int pwidth=62, pheight=66, swidth=pwidth, sheight=pheight+5, rwidth=pwidth+2, rheight=pheight+2;
    private Timer timer, timer2; //set timer

    //img: 0.background, 1.sun, 2.sunflower, 3.peashooter, 4.repeater, 5.sungif, 6.peagif, 7.repgif, 
    //8.zombie, 9.zombief, 10.pea, 11.wasted, 12.try again, 13.sun_g, 14.pea_g, 15.rep_g, 16.win, 17.play again
    private Image[] img = new Image[18];
    private Rectangle r_sunflower, r_peashooter, r_repeater, r_again, r_end; //rectangle for plants menu and others
    private Shape[][] field = new Shape[5][9]; //rectangle array with 5 rows and 9 columns for field area
    private Point mouse = new Point(); //point for mouse position
    public static Point[][] plant_f = new Point[5][9]; //array for plants coordinate
    private int choice=0, xp, yp, i, j; //for paint plant chooser
    private boolean play=true, win=false, sun_clicked=false;

    //audio
    private Clip clip, clip2, clip3; 

    private Player player;  
    private Plant<Integer> plant = new Plant<Integer>(0, 0, 0);
    private Sun sun = new Sun();
    private Pea pea = new Pea(0, 0, 0);
    private Zombie zombie = new Zombie(0);

    public static List<Plant<Integer>> plants = new ArrayList<Plant<Integer>>();
    public static List<Sun> suns = new ArrayList<Sun>();
    public static List<Pea> peas = new ArrayList<Pea>();
    public static List<Zombie> zombies = new ArrayList<Zombie>();
      

    public World(){
        timer = new Timer(25, this); //set up timer
        
        player = new Player();
        sun.start(5);
        zombie.start(20);
        
        getImg(); //load image from disk
        init();
        
        addMouseListener(new MListener()); //listen to mouse click
        addMouseMotionListener(new MouseMotionAdapter() { //listen to mouse motion
            public void mouseMoved(MouseEvent e) {
                mouse.setX(e.getX());
                mouse.setY(e.getY());
            }
        });
        
        
        //music
        try{
            // create clip reference 
            clip = AudioSystem.getClip(); 
            clip2 = AudioSystem.getClip(); 
            clip3 = AudioSystem.getClip(); 
            // open audioInputStream to the clip 
            clip.open(AudioSystem.getAudioInputStream(new File("../Assets/Background.wav").getAbsoluteFile())); 
            clip2.open(AudioSystem.getAudioInputStream(new File("../Assets/End.wav").getAbsoluteFile())); 
            clip3.open(AudioSystem.getAudioInputStream(new File("../Assets/Zombies_coming.wav").getAbsoluteFile())); 
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
        }catch (Exception ex)  { 
            System.out.println("Error with playing sound."); 
        } 
        clip.start(); 

        timer2 = new Timer(12000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                clip3.start(); 
                timer2.stop();
            }
        });
        
        timer.start();
        timer2.start();
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
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
        g.drawImage(img[0], 0, 0, 1024, 626, this); //draw background
        
        //check if not enough sunflower points
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
                plant.act();

            }else{
                if(plant.getType().equals(2)){ //peashooter gif
                    g.drawImage(img[6], xp-(pwidth+2)/2, yp-(pheight+2)/2, pwidth+2, pheight+2, this);
                }else if(plant.getType().equals(3)){ //repeater gif
                    g.drawImage(img[7], xp-(rwidth+20)/2, yp-(rheight+9)/2, rwidth+26, rheight+13, this);
                }

                //shoot zombie
                xp=plant.X();
                yp=plant.Y();
                A: for(Zombie zombie: zombies){
                    if(xp==zombie.getLane() && yp<=zombie.getColumn()){ //zombie exist in front of plant?
                        if(plant.isIdle()){
                            plant.attack();
                        }
                        plant.setThreat(true);
                        break A;
                    }else{
                        plant.setThreat(false);
                    }
                }
                if(!plant.isThreaten()){ //plant is not threaten: no zombie in front of plant
                    plant.stop();
                }
            }
        }
        
        //zombie
        Iterator<Zombie> itz = zombies.iterator(); 
        while (itz.hasNext()){
            zombie=itz.next();
            
            //draw zombie
            if(zombie.getType()==1){ //standard zombie
                g.drawImage(img[8], Math.round(zombie.getCoorX()), zombie.getCoorY(), pwidth+11, pheight+53, this);
            }else if(zombie.getType()==2){ //football zombie
                g.drawImage(img[9], Math.round(zombie.getCoorX()), zombie.getCoorY(), this);
            }

            //check is zombie intersect plant
            zombie.attack();
            
            //check is zombie intersect pea
            Iterator<Pea> itp = peas.iterator(); 
            A: while (itp.hasNext()){
                pea=itp.next();
                xp=zombie.getLane();
                if(pea.X()==xp){ //same lane
                    if(zombie.getType()==1){ //normal zombie
                        if((pea.getCoorX()>=zombie.getCoorX()-3) && (pea.getCoorX()<=zombie.getCoorX()+92)){ //hit 62
                            zombie.hit(pea.getDamage());
                            itp.remove();
                            break A;
                        }
                    }else if(zombie.getType()==2){ //football zombie
                        if((pea.getCoorX()>=zombie.getCoorX()+10) && (pea.getCoorX()<=zombie.getCoorX()+105)){ //hit 75
                            zombie.hit(pea.getDamage());
                            itp.remove();
                            break A;
                        }
                    }
                }
            }

            if(zombie.isDead()){
                itz.remove();
            }
            
            if(zombie.gameOver()){
                play=false;
            }
        }

        if(Zombie.getN()==40 && zombies.isEmpty()){
            play=false;
            win=true;
        }

        
        if(play){
            //draw pea
            Iterator<Pea> itp = peas.iterator();
            while (itp.hasNext()){
                pea=itp.next();
                g.drawImage(img[10], pea.getCoorX(), pea.getCoorY(), this);
                pea.move();
                    
                if(pea.getCoorX()>1030){
                    itp.remove();
                }
            }
        
            //draw falling sun
            Iterator<Sun> its = suns.iterator(); 
            while (its.hasNext()){
                sun=its.next();
                if(sun.isSunflower()){
                    if(sun.getY()<sun.getLimit()){ //sun falls
                        g.drawImage(img[1],sun.getX(),sun.getY2(),80,80,this);
                        sun.setE(new Ellipse2D.Float(sun.getX(), sun.getY2(), 80, 80));
                        sun.lower();
                    }else{ //falling sun gone
                        its.remove();
                    }
                }else{
                    if(sun.getY()<sun.getLimit()){ //sun falls
                        g.drawImage(img[1],sun.getX(),sun.getY(),80,80,this);
                        sun.setE(new Ellipse2D.Float(sun.getX(), sun.getY(), 80, 80));
                        sun.lower();
                    }else if(sun.getY()<(sun.getLimit()+200)){ //sun waits a while until gone
                        g.drawImage(img[1],sun.getX(),sun.getLimit(),80,80,this);
                        sun.setE(new Ellipse2D.Float(sun.getX(), sun.getLimit(), 80, 80));
                        sun.lower();
                    }else{ //falling sun gone
                        its.remove();
                    }
                }
            }

        }else{ //play=false, win or game over
            clip.stop(); 
            clip2.start();
            clip2.loop(Clip.LOOP_CONTINUOUSLY); 

            peas.clear();
            suns.clear();
            for(Plant plant: plants){
                plant.stop();
            }
            // for(Zombie zombie: zombies){
            //     zombie.stop();
            // }

            if(win){
                r_end = new Rectangle(0, 0, 1024, 626);
                g2.setColor(Color.WHITE);
                g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
                g2.fill(r_end);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
                // r_again = new Rectangle(445, 410, 140, 65);

                g.drawImage(img[16],263,130,500,250,this); //win text
                // g.drawImage(img[17],445,410,140,65,this); //play again text
                
            }else{
                r_end = new Rectangle(0, 0, 1024, 626);
                g2.setColor(Color.WHITE);
                g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
                g2.fill(r_end);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
                // r_again = new Rectangle(395, 340, 240, 35);
                
                g.drawImage(img[11],365,130,this); //game over text
                // g.drawImage(img[12],400,345,230,25,this); //try again text
            }
        }

        g.dispose();
    }
    

    private class MListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) { //if mouse pressed
            if(play){ //the game is playing
                Iterator<Sun> its = suns.iterator(); 
                A: while (its.hasNext()){
                    sun=its.next();
                    try{
                        if(sun.getE().contains(e.getPoint())){ //click falling sun
                            player.addSunCredits(); //add 25 sun points;
                            sun_clicked=true;
                            its.remove();
                            break A;
                        }
                    }catch(Exception ex){}
                }
                if(!sun_clicked){
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
                        A: for(i=0;i<5;i++){
                            for(j=0;j<9;j++){
                                if(field[i][j].contains(e.getPoint())){ //plant the plant in field
                                    if(plant.put(i,j,choice)){
                                        player.plantType(choice);
                                        choice=0;
                                    }
                                    break A;
                                }
                            }
                        }
                        if(i==5){ //not selected a plant-able area
                            choice=0;
                        }
                    }
                }else{sun_clicked=false;}

            }else{ //the game is not playing
                // if (r_again.contains(e.getPoint())) { //click again
                //     play=true;
                //     win=false;
                //     player.resetCredits();
                //     plants.clear();
                //     suns.clear();
                //     peas.clear();
                //     zombies.clear();
                //     Zombie.resetN();

                //     sun.start(5);
                //     // zombie.start(20);
                //     for(i=0;i<5;i++){
                //         for(j=0;j<9;j++){
                //             Plant.setOcc(i, j);
                //         }
                //     }
                // }
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
            img[10]=ImageIO.read(new File("../Assets/Pea.png"));
            img[11]=ImageIO.read(new File("../Assets/Wasted.png"));
            img[12]=ImageIO.read(new File("../Assets/Tryagain.png"));
            img[13]=ImageIO.read(new File("../Assets/Sunflower_g.png"));
            img[14]=ImageIO.read(new File("../Assets/Peashooter_g.png"));
            img[15]=ImageIO.read(new File("../Assets/Repeater_g.png"));
            img[16]=ImageIO.read(new File("../Assets/Win.png"));
            img[17]=ImageIO.read(new File("../Assets/Playagain.png"));
        } catch(IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString()); //show error dialog
        }
    }

    private void init(){
        //create rectangle for plant menu, peas, and try again
        r_sunflower = new Rectangle(30, 184, pwidth+26, pheight+56);
        r_peashooter = new Rectangle(30, 190+pheight+50, pwidth+26, pheight+52);
        r_repeater = new Rectangle(30, 190+2*pheight+95, pwidth+26, pheight+55);
        
        //create rectangle clickable area for field
        int[] fw = {0,90,165,250,330,410,492,570,651,749}; //field width
        int[] fh = {0,118,215,323,405,516}; //field height
        for(i=0;i<5;i++){
            for(j=0;j<9;j++){
                //set rectangle field area
                field[i][j] = new Rectangle(245+fw[j], 50+fh[i], fw[j+1]-fw[j], fh[i+1]-fh[i]);
                
                //set point for plant field
                plant_f[i][j] = new Point(296+j*81,117+i*98);
                Plant.setOcc(i, j);
            }
        }
    }
}
