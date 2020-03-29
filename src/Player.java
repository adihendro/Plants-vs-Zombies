import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.JOptionPane;

public class Player {
    private int sunCredits, temp;
    private Font font;
    // public static PlantvsZombie world = new PlantvsZombie();

    //profil player
    public Player(){
        sunCredits=200;
        temp=sunCredits;
        try{
            //create the font to use
            font=Font.createFont(Font.TRUETYPE_FONT, new File("../Assets/Chalkboard.ttc")).deriveFont(Font.BOLD, 20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font); //register the font
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString()); //show error dialog
        }
        font=new Font("Chalkboard", Font.BOLD, 20); //load font
    }

    public void draw(Graphics2D g){
        //sunflower points
        g.setFont(font); 
        g.setColor(Color.BLACK);
        FontMetrics metrics = g.getFontMetrics(font); //font width and height
        //to animate sunflower points change
        if(sunCredits==temp){
            g.drawString(Integer.toString(temp), 74-(metrics.stringWidth(Integer.toString(temp))/2), 165);
        }else if(sunCredits<temp){
            temp-=5;
            g.drawString(Integer.toString(temp), 74-(metrics.stringWidth(Integer.toString(temp))/2), 165);
        }else{ //sunCredits>temp
            temp+=5;
            g.drawString(Integer.toString(temp), 74-(metrics.stringWidth(Integer.toString(temp))/2), 165);
        }
    }

    //tambahkan credit 25 poin
    public void addSunCredits(){
        sunCredits+=25;
    }
    // // reset credit
    // public void clearCredits(){
    //     sunCredits=0;
    // }
    // //mengubah jumlah credit
    // public void setCredits(int credits){
    //     sunCredits = credits;
    // }
    //getter jumlah suncredit dari player
    public int getCredits(){
        return sunCredits;
    }
    // // getter world
    // public PlantvsZombie getWorld(){
    //     return world;
    // }

    //jumlah credit pada plants
    public void plantType (int plantT){
        switch (plantT){
            case 1: sunCredits-=50; break; //sunflower
            case 2: sunCredits-=100; break; //peashooter
            case 3: sunCredits-=150; break; //repeater
            // default : sunCredits -= 1000;
        }
    }
}





	
