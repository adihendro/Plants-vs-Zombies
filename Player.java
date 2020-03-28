import javax.swing.JFrame;

public class Player {
    private static int sunCredits;
    public static PlantvsZombie world = new PlantvsZombie();

//profil player
public Player(){
    sunCredits = 50;
    time =0;
}

//tambahkan kredit 50 poin
public void addSunCredits(){
    sunCredits+=50;
}
// reset credit
public static void clearCredits(){
    sunCredits=0;
}
//mengubah jumlah credit
public static void setCredits(int credits){
    sunCredits = credits;
}
//getter jumlah suncredit dari player
public static void getCredits(){
    return sunCredits;
}
// getter world
public static PlantvsZombie getWorld(){
    return world;
}

//jumlah credit pada plants
public void plantT (String plantType){
    switch (plantType)
{
 case "Peashooter" : sunCredits-=100; break;
 case "Sunflower": sunCredits-=50; break;
 case "Repeater": -+ sunCredits-=150; break;
 default : sunCredits -= 1000;
 }
 }
}





	
