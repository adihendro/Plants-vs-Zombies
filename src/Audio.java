import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Audio{
    private static Clip[] clip = new Clip[10]; 
    private static Timer timer; //set timer

    static{
        try{
            // create clip reference 
            for(int i=0;i<10;i++){
                clip[i] = AudioSystem.getClip(); 
            }
            // open audioInputStream to the clip 
            clip[0].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Menu.wav")))); 
            clip[1].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Background.wav")))); 
            clip[2].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/End.wav")))); 
            clip[3].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Zombies_coming.wav")))); 
            clip[4].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Plant.wav")))); 
            
            clip[6].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Eat.wav")))); 
            clip[7].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Lose.wav")))); 
        }catch(Exception ex)  { 
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString()); //show error dialog
        } 

        //play zombies coming after 10 seconds
        timer = new Timer(10000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                clip[3].start(); 
                timer.stop();
            }
        });
    }

    public static void begin(){
        clip[2].stop();
        clip[2].setMicrosecondPosition(0); 

        clip[1].start(); 
        clip[1].loop(Clip.LOOP_CONTINUOUSLY);
       
        timer.start();
    }

    public static void win(){
        clip[1].stop(); 
        clip[1].setMicrosecondPosition(0);
        clip[3].setMicrosecondPosition(0);
        
        clip[2].start();
        clip[2].loop(Clip.LOOP_CONTINUOUSLY); 
    }

    public static void lose(){
        clip[1].stop(); 
        clip[1].setMicrosecondPosition(0);
        clip[3].setMicrosecondPosition(0);

        clip[7].start();
    }
    
    public static void plant(){
        clip[4].setMicrosecondPosition(0);
        clip[4].start();
    }
    
    public static void eat(){
        clip[6].setMicrosecondPosition(0);
        clip[6].start();
    }
    public static boolean isEating(){
        return clip[6].isActive();
    }
}