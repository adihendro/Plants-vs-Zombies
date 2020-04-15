import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 

public class Pea{
    private int type;
    private int damage;
    private int x, y; //array for pea location [5][9]
    private int coorX, coorY; //pea coordinate
    private static Point[][] pea_f = new Point[5][9]; //array for pea coordinate
    private Clip clip, clip2;
    
    public Pea(int type, int x, int y){
        this.type=type;
        this.x=x;
        this.y=y;
        convert(x,y);
        if(type==2){ //Peashooter
        	damage=6;
        }
        else if(type==3){ //Repeater
        	damage=5;
        }

        try{
            // create clip reference 
            clip = AudioSystem.getClip();
            clip2 = AudioSystem.getClip();
            // open audioInputStream to the clip 
            clip.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/wav/Splat.wav")))); 
            clip2.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/wav/Shieldhit.wav")))); 
        }catch(Exception ex)  { 
            ex.printStackTrace();
        }
    }

    //static initialization block
    static{
        for(int i=0;i<5;i++){
            for(int j=0;j<9;j++){
                //set coordinate for pea field
                pea_f[i][j] = new Point(296+j*81 +28,117+i*98 -19);
            }
        }
    }

    //getter
    public int getCoorX(){return coorX;}
    public int getCoorY(){return coorY;}
    public int X(){return x;}
    public int Y(){return y;}
    public int getType(){return type;}
    public int getDamage(){return damage;}

    public void move(){
        coorX+=6; //pea speed = 6
    }
    public void convert(int i, int j){ //convert pea position to coordinate
        coorX=pea_f[i][j].getX();
        coorY=pea_f[i][j].getY();
    }
    public void splat(){ //play splat sound
        clip.start();
    }
    public void shieldhit(){ //play shieldhit sound
        clip2.start();
    }
}