import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 

public class Audio{
    private static Clip clip1, clip2, clip3; 
    private static Timer timer; //set timer

    static{
        try{
            // create clip reference 
            clip1 = AudioSystem.getClip(); 
            clip2 = AudioSystem.getClip(); 
            clip3 = AudioSystem.getClip(); 
            // open audioInputStream to the clip 
            clip1.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Background.wav")))); 
            clip2.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/End.wav")))); 
            clip3.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Zombies_coming.wav")))); 
        }catch (Exception ex)  { 
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString()); //show error dialog
        } 

        //play zombies coming after 10 seconds
        timer = new Timer(10000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                clip3.start(); 
                timer.stop();
            }
        });
    }

    public static void begin(){
        clip2.stop();
        clip2.setMicrosecondPosition(0); 

        clip1.start(); 
        clip1.loop(Clip.LOOP_CONTINUOUSLY);
       
        timer.start();
    }

    public static void end(){
        clip1.stop(); 
        clip1.setMicrosecondPosition(0); 
        clip3.setMicrosecondPosition(0); 

        clip2.start();
        clip2.loop(Clip.LOOP_CONTINUOUSLY); 
    }
}