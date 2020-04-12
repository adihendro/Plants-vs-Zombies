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

    //img: 0.background, 1.sun, 2.sunflower, 3.peashooter, 4.repeater, 5.sungif, 6.peagif, 7.repgif, 8.zombie, 9.zombief 
    //10.pea_p, 11.wasted, 12.try again, 13.sun_g, 14.pea_g, 15.rep_g, 16.win, 17.play again, 18.brain, 19.pea_r, 
    //20.zombief2, 21.shovel, 22.shovel1, 23.shovel2, 24.progress1, 25.progress2, 26.progress3, 27.progress4
    private Image[] img = new Image[28];
    private Toolkit t = Toolkit.getDefaultToolkit();
    private Rectangle r_play, r_sunflower, r_peashooter, r_repeater, r_again, r_end; //rectangle for plants menu and others
    private Ellipse2D e_shovel; //ellipse for shovel
    private Shape[][] field = new Shape[5][9]; //rectangle array with 5 rows and 9 columns for field area
    private Point mouse = new Point(); //point for mouse position
    private int xp, yp, i, j; //coordinate
    private boolean start=false, play=true, win=false, end_sound=true, sun_clicked=false;

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

        try{ //load image
            img[0]=Toolkit.getDefaultToolkit().getImage(getClass().getResource("Assets/Menu.jpg"));
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString()); //show error dialog
        }

        addMouseListener(new MListener()); //listen to mouse click
        addMouseMotionListener(new MouseMotionAdapter() { //listen to mouse motion
            public void mouseMoved(MouseEvent e) {
                mouse.setX(e.getX());
                mouse.setY(e.getY());
            }
        });

        r_play = new Rectangle(445, 525, 135, 42);

        Audio.menu();
        timer.start();
    }


    public void start(){
        player = new Player();
        Sun.start(5);
        Zombie.start(5);
        
        getImg(); //load image from disk
        init();
        
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
        
        if(!start){
            g.drawImage(img[0], 0, 0, 1024, 626, this);
            
        }else{
            
            Graphics2D g2 = (Graphics2D) g;
            //draw background
            g.drawImage(img[0], 0, 0, 1024, 626, this);

            //draw progress
            xp = Math.round(4.625f*Zombie.getN());
            yp = Math.round(4.175f*Zombie.getN());
            g.drawImage(img[27], 795+185-xp, 588, xp, 16, this); //draw greenbar
            g.drawImage(img[26], 790, 572, 195, 40, this); //draw bar
            if(Zombie.getN() < Zombie.getMax()-4){
                g.drawImage(img[25], 790, 572, 241, 40, this); //draw flag
            }else{
                g.drawImage(img[25], 790, 560, 241, 40, this); //draw flag
            }
            g.drawImage(img[24], 952-yp, 574, 35, 38, this); //draw zombie head (785-952)
            
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
                xp=Plant.getCoor(plant.X(), plant.Y()).getX();
                yp=Plant.getCoor(plant.X(), plant.Y()).getY();

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
                    if(zombies.isEmpty()){ //there is no zombie
                        plant.setThreat(false);
                    }
                    if(!plant.isThreaten()){ //plant is not threaten: no zombie in front of plant
                        plant.stop();
                    }
                }
            }

            //draw transparent plant following mouse position
            if(player.getChoice()==1){ //sunflower
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f)); //set alpha to 0.7
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
                    if(zombie.getHealth()>=30){ //zombie uses helmet
                        g.drawImage(img[9], Math.round(zombie.getCoorX()), zombie.getCoorY(), this);
                    }else{ //zombie doesn't use helmet
                        g.drawImage(img[20], Math.round(zombie.getCoorX()), zombie.getCoorY(), this);
                    }
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
                                pea.shieldhit(); //play shieldhit sound
                                zombie.hit(pea.getDamage()); //damage zombie
                                itp.remove(); //remove pea from list
                                break A;
                            }
                        }
                    }
                }

                //check if zombie is dead
                if(zombie.isDead()){
                    zombie.stopEat();
                    itz.remove();
                }
                
                //check if zombie reaches house
                if(zombie.gameOver()){
                    play=false;
                }
            }

            //check if all zombies are killed
            if(Zombie.getN()==Zombie.getMax() && zombies.isEmpty()){
                play=false;
                win=true;
            }

            //draw shovel
            if(!player.getShovel()){ //if shovel is idle
                g.drawImage(img[22], 145, 520, 80, 80, this);
            }else{ //if shovel is taken
                g.drawImage(img[23], 145, 520, 80, 80, this);
                //draw shovel following mouse position
                g.drawImage(img[21], mouse.getX(), mouse.getY()-80, 80, 80, this);
            }
            
            if(play){
                //draw pea
                Iterator<Pea> itp = peas.iterator();
                while (itp.hasNext()){
                    pea=itp.next();
                    if(pea.getType()==2){ //peashooter
                        g.drawImage(img[10], pea.getCoorX(), pea.getCoorY(), this);
                    }else{ //repeater
                        g.drawImage(img[19], pea.getCoorX(), pea.getCoorY(), this);
                    }
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
                        }else if(sun.getY()<(sun.getLimit()+300)){ //sun waits a while until gone
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
                    if(end_sound){
                        Audio.win(); //play win sound
                        end_sound=false;
                    }
                    g2.setColor(Color.WHITE);
                    g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
                    g2.fill(r_end);
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f));
                    r_again = new Rectangle(442, 410, 140, 65);

                    g.drawImage(img[16],263,130,500,250,this); //win image
                    g.drawImage(img[17],442,410,140,65,this); //play again image
                    
                }else{ //lose
                    if(end_sound){
                        Audio.lose(); //play lose sound
                        end_sound=false;
                    }
                    g2.setColor(Color.WHITE);
                    g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
                    g2.fill(r_end);
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f));
                    r_again = new Rectangle(400, 395, 220, 45);
                    
                    g.drawImage(img[18],425,85,180,210,this); //brain image
                    g.drawImage(img[11],365,190,this); //lose image
                    g.drawImage(img[12],410,405,200,25,this); //try again image
                }
            }

        }
        g.dispose();
    }
    

    private class MListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) { //if mouse pressed
            if(!start){
                if (r_play.contains(e.getPoint())) { //click play
                    Audio.evillaugh();
                    start=true;
                    r_play=null;
                    start();
                }
            }else{

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
                    if(!sun_clicked){ //sun is not clicked
                        // check if mouse clicked plants
                        if(r_sunflower.contains(e.getPoint())) { //click sunflower
                            if(player.getCredits()>=50){ //>=50
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice((player.getChoice()==1) ? 0:1);
                            }else{
                                Audio.buzzer(); //play buzzer sound
                            }
                        }else if(r_peashooter.contains(e.getPoint())) { //click peashooter
                            if(player.getCredits()>=100){ //>=100
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice((player.getChoice()==2) ? 0:2);
                            }else{
                                Audio.buzzer(); //play buzzer sound
                            }
                        }else if(r_repeater.contains(e.getPoint())) { //click repeater
                            if(player.getCredits()>=150){ //>=150
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice((player.getChoice()==3) ? 0:3);
                            }else{
                                Audio.buzzer(); //play buzzer sound
                            }
                        }else if(player.getChoice()!=0){ //to click field
                            A: for(i=0;i<5;i++){
                                for(j=0;j<9;j++){
                                    if(field[i][j].contains(e.getPoint())){ //plant the plant in field
                                        if(plant.put(i,j,player.getChoice())){
                                            Audio.plant(); //play plant sound
                                            player.plant();
                                            player.setChoice(0);
                                        }
                                        break A;
                                    }
                                }
                            }
                            if(i==5){ //not selected a plant-able area
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice(0);
                            }
                        }
                    }else{sun_clicked=false;}

                    //check shovel
                    if(player.getShovel()){
                        A: for(i=0;i<5;i++){
                            for(j=0;j<9;j++){
                                if(field[i][j].contains(e.getPoint())){ //click field
                                    if(Plant.getOcc(i, j)!=0){ //plant exist
                                        Plant.setOcc(i, j); //remove plant

                                        for(Plant plant: plants){
                                            if(plant.X()==i && plant.Y()==j){
                                                plant.stop(); //stop plant's activity
                                                Audio.remove(); //play remove sound
                                                plants.remove(plant);
                                                break A;
                                            }
                                        }
                                    }
                                    break A; //field empty
                                }
                            }
                        }
                        player.setShovel(false);

                    }else if(e_shovel.contains(e.getPoint())){ //click shovel
                        player.setShovel(true);
                        Audio.shovel(); //play shovel sound
                    }

                }else{ //the game is not playing
                    if (r_again.contains(e.getPoint())) { //click try/play again
                        play=true;
                        win=false;
                        player.resetCredits();
                        plants.clear();
                        zombies.clear();
                        Zombie.resetN();
                        end_sound=true;
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
    }


    private void getImg(){
        try{ //load image
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
            img[10]=t.getImage(getClass().getResource("Assets/Pea_p.png"));
            img[11]=t.getImage(getClass().getResource("Assets/Wasted.png"));
            img[12]=t.getImage(getClass().getResource("Assets/Tryagain.png"));
            img[13]=t.getImage(getClass().getResource("Assets/Sunflower_g.png"));
            img[14]=t.getImage(getClass().getResource("Assets/Peashooter_g.png"));
            img[15]=t.getImage(getClass().getResource("Assets/Repeater_g.png"));
            img[16]=t.getImage(getClass().getResource("Assets/Win.png"));
            img[17]=t.getImage(getClass().getResource("Assets/Playagain.png"));
            img[18]=t.getImage(getClass().getResource("Assets/Brain.png"));
            img[19]=t.getImage(getClass().getResource("Assets/Pea_r.png"));
            img[20]=t.getImage(getClass().getResource("Assets/Zombief2.gif"));
            img[21]=t.getImage(getClass().getResource("Assets/Shovel.png"));
            img[22]=t.getImage(getClass().getResource("Assets/Shovel1.png"));
            img[23]=t.getImage(getClass().getResource("Assets/Shovel2.png"));
            img[24]=t.getImage(getClass().getResource("Assets/Progress1.png"));
            img[25]=t.getImage(getClass().getResource("Assets/Progress2.png"));
            img[26]=t.getImage(getClass().getResource("Assets/Progress3.png"));
            img[27]=t.getImage(getClass().getResource("Assets/Progress4.png"));
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString()); //show error dialog
        }
    }

    private void init(){
        //create rectangle for plant menu and end game
        r_sunflower = new Rectangle(30, 184, pwidth+26, pheight+56);
        r_peashooter = new Rectangle(30, 190+pheight+50, pwidth+26, pheight+52);
        r_repeater = new Rectangle(30, 190+2*pheight+95, pwidth+26, pheight+55);
        r_end = new Rectangle(0, 0, 1024, 626);
        
        //create ellipse for shovel
        e_shovel = new Ellipse2D.Float(145, 520, 80, 80);

        //create rectangle clickable area for field
        int[] fw = {0,90,165,250,330,410,492,570,651,749}; //field width
        int[] fh = {0,118,215,323,405,516}; //field height
        for(i=0;i<5;i++){
            for(j=0;j<9;j++){
                //set rectangle field area
                field[i][j] = new Rectangle(245+fw[j], 50+fh[i], fw[j+1]-fw[j], fh[i+1]-fh[i]);
                
                //set plant field
                Plant.setOcc(i, j);
                Plant.setCoor(i, j);
            }
        }
    }
}
