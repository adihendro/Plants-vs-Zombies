import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.swing.Timer;

public class Zombie extends Actor implements Comparable<Zombie>{
    protected int zombieDamage;
    protected float zombieSpeed;
    private int[] column = {296,377,458,539,620,701,782,863,944}; //9
    private int type, lane, coorY, yp, id;
    private float coorX; //zombie x coordinate
    private static int[] arrY = new int[5]; //zombie y coordinate
    private static int n=0, max=50, interval;
    private static boolean gameOver=false;
    private static Timer timer; //spawning zombie timer
    private Timer timer2; //attacking plant timer
    private Clip clip, clip2;

    public Zombie(int type){
        this.type=type;
        coorX=1020f;
        coorY=arrY[setLane()];
        id=n;
        if(type==1){ //Normal zombie
            super.health=35;
            zombieDamage=10;
            zombieSpeed=0.3f;
            // zombieSpeed=5f;
        }else if(type==2) { //Football zombie
            super.health=60;
            zombieDamage=15;
            zombieSpeed=0.55f;
        }
    }

    //initialization block
    {
        //attacking plant
        timer2=new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                for(Plant plant: World.plants){
                    if(plant.X()==lane && plant.Y()==yp){ //intersect plant
                        if(!Audio.isEating() && !gameOver){
                            Audio.eat();
                        }
                        plant.hit(zombieDamage); //damage plant
                    }
                }
            }
        });
        timer2.setInitialDelay(200);

        try{
            // create clip reference 
            clip = AudioSystem.getClip(); 
            clip2 = AudioSystem.getClip(); 
            // open audioInputStream to the clip 
            clip.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Yuck.wav")))); 
            clip2.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Yuck2.wav")))); 
        }catch(Exception ex)  { 
            ex.printStackTrace();
        } 

    }

    //static initialization block
    static{
        for(int i=0;i<5;i++){
            arrY[i]=117+i*98-82;
        }
    }

    //init
    public static void start(int inter){
        interval=inter;
        timer=new Timer(interval*1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(n<max){
                    n++; //increase count zombie
                    World.zombies.add(new Zombie(setType())); //deploy zombie

                    //sort zombie based on lane
                    Collections.sort(World.zombies);
                }
            }
        });
        timer.start();
        timer.setDelay(interval*700);
    }
    public static void stop(){
        timer.stop(); //stop deploying zombie
    }

    @Override
	public int compareTo(Zombie z) { //sort zombies based on lane
		return lane-z.getLane();
	}

    //getter
    public static int getN(){return n;}
    public static int getMax(){return max;}
    public int getType(){return type;}
    public int getDamage(){return zombieDamage;}
    public int getHealth(){return health;}
    public int getId(){return id;}
    public float getCoorX(){return coorX;}
    public int getCoorY(){return coorY;}
    public int getLane(){return lane;}
    public int getColumn(){
        int c=9;
        for(int i=0;i<9;i++){
            if(coorX<=column[i] && coorX>column[i]-60){
                c=i;
            }
        }
        return c;
    }
    
    //setter
    public int setLane(){
        lane=(int)(Math.random() * 5); //generate zombie lane from 0 to 4
        return lane;
    }
    
    public static int setType(){
        if(n<=3){ //easy
            timer.setDelay(interval*550);
            return 1; //normal zombie
        }else if(n<=6){ //medium
            timer.setDelay(interval*200);
            if((int)(Math.random() * 4)==2){ //generate zombie type from 0 to 3
                return 2; //football zombie
            }else{
                return 1; //normal zombie
            }
        }else if(n<=20){ //hard
            timer.setDelay(interval*160);
            if(n==20){ //stop when 20 zombies are out
                timer.stop(); //wait for wave
            }
            if((int)(Math.random() * 2)==1){ //generate zombie type from 0 to 1
                return 2; //football zombie
            }else{
                return 1; //normal zombie
            }
        }else{ //(n<=max) extreme
            timer.setDelay(interval*90);
            if(n==21){
                Audio.siren(); //play siren audio
            }
            if((int)(Math.random() * 4)==1){ //generate zombie type from 0 to 3
                return 1; //normal zombie
            }else{
                return 2; //football zombie
            }
        }
    }
    
    public static void startWave(){ //start wave
        timer.setInitialDelay(5000);
        timer.start();
    }

    public static void resetN(){n=0;}
    
    public boolean gameOver(){
        if(coorX>210){ //zombie hasn't reach house yet
            return false;
        }else{ //zombie reaches house
            gameOver=true;
            return true;
        }
    }
    public void attack(){
        //check is zombie intersect plant
        yp=getColumn();
        if(Plant.getOcc(lane, yp)!=0){ //intersect plant
            A: for(Plant plant: World.plants){
                if(plant.X()==lane && plant.Y()==yp){
                    timer2.start();
                    if(plant.isDead()){ //plant dies
                        plant.stop(); //stop plant's activity
                        Plant.setOcc(lane, yp); //set spot to empty
                        timer2.stop(); //stop attacking plant
                        World.plants.remove(plant);
                        break A;
                    }
                }
            }
        }else{ //empty spot
            coorX-=zombieSpeed; //move
        }
    }
    public void stopEat(){
        timer2.stop(); //stop eating plant
    }

    public void yuck(){ //play yuck sound
        clip.start();
    }
    public void yuck2(){ //play yuck2 sound
        clip2.start();
    }
}