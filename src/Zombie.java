import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Zombie extends Actor{
    protected int zombieDamage;
<<<<<<< HEAD
    protected double zombieSpeed;
=======
    protected float zombieSpeed;
    private int[] column = {296,377,458,539,620,701,782,863,944}; //9
    private int type, lane, coorY, yp, interval;
    private float coorX; //zombie x coordinate
    private static int[] arrY = new int[5];
    private static int n=0;
    private Timer timer, timer2; //set timer
>>>>>>> 7691f8c9e1f1a1cd630700dde2bb56ec98d862d5

    public Zombie(int type){
        this.type=type;
        coorX=1020f;
        coorY=arrY[setLane()];
        if(type==1){ //Normal zombie
            super.health=40;
            zombieDamage=10;
<<<<<<< HEAD
            zombieSpeed=0.5;
        }else { //Football zombie
            health=80;
=======
            zombieSpeed=0.35f;
        }else if(type==2) { //Football zombie
            super.health=60;
>>>>>>> 7691f8c9e1f1a1cd630700dde2bb56ec98d862d5
            zombieDamage=15;
            zombieSpeed=0.6f;
        }
    }

    static{
        for(int i=0;i<5;i++){
            arrY[i]=117+i*98-82;
        }
    }

    {
        timer2=new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                for(Plant plant: World.plants){
                    if(plant.X()==lane && plant.Y()==yp){ //intersect plant
                        plant.hit(zombieDamage);
                    }
                }
            }
        });
    }

    public void start(int interval){
        this.interval=interval;
        timer=new Timer(interval*1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(n<40){
                    n++;
                    World.zombies.add(new Zombie(setType()));
                }
            }
        });
        timer.start();
    }

    public void stop(){
        timer.stop();
    }

    //getter
    public static int getN(){return n;}
    public int getType(){return type;}
    public int getDamage(){return zombieDamage;}
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
    public static void resetN(){n=0;}
    public int setLane(){
        lane=(int)(Math.random() * 5); //generate zombie lane from 0 to 4
        return lane;
    }

    public int setType(){
        if(n<=3){ //easy
            if(n>=1){
                timer.setDelay(interval*500);
            }
            return 1; //normal zombie
        }else if(n<=6){ //medium
            timer.setDelay(interval*170);
            if((int)(Math.random() * 4)==2){ //generate zombie type from 0 to 3
                return 2; //football zombie
            }else{
                return 1; //normal zombie
            }
        }else if(n<=15){ //hard
            timer.setDelay(interval*140);
            if((int)(Math.random() * 3)==2){ //generate zombie type from 0 to 2
                return 2; //football zombie
            }else{
                return 1; //normal zombie
            }
        }else{ //n<40 extreme
            timer.setDelay(interval*100);
            if((int)(Math.random() * 4)==1){ //generate zombie type from 0 to 3
                return 1; //normal zombie
            }else{
                return 2; //football zombie
            }
        }
    }

<<<<<<< HEAD
    //getter

    public double getzombieSpeed(){
        return(zombieSpeed);
    }
    
    // public void setSpeed(int speed){
    //     if(isTouching(Plant.class)){
    //         zombieSpeed=0;
    //     } else {
    //         zombieSpeed =speed;
    //     }
    // }
=======
    public void move(){
        coorX-=zombieSpeed;
    }
    public boolean gameOver(){
        return coorX<210;
    }
    public void attack(){
        //check is zombie intersect plant
        yp=getColumn();
        if(Plant.getOcc(lane, yp)!=0){
            A: for(Plant plant: World.plants){
                if(plant.X()==lane && plant.Y()==yp){ //intersect plant
                    timer2.start();
                    if(plant.isDead()){
                        plant.stop();
                        Plant.setOcc(lane, yp);
                        timer2.stop();
                        World.plants.remove(plant);
                        break A;
                    }
                }
            }
        }else{
            move();
        }
    }
    public void hit(int damage){
        super.health-=damage;
    }
>>>>>>> 7691f8c9e1f1a1cd630700dde2bb56ec98d862d5
}