import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        JFrame frame=new JFrame();
                
        frame.setBounds(10,10, 800, 600);
        frame.setTitle("Plants vs Zombies");
        frame.setResizable(false);   
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
        // frame.setSize(400,500); 
        // f.setLayout(null);
        frame.setVisible(true);
    }
}