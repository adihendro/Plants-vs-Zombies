import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class World extends JPanel implements ActionListener{
    private static final long serialVersionUID = 1L;

    // width and height for (p)eashooter, (s)unflower, (r)epeater
    private int pwidth=62, pheight=66, swidth=pwidth, sheight=pheight+5, rwidth=pwidth+2, rheight=pheight+2;
    private Timer timer; //set timer

    //img: 0.background, 1.sun, 2.sunflower, 3.peashooter, 4.repeater, 5.sungif, 6.peagif, 7.repgif, 
    //8.zombie, 9.zombief, 10.pea, 11.wasted, 12.try again, 13.sun_g, 14.pea_g, 15.rep_g, 16.win, 17.play again
    private Image[] img = new Image[18];
    private Toolkit t = Toolkit.getDefaultToolkit();
    private Rectangle r_sunflower, r_peashooter, r_repeater, r_again, r_end; //rectangle for plants menu and others
    private Shape[][] field = new Shape[5][9]; //rectangle array with 5 rows and 9 columns for field area
    private Point mouse = new Point(); //point for mouse position
    public static Point[][] plant_f = new Point[5][9]; //array for plants coordinate
    private int xp, yp, i, j; //coordinate
    private boolean play=true, win=false, sun_clicked=false;

    private Player player;  
    private Plant<Integer> plant = new Plant<Integer>(0, 0, 0);
    private Pea pea;
    private Sun sun;

    public static List<Plant<Integer>> plants = new ArrayList<Plant<Integer>>();
    public static List<Sun> suns = new ArrayList<Sun>();
    public static List<Pea> peas = new ArrayList<Pea>();
    public static List<Zombie> zombies = new ArrayList<Zombie>();
      

    public World(){
        timer = new Timer(25, this); //set up timer for 25 milliseconds
        
        player = new Player();
        Sun.start(5);
        Zombie.start(10);
        
        getImg(); //load image from disk
        init();
        
        addMouseListener(new MListener()); //listen to mouse click
        addMouseMotionListener(new MouseMotionAdapter() { //listen to mouse motion
            public void mouseMoved(MouseEvent e) {
                mouse.setX(e.getX());
                mouse.setY(e.getY());
            }
        });
        
        Audio.begin();
        timer.start();
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        repaint();
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //draw background
        g.drawImage(img[0], 0, 0, 1024, 626, this);
        
        //draw black&white plant menu
        if(player.getCredits()<150){ //suncredits <150
            g.drawImage(img[15], 40, 433, rwidth+2, rheight+1, this); //draw repeater g
            if(player.getCredits()<100){ //suncredits <100
                g.drawImage(img[14], 41, 320, pwidth+2, pheight, this); //draw peashooter g
                if(player.getCredits()<50){ //suncredits <50
                    g.drawImage(img[13], 42, 196, swidth, sheight, this); //draw sunflower g
                }
            }
        }

        //draw sunflower points
        player.draw(g2);
        
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

        //make a transparent plant selection following mouse movement
        if(player.getChoice()==1){ //sunflower
            g2.setComposite(AlphaComposite.SrcOver.derive(0.7f)); //set alpha 0.7
            g2.drawImage(img[2], mouse.getX()-swidth/2, mouse.getY()-sheight/2, swidth, sheight, this);
            g2.setComposite(AlphaComposite.SrcOver.derive(1f)); //set alpha back to 1
        }else if(player.getChoice()==2){ //peashooter
            g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            g2.drawImage(img[3], mouse.getX()-pwidth/2, mouse.getY()-pheight/2, pwidth, pheight, this);
            g2.setComposite(AlphaComposite.SrcOver.derive(1f));
        }else if(player.getChoice()==3){ //repeater
            g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            g2.drawImage(img[4], mouse.getX()-(rwidth+8)/2, mouse.getY()-(rheight+8)/2, rwidth+4, rheight+4, this);
            g2.setComposite(AlphaComposite.SrcOver.derive(1f));
        }
        
        //zombie
        Iterator<Zombie> itz = zombies.iterator(); 
        while (itz.hasNext()){
            Zombie zombie=itz.next();
            
            //draw zombie
            if(zombie.getType()==1){ //standard zombie
                g.drawImage(img[8], Math.round(zombie.getCoorX()), zombie.getCoorY(), pwidth+11, pheight+53, this);
            }else if(zombie.getType()==2){ //football zombie
                g.drawImage(img[9], Math.round(zombie.getCoorX()), zombie.getCoorY(), this);
            }

            //check if zombie intersects plant
            zombie.attack();
            
            //check if zombie intersects pea
            Iterator<Pea> itp = peas.iterator(); 
            A: while (itp.hasNext()){
                pea=itp.next();
                xp=zombie.getLane(); //get zombie lane
                if(pea.X()==xp){ //same lane
                    if(zombie.getType()==1){ //normal zombie
                        if((pea.getCoorX()>=zombie.getCoorX()-3) && (pea.getCoorX()<=zombie.getCoorX()+92)){
                            pea.splat(); //play splat sound
                            zombie.hit(pea.getDamage()); //damage zombie
                            itp.remove(); //remove pea from list
                            break A;
                        }
                    }else if(zombie.getType()==2){ //football zombie
                        if((pea.getCoorX()>=zombie.getCoorX()+10) && (pea.getCoorX()<=zombie.getCoorX()+105)){
                            pea.splat(); //play splat sound
                            zombie.hit(pea.getDamage()); //damage zombie
                            itp.remove(); //remove pea from list
                            break A;
                        }
                    }
                }
            }

            //check if zombie is dead
            if(zombie.isDead()){
                itz.remove();
            }
            
            //check if zombie reaches house
            if(zombie.gameOver()){
                play=false;
            }
        }

        //check if all 40 zombies are killed
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
                    
                if(pea.getCoorX()>1030){ //pea move beyond the frame
                    itp.remove();
                }
            }
        
            //draw falling sun
            Iterator<Sun> its = suns.iterator(); 
            while (its.hasNext()){
                sun=its.next();
                if(sun.isSunflower()){ //sun from sunflower
                    if(sun.getY()<sun.getLimit()){ //sun waits a while until gone
                        g.drawImage(img[1],sun.getX(),sun.getY2(),80,80,this);
                        sun.setE(new Ellipse2D.Float(sun.getX(), sun.getY2(), 80, 80));
                        sun.lower();
                    }else{ //falling sun gone
                        its.remove();
                    }
                }else{ //sun from the sky
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
            player.setChoice(0);
            for(Plant plant: plants){
                plant.stop();
            }
            Zombie.stop();
            Sun.stop();
            suns.clear();
            peas.clear();

            if(win){
                Audio.win();
                r_end = new Rectangle(0, 0, 1024, 626);
                g2.setColor(Color.WHITE);
                g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
                g2.fill(r_end);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
                r_again = new Rectangle(445, 410, 140, 65);

                g.drawImage(img[16],263,120,500,250,this); //win text
                g.drawImage(img[17],445,410,140,65,this); //play again text
                
            }else{ //lose
                Audio.lose();
                r_end = new Rectangle(0, 0, 1024, 626);
                g2.setColor(Color.WHITE);
                g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
                g2.fill(r_end);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
                r_again = new Rectangle(395, 340, 240, 35);
                
                g.drawImage(img[11],365,120,this); //game over text
                g.drawImage(img[12],400,345,230,25,this); //try again text
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
                            sun.points(); //play points sound
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
                            player.setChoice((player.getChoice()==1) ? 0:1);
                        }
                    }else if(r_peashooter.contains(e.getPoint())) { //click peashooter
                        if(player.getCredits()>=100){ //>=100
                            player.setChoice((player.getChoice()==2) ? 0:2);
                        }
                    }else if(r_repeater.contains(e.getPoint())) { //click repeater
                        if(player.getCredits()>=150){ //>=150
                            player.setChoice((player.getChoice()==3) ? 0:3);
                        }
                    }else if(player.getChoice()!=0){ //to click field
                        A: for(i=0;i<5;i++){
                            for(j=0;j<9;j++){
                                if(field[i][j].contains(e.getPoint())){ //plant the plant in field
                                    if(plant.put(i,j,player.getChoice())){
                                        player.plant();
                                        player.setChoice(0);
                                        Audio.plant();
                                    }
                                    break A;
                                }
                            }
                        }
                        if(i==5){ //not selected a plant-able area
                            player.setChoice(0);
                        }
                    }
                }else{sun_clicked=false;}

            }else{ //the game is not playing
                if (r_again.contains(e.getPoint())) { //click try/play again
                    play=true;
                    win=false;
                    player.resetCredits();
                    plants.clear();
                    zombies.clear();
                    Zombie.resetN();
                    Audio.begin();

                    Sun.start(5);
                    Zombie.start(20);
                    for(i=0;i<5;i++){
                        for(j=0;j<9;j++){
                            Plant.setOcc(i, j);
                        }
                    }
                }
            }
        }
    }

    private void getImg(){
        try{ //try to load image and font
            img[0]=t.getImage(getClass().getResource("Assets/Background.jpg"));
            img[1]=t.getImage(getClass().getResource("Assets/Sun.png"));
            img[2]=t.getImage(getClass().getResource("Assets/Sunflower.png"));
            img[3]=t.getImage(getClass().getResource("Assets/Peashooter.png"));
            img[4]=t.getImage(getClass().getResource("Assets/Repeater.png"));
            img[5]=t.getImage(getClass().getResource("Assets/Sunflower.gif"));
            img[6]=t.getImage(getClass().getResource("Assets/Peashooter.gif"));
            img[7]=t.getImage(getClass().getResource("Assets/Repeater.gif"));
            img[8]=t.getImage(getClass().getResource("Assets/Zombie.gif"));
            img[9]=t.getImage(getClass().getResource("Assets/Zombief.gif"));
            img[10]=t.getImage(getClass().getResource("Assets/Pea.png"));
            img[11]=t.getImage(getClass().getResource("Assets/Wasted.png"));
            img[12]=t.getImage(getClass().getResource("Assets/Tryagain.png"));
            img[13]=t.getImage(getClass().getResource("Assets/Sunflower_g.png"));
            img[14]=t.getImage(getClass().getResource("Assets/Peashooter_g.png"));
            img[15]=t.getImage(getClass().getResource("Assets/Repeater_g.png"));
            img[16]=t.getImage(getClass().getResource("Assets/Win.png"));
            img[17]=t.getImage(getClass().getResource("Assets/Playagain.png"));
        } catch(Exception ex) {
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
