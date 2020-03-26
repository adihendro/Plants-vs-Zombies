import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        JFrame frame=new JFrame();
        World panel = new World();
        // Foreground f = new Foreground();
        frame.setBounds(150,0, 1024, 647);
        frame.setTitle("Plants vs Zombies");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        // frame.add(f);

        frame.setVisible(true);
    }
}
