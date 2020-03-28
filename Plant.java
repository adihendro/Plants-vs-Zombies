public class Plant extends Actor{
    private int price;

    public Plant(String type){
        super.type=type;
        if(type.equals("Sunflower")){
            health = 100;
            price=50;
        }else if(type.equals("Peashooter")){
            health = 50;
            price=100;
        }else{ //Repeater
            health = 70;
            price=150;
        }
    }
    public int getPrice(){
        return price;
    }

}