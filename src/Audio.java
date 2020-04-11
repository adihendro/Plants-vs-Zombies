import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            clip[2].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Win.wav")))); 
            clip[3].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Lose.wav")))); 
            clip[4].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Zombies_coming.wav")))); 
            clip[5].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Seedlift.wav")))); 
            clip[6].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Plant.wav")))); 
            clip[7].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Eat.wav")))); 
            clip[8].open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Buzzer.wav")))); 
        }catch(Exception ex)  { 
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString()); //show error dialog
        } 

        //play zombies coming after 12 seconds
        timer = new Timer(12000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                clip[4].setMicrosecondPosition(0);
                clip[4].start(); 
                timer.stop();
            }
        });
    }

    public static void begin(){
        clip[2].stop();
        clip[3].stop();
        
        clip[1].start(); 
        clip[1].loop(Clip.LOOP_CONTINUOUSLY);
       
        timer.start();
    }

    public static void win(){
        clip[1].stop(); 
        clip[1].setMicrosecondPosition(0);
        
        clip[2].setMicrosecondPosition(0);
        clip[2].start();
    }

    public static void lose(){
        clip[1].stop(); 
        clip[1].setMicrosecondPosition(0);
        
        clip[3].setMicrosecondPosition(0);
        clip[3].start();
    }
    
    public static void seedlift(){
        clip[5].setMicrosecondPosition(0);
        clip[5].start();
    }
    public static void plant(){
        clip[6].setMicrosecondPosition(0);
        clip[6].start();
    }
    
    public static void eat(){
        clip[7].setMicrosecondPosition(0);
        clip[7].start();
    }
    public static boolean isEating(){
        return clip[7].isActive();
    }

    public static void buzzer(){
        clip[8].setMicrosecondPosition(0);
        clip[8].start();
    }
}