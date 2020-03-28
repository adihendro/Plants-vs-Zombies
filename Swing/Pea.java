public class Pea{
    private String type;
    private int damage;
    
    public Pea(String type){
        this.type=type;
        if(type.equals("Peashooter")){
        	damage=10;
        }
        else if(type.equals("Repeater")){
        	damage=8;
        }
    }
}