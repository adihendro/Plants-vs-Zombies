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
    //28.hugewave, 29.finalwave, 30.cherry, 31.powie, 32.cherry_g
    private Image[] img = new Image[33];
    private Toolkit t = Toolkit.getDefaultToolkit();
    //rec: 0.r_play, 1.r_again, 2.r_end, 3.r_sunflower, 4.r_peashooter, 5.r_repeater, 6.r_wallnut, 7.r_cherrybomb
    private Rectangle[] rec = new Rectangle[8]; //rectangle for menu and others
    private Ellipse2D e_shovel; //ellipse for shovel
    private Shape[][] field = new Shape[5][9]; //rectangle array with 5 rows and 9 columns for field area
    private Point mouse = new Point(); //point for mouse position
    private int xp, yp, i, j; //coordinate
    private static int wave=0; //zombies wave
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

        try{ //load image menu
            img[0]=Toolkit.getDefaultToolkit().getImage(getClass().getResource("Assets/image/Menu.jpg"));
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot open image!"); //show error dialog
        }

        addMouseListener(new MListener()); //listen to mouse click
        addMouseMotionListener(new MouseMotionAdapter() { //listen to mouse motion
            public void mouseMoved(MouseEvent e) {
                mouse.setX(e.getX());
                mouse.setY(e.getY());
            }
        });

        rec[0] = new Rectangle(445, 525, 135, 42);

        Audio.menu();
        timer.start();
    }


    public void start(){
        player = new Player();
        Sun.start(5);
        Zombie.start(10);
        
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
            xp = Math.round((205.0f/Zombie.getMax())*Zombie.getN());
            yp = Math.round((190.0f/Zombie.getMax())*Zombie.getN());
            g.drawImage(img[27], 498+205-xp, 586, xp, 16, this); //draw greenbar (max 205 pixels)
            g.drawImage(img[26], 490, 570, 215, 40, this); //draw bar
            if(Zombie.getN() <= Zombie.getMax()-5){
                g.drawImage(img[25], 489, 562, 261, 49, this); //draw flag
            }else{ //raise flag for the last 5 zombies
                g.drawImage(img[25], 489, 550+Math.round((12.0f/5)*(Zombie.getMax()-Zombie.getN())), 261, 49, this);
            }
            g.drawImage(img[24], 675-yp, 572, 35, 38, this); //draw zombie head (485 to 675; max 190 pixels)
            
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
            Iterator<Plant<Integer>> itpl = plants.iterator(); 
            while (itpl.hasNext()){
                plant=itpl.next();

                xp=Plant.getCoor(plant.getX(),plant.getY()).getX(); //x coordinate
                yp=Plant.getCoor(plant.getX(),plant.getY()).getY(); //y coordinate

                if(plant.getType().equals(1)){ //sunflower gif
                    g.drawImage(img[5], xp-swidth/2, yp-sheight/2, swidth, sheight, this);
                    plant.act();

                }else if(plant.getType().equals(4)){ //wallnut gif

                    
                }else if(plant.getType().equals(5)){ //cherrybomb
                    if(plant.getCw()<110){ //enlarge
                        g.drawImage(img[30], xp-plant.getCw()/2-4, yp-plant.getCh()/2-4, plant.getCw(), plant.getCh(), this);
                        plant.enlarge();
                        plant.cherry_enlarge(); //play cherry_enlarge sound
                    }else{ //explode
                        i=plant.getX();
                        j=plant.getY();
                        if(!plant.isExploded()){
                            plant.cherrybomb(); //play cherrybomb sound
                            plant.setExplode(); //set explode to true
                            plant.startTimer(); //start waiting thread
                            
                            //kill zombie
                            Iterator<Zombie> itz = zombies.iterator(); 
                            while (itz.hasNext()){
                                Zombie zombie=itz.next();
                                if(zombie.getLane()<=(i+1) && zombie.getLane()>=(i-1) 
                                && zombie.getColumn()<=(j+1) && zombie.getColumn()>=(j-1)){ //zombies around plant 3x3
                                    zombie.stopEat(); //stop eating plant
                                    itz.remove();
                                }
                            }
                        }
                        if(plant.isTcherryAlive()){ //waiting thread running
                            g.drawImage(img[31], xp-150, yp-125, 300, 250, this);
                        }else{ //remove explosion
                            Plant.setOcc(i, j); //set spot to empty
                            itpl.remove();
                        }
                    }      

                }else{
                    if(plant.getType().equals(2)){ //peashooter gif
                        g.drawImage(img[6], xp-(pwidth+2)/2, yp-(pheight+2)/2, pwidth+2, pheight+2, this);
                    }else if(plant.getType().equals(3)){ //repeater gif
                        g.drawImage(img[7], xp-(rwidth+20)/2, yp-(rheight+9)/2, rwidth+26, rheight+13, this);
                    }

                    //shoot zombie
                    xp=plant.getX(); //field row
                    yp=plant.getY(); //field column

                    A: for(Zombie zombie: zombies){
                        if(xp==zombie.getLane() && yp<=zombie.getColumn()){ //zombies in front of plant
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
                Iterator<Pea> itpea = peas.iterator(); 
                A: while (itpea.hasNext()){
                    pea=itpea.next();
                    xp=zombie.getLane(); //get zombie lane
                    if(pea.getX()==xp){ //same lane
                        if(zombie.getType()==1){ //normal zombie
                            if((pea.getCoorX()>=zombie.getCoorX()-3) && (pea.getCoorX()<=zombie.getCoorX()+92)){
                                pea.splat(); //play splat sound
                                zombie.hit(pea.getDamage()); //damage zombie
                                itpea.remove(); //remove pea from list
                                break A;
                            }
                        }else if(zombie.getType()==2){ //football zombie
                            if((pea.getCoorX()>=zombie.getCoorX()+10) && (pea.getCoorX()<=zombie.getCoorX()+105)){
                                pea.shieldhit(); //play shieldhit sound
                                zombie.hit(pea.getDamage()); //damage zombie
                                itpea.remove(); //remove pea from list
                                break A;
                            }
                        }
                    }
                }

                //check if zombie is dead
                if(zombie.isDead()){
                    zombie.stopEat(); //stop eating plant
                    if(zombie.getType()==2){ //football zombie
                        zombie.yuck2();
                    }else{
                        zombie.yuck();
                    }
                    itz.remove();
                }
                
                //check if zombie reaches house
                if(zombie.gameOver()){
                    play=false;
                }
            }

            //check if all zombies before wave are dead
            if(wave==0 && Zombie.getN()==Zombie.getWave() && zombies.isEmpty()){
                Zombie.startWave(); //start wave
            }

            //check if all zombies are dead
            if(Zombie.getN()==Zombie.getMax() && zombies.isEmpty()){
                play=false;
                win=true;
            }

            //draw shovel
            if(!player.getShovel()){ //if shovel is idle
                g.drawImage(img[22], 138, 525, 80, 80, this);
            }else{ //if shovel is taken
                g.drawImage(img[23], 138, 525, 80, 80, this);
                //draw shovel following mouse position
                g.drawImage(img[21], mouse.getX(), mouse.getY()-80, 80, 80, this);
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
            }else if(player.getChoice()==4){ //wallnut
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
                g2.drawImage(img[4], mouse.getX()-(rwidth+8)/2, mouse.getY()-(rheight+8)/2, rwidth+4, rheight+4, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            }else if(player.getChoice()==5){ //cherrybomb
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
                g2.drawImage(img[30], mouse.getX()-40, mouse.getY()-41, 80, 82, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            }
            
            if(play){
                //draw pea
                Iterator<Pea> itpea = peas.iterator();
                while (itpea.hasNext()){
                    pea=itpea.next();
                    if(pea.getType()==2){ //peashooter
                        g.drawImage(img[10], pea.getCoorX(), pea.getCoorY(), this);
                    }else{ //repeater
                        g.drawImage(img[19], pea.getCoorX(), pea.getCoorY(), this);
                    }
                    pea.move();
                        
                    if(pea.getCoorX()>1030){ //pea move beyond the frame
                        itpea.remove();
                    }
                }
            
                //draw falling sun
                Iterator<Sun> its = suns.iterator(); 
                while (its.hasNext()){
                    sun=its.next();
                    if(sun.isSunflower()){ //sun from sunflower
                        if(!sun.isWaiting()){ //if the sun is not waiting
                            sun.startTimer(); //start waiting thread
                            sun.setWaiting(); //set waiting variable to true
                        }
                        if(sun.isTsunAlive()){ //waiting thread running
                            g.drawImage(img[1],sun.getX(),sun.getY(),80,80,this);
                            sun.setE(new Ellipse2D.Float(sun.getX(), sun.getY(), 80, 80));
                        }else{ //remove sunflower sun
                            its.remove();
                        }
                    }else{ //sun from the sky
                        if(sun.getY()<sun.getLimit()){ //sun falls
                            g.drawImage(img[1],sun.getX(),sun.getY(),80,80,this);
                            sun.setE(new Ellipse2D.Float(sun.getX(), sun.getY(), 80, 80));
                            sun.lower();
                        }else if(sun.getY()<(sun.getLimit()+300)){ //sun waits a while until gone
                            if(!sun.isWaiting()){ //if the sun is not waiting
                                sun.startTimer(); //start waiting thread
                                sun.setWaiting(); //set waiting variable to true
                            }
                            if(sun.isTsunAlive()){ //waiting thread running
                                g.drawImage(img[1],sun.getX(),sun.getY(),80,80,this);
                                sun.setE(new Ellipse2D.Float(sun.getX(), sun.getY(), 80, 80));
                            }else{ //remove falling sun
                                its.remove();
                            }
                        }
                    }
                }

                //wave
                if(wave==1){ //a huge wave of zombies is approaching
                    g.drawImage(img[28], 160, 290, 743, 42, this);
                }else if(wave==2){ //final wave
                    g.drawImage(img[29], 380, 280, 300, 61, this);
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
                    g2.fill(rec[2]);
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f));
                    rec[1] = new Rectangle(442, 410, 140, 65);

                    g.drawImage(img[16],263,130,500,250,this); //win image
                    g.drawImage(img[17],442,410,140,65,this); //play again image
                    
                }else{ //lose
                    if(end_sound){
                        Audio.lose(); //play lose sound
                        end_sound=false;
                    }
                    g2.setColor(Color.WHITE);
                    g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
                    g2.fill(rec[2]);
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f));
                    rec[1] = new Rectangle(400, 395, 220, 45);
                    
                    g.drawImage(img[18],425,85,180,210,this); //brain image
                    g.drawImage(img[11],365,190,this); //lose image
                    g.drawImage(img[12],410,405,200,25,this); //try again image
                }
            }

            g2.setColor(Color.WHITE);
            g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
            g2.fill(rec[6]);
            g2.setComposite(AlphaComposite.SrcOver.derive(1f));

            g2.setColor(Color.WHITE);
            g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
            g2.fill(rec[7]);
            g2.setComposite(AlphaComposite.SrcOver.derive(1f));

        }

        g.dispose();
    }
    

    private class MListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) { //if mouse pressed
            if(!start){
                if (rec[0].contains(e.getPoint())) { //click play
                    Audio.evillaugh();
                    start=true;
                    rec[0]=null;
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
                        if(rec[3].contains(e.getPoint())) { //click sunflower
                            if(player.getCredits()>=50){
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice((player.getChoice()==1) ? 0:1);
                            }else{
                                Audio.buzzer(); //play buzzer sound
                            }
                        }else if(rec[4].contains(e.getPoint())) { //click peashooter
                            if(player.getCredits()>=100){
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice((player.getChoice()==2) ? 0:2);
                            }else{
                                Audio.buzzer(); //play buzzer sound
                            }
                        }else if(rec[5].contains(e.getPoint())) { //click repeater
                            if(player.getCredits()>=150){
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice((player.getChoice()==3) ? 0:3);
                            }else{
                                Audio.buzzer(); //play buzzer sound
                            }
                        }else if(rec[6].contains(e.getPoint())) { //click wallnut
                            if(player.getCredits()>=50){
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice((player.getChoice()==4) ? 0:4);
                            }else{
                                Audio.buzzer(); //play buzzer sound
                            }
                        }else if(rec[7].contains(e.getPoint())) { //click cherrybomb
                            if(player.getCredits()>=150){
                                Audio.seedlift(); //play seedlift sound
                                player.setChoice((player.getChoice()==5) ? 0:5);
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
                                        }
                                        player.setChoice(0);
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
                                            if(plant.getX()==i && plant.getY()==j){
                                                plant.stop(); //stop plant's activity
                                                Audio.remove(); //play remove sound
                                                plants.remove(plant);
                                                break A;
                                            }
                                        }

                                        for(Zombie zombie: zombies){
                                            if(zombie.getLane()==i && zombie.getColumn()==j){
                                                zombie.stopEat(); //stop attacking plant
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
                    if (rec[1].contains(e.getPoint())) { //click try/play again
                        play=true;
                        win=false;
                        end_sound=true;
                        player.resetCredits();
                        plants.clear();
                        zombies.clear();
                        Zombie.resetN();
                        Audio.begin();

                        Sun.start(5);
                        Zombie.start(18);
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
            img[0]=t.getImage(getClass().getResource("Assets/image/Background.jpg"));
            img[1]=t.getImage(getClass().getResource("Assets/image/Sun.png"));
            img[2]=t.getImage(getClass().getResource("Assets/image/Sunflower.png"));
            img[3]=t.getImage(getClass().getResource("Assets/image/Peashooter.png"));
            img[4]=t.getImage(getClass().getResource("Assets/image/Repeater.png"));
            img[5]=t.getImage(getClass().getResource("Assets/gif/Sunflower.gif"));
            img[6]=t.getImage(getClass().getResource("Assets/gif/Peashooter.gif"));
            img[7]=t.getImage(getClass().getResource("Assets/gif/Repeater.gif"));
            img[8]=t.getImage(getClass().getResource("Assets/gif/Zombie.gif"));
            img[9]=t.getImage(getClass().getResource("Assets/gif/Zombief.gif"));
            img[10]=t.getImage(getClass().getResource("Assets/image/Pea_p.png"));
            img[11]=t.getImage(getClass().getResource("Assets/image/Wasted.png"));
            img[12]=t.getImage(getClass().getResource("Assets/image/Tryagain.png"));
            img[13]=t.getImage(getClass().getResource("Assets/image/Sunflower_g.png"));
            img[14]=t.getImage(getClass().getResource("Assets/image/Peashooter_g.png"));
            img[15]=t.getImage(getClass().getResource("Assets/image/Repeater_g.png"));
            img[16]=t.getImage(getClass().getResource("Assets/image/Win.png"));
            img[17]=t.getImage(getClass().getResource("Assets/image/Playagain.png"));
            img[18]=t.getImage(getClass().getResource("Assets/image/Brain.png"));
            img[19]=t.getImage(getClass().getResource("Assets/image/Pea_r.png"));
            img[20]=t.getImage(getClass().getResource("Assets/gif/Zombief2.gif"));
            img[21]=t.getImage(getClass().getResource("Assets/image/Shovel.png"));
            img[22]=t.getImage(getClass().getResource("Assets/image/Shovel1.png"));
            img[23]=t.getImage(getClass().getResource("Assets/image/Shovel2.png"));
            img[24]=t.getImage(getClass().getResource("Assets/image/Progress1.png"));
            img[25]=t.getImage(getClass().getResource("Assets/image/Progress2.png"));
            img[26]=t.getImage(getClass().getResource("Assets/image/Progress3.png"));
            img[27]=t.getImage(getClass().getResource("Assets/image/Progress4.png"));
            img[28]=t.getImage(getClass().getResource("Assets/image/HugeWave.png"));
            img[29]=t.getImage(getClass().getResource("Assets/image/FinalWave.png"));
            img[30]=t.getImage(getClass().getResource("Assets/image/Cherry.png"));
            img[31]=t.getImage(getClass().getResource("Assets/image/Powie.png"));
            img[32]=t.getImage(getClass().getResource("Assets/image/Cherry_g.png"));
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot open image!"); //show error dialog
        }
    }

    private void init(){    
        //create rectangle for plant menu and end game
        rec[3] = new Rectangle(30, 184, pwidth+26, pheight+56); //sunflower
        rec[4] = new Rectangle(30, 190+pheight+50, pwidth+26, pheight+52); //peashooter
        rec[5] = new Rectangle(30, 190+2*pheight+95, pwidth+26, pheight+55); //repeater
        rec[6] = new Rectangle(130, 190+pheight+50, pwidth+26, pheight+55); //wallnut
        rec[7] = new Rectangle(130, 190+2*pheight+95, pwidth+26, pheight+55); //cherrybomb
        rec[2] = new Rectangle(0, 0, 1024, 626); //end
        
        //create ellipse for shovel
        e_shovel = new Ellipse2D.Float(138, 525, 80, 80);

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

    public static void setWave(int w){
        wave=w;
    }
}
