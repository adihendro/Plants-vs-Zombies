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
    private int type, lane, coorY, yp;
    private float coorX; //zombie x coordinate
    private static int[] arrY = new int[5]; //zombie y coordinate
    private static int n=0, max=50, interval, wave=20;
    private static boolean gameOver=false;
    private static Timer timer; //spawning zombie timer
    private Timer timer2; //attacking plant timer
    private Clip clip, clip2;

    public Zombie(int type){
        this.type=type;
        coorX=1020f;
        coorY=arrY[setLane()];
        if(type==1){ //Normal zombie
            super.health=45;
            zombieDamage=10;
            zombieSpeed=0.3f;
            // zombieSpeed=5f;
        }else if(type==2){ //Football zombie
            super.health=80;
            zombieDamage=15;
            zombieSpeed=0.55f;
        } else if (type==3){//Flying zombie
	        super.health=65;
            zombieSpeed=0.45f;
        }
    }

    //initialization block
    {
        //attacking plant
        timer2=new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                for(Plant plant: World.plants){
                    if(plant.getX()==lane && plant.getY()==yp){ //intersect plant
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
            clip.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/wav/Yuck.wav")))); 
            clip2.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/wav/Yuck2.wav")))); 
        }catch(Exception ex)  { 
            ex.printStackTrace();
        } 

    }

    //static initialization block
    static{
        for(int i=0;i<5;i++){
            arrY[i]=117+i*98-82; //initialize zombies y coordinate
        }
    }

    //spawning zombie automatically
    public static void start(int inter){
        interval=inter;
        timer=new Timer(interval*1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(n<max){
                    n++; //increase count zombie
                    World.zombies.add(new Zombie(setType())); //deploy zombie
                    //sort zombie based on lane
                    Collections.sort(World.zombies);
                    
                    playAudio();
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
    public static int getWave(){return wave;}
    public int getType(){return type;}
    public int getDamage(){return zombieDamage;}
    public int getHealth(){return health;}
    public float getCoorX(){return coorX;}
    public int getCoorY(){return coorY;}
    public int getLane(){return lane;}
    
    public int getColumn(){ //convert x coordinate to field column
        int c=9;
        if(type==2){ //football zombie
            A: for(int i=8;i>=1;i--){
                if(coorX<=column[i]-18 && coorX>column[i-1]-18){
                    c=i;
                    break A;
                }else if(coorX<=column[0]-18){
                    c=0;
                }
            }
        }else{
            A: for(int i=8;i>=1;i--){
                if(coorX<=column[i] && coorX>column[i-1]){
                    c=i;
                    break A;
                }else if(coorX<=column[0]){
                    c=0;
                }
            }
        }
        return c;
    }
   
    public int getColumnEat(){ //convert x coordinate to field column for attacking plant
        int c=9;
        if(type==2){ //football zombie
            A: for(int i=8;i>=0;i--){
                if(coorX<=column[i]-18 && coorX>column[i]-72){
                    c=i;
                    break A;
                }
            }
        }else{
            A: for(int i=8;i>=0;i--){
                if(coorX<=column[i] && coorX>column[i]-60){
                    c=i;
                    break A;
                }
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
            return 2; //normal zombie
        }else if(n<=6){ //medium
            timer.setDelay(interval*200);
            if((int)(Math.random() * 4)==2){ //generate zombie type from 0 to 3
                return 2; //football zombie
            }else{
                return 1; //normal zombiee
            }
        }else if(n<=wave){ //hard
            timer.setDelay(interval*160);
            if(n==wave){ //stop when wave zombies are about to come
                timer.stop(); //wait for wave
            }
            if((int)(Math.random() * 2)==1){ //generate zombie type from 0 to 1
                return 2; //football zombie
            }else{
                return 1; //normal zombie
            }
        }else{ //(n<=max) extreme
            timer.setDelay(interval*80);
            if((int)(Math.random() * 4)==1){ //generate zombie type from 0 to 3
                return 1; //normal zombie
            }else{
                return 2; //football zombie
            }
        }
    }
    
    public static void startWave(){ //start wave
        Audio.wave(); //play wave audio
        timer.setInitialDelay(5000);
        timer.start();
        World.setWave(1);
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
        //check if zombie intersects plant
        yp=getColumnEat();
        if(Plant.getOcc(lane, yp)!=0){ //intersect plant
        // if(Plant.getOcc(lane, yp)!=0 && Plant.getOcc(lane, yp)!=5){ //intersect plant (excluding cherrybomb)
            A: for(Plant plant: World.plants){
                if(plant.getX()==lane && plant.getY()==yp){
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
        }else{ //field empty
            move();
        }
    }
    public void move(){
        coorX-=zombieSpeed; //move
    }
    public void stopEat(){
        timer2.stop(); //stop eating plant
    }
    

    //Audio
    public static void playAudio(){
        if(n==1){
            Audio.groan1();
        }else if(n==2){
            Audio.brain1();
        }else if(n==3){
            Audio.groan2();
        }else if(n==5){
            Audio.brain2();
        }else if(n==6){
            Audio.brain3();
        }else if(n==8){
            Audio.groan3();
        }else if(n==10){
            Audio.groan2();
        }else if(n==15){
            Audio.brain1();
        }else if(n==18){
            Audio.groan1();
        }else if(n==20){
            Audio.groan2();
        }

        if(n==wave+1){
            Audio.siren(); //play siren audio
            World.setWave(2);
        }else if(n==wave+2){
            Audio.brain1();
            World.setWave(0);
        }else if(n==wave+5){
            Audio.groan3();
        }else if(n==wave+8){
            Audio.groan1();
        }
    }
    public void yuck(){ //play yuck sound
        clip.start();
    }
    public void yuck2(){ //play yuck2 sound
        clip2.start();
    }
}
