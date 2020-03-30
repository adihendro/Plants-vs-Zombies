public class Pea<T>{
    private String type;
    private T damage;
    
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