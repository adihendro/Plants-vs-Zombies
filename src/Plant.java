import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip;

public class Plant<T> extends Actor{
    private T type;
    private boolean idle=true, threaten=false, exploded=false;
    private Timer timer, timer2, timer3; //set timer
    private int x, y; //array for plant location [5][9]
    private int cw=74, ch=76; //cherrybomb
    private static int[][] occ = new int[5][10];
    private static Point[][] coor = new Point[5][9]; //array for plants coordinate
    private Clip clip, clip2;
    private Thread tcherry; //thread for waiting time
    
    public Plant(T type, int x, int y){
        this.type=type;
        this.x=x;
        this.y=y;
        if(type.equals(1)){ //Sunflower
            super.health = 50;
        }else if(type.equals(2)){ //Peashooter
            super.health = 50;
        }else if(type.equals(3)){ //Repeater
            super.health = 70;
        }else if(type.equals(4)){ //Wallnut
            super.health = 300;
        }else if(type.equals(5)){ //Cherrybomb
            super.health = 200;
            tcherry = new Thread(new CherryWaits()); 
            try{
                clip = AudioSystem.getClip();
                clip2 = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/wav/Cherry_enlarge.wav")))); 
                clip2.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/wav/Cherrybomb.wav")))); 
            }catch(Exception ex){ 
                ex.printStackTrace();
            }
        }else{}
    }

    //initialization block
    {
        //shoot pea every 2 seconds
        timer=new Timer(2000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                World.peas.add(new Pea((int)type, x, y));
            }
        });
        
        //repeater shoots second pea every 2.2 seconds
        timer2=new Timer(2000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                World.peas.add(new Pea(3, x, y));
            }
        });
        timer2.setInitialDelay(2200);

        //drop sun every 10 seconds
        timer3=new Timer(10000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                World.suns.add(new Sun(x, y));
            }
        });
    }

    //getter
    public int getX(){return x;}
    public int getY(){return y;}
    public T getType(){return type;}
    public int getHealth(){return health;}
    public boolean isThreaten(){return threaten;}
    public static int getOcc(int x, int y){return occ[x][y];}
    public static Point getCoor(int x, int y){return coor[x][y];}
    public boolean isIdle(){return idle;}
    
    //setter
    public static void setOcc(int i, int j){
        occ[i][j]=0;
    }
    public static void setCoor(int i, int j){
        coor[i][j]=new Point(296+j*81,117+i*98);
    }
    public void setThreat(boolean threat){
        threaten=threat;
    }

    public boolean put(int x, int y, T type){
        if(occ[x][y]==0){ //empty spot
            occ[x][y]=(int)type;
            World.plants.add(new Plant<Integer>((int)type, x, y));
            return true;
        }else{
            return false;
        }
    }
    public void attack(){
        timer.start();
        if(type.equals(3)){ //repeater
            timer2.start();
        }
        idle=false;
    }
    public void act(){
        timer3.start();
    }
    public void stop(){
        timer.stop();
        timer2.stop();
        timer3.stop();
        idle=true;
    }


    //cherrybomb
    //private class Threading
    private class CherryWaits implements Runnable { 
        public void run() { 
            try{
                Thread.sleep(800); //Exploded cherry waits for 800 milliseconds
            } catch (InterruptedException e) {}
        }
    } 
    public void startTimer(){
        tcherry.start();
    }

    public void enlarge(){
        cw+=1; ch+=1;
    }
    public int getCw(){return cw;}
    public int getCh(){return ch;}
    public boolean isExploded(){return exploded;}
    public boolean isTcherryAlive(){return tcherry.isAlive();}
    public void setExplode(){
        exploded=true;
    }
    public void cherry_enlarge(){ //play cherry_enlarge sound
        clip.start();
    }
    public void cherrybomb(){ //play cherrybomb sound
        clip.stop();
        clip2.start();
    }
}
