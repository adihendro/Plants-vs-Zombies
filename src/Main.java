import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        int inset=38;
        if(System.getProperty("os.name").equals("Mac OS X")){
            inset=22;
        }else if(System.getProperty("os.name").equals("Linux")){
            inset=37;
        }
        
        JFrame frame = new JFrame();
        frame.add(new World());
        frame.setTitle("Plants vs Zombies");
        frame.setBounds(127, 0, 1024, 625+inset);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
