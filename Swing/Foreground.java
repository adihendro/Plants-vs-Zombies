import javax.swing.*;  
import java.awt.*;  

public class Foreground extends JFrame {  
    public Foreground() {  
        super("LayeredPane Example");  
        setSize(200, 200);  
        final JLayeredPane pane = getLayeredPane();  
        //creating buttons  
        final JButton top = new JButton();  
        top.setBackground(Color.white);  
        top.setBounds(20, 20, 50, 50);  
        final JButton middle = new JButton();  
        middle.setBackground(Color.red);  
        middle.setBounds(40, 40, 50, 50);  
        final JButton bottom = new JButton();  
        bottom.setBackground(Color.cyan);  
        bottom.setBounds(60, 60, 50, 50);  
        //adding buttons on pane  
        pane.add(bottom, new Integer(1));  
        pane.add(middle, new Integer(2));  
        pane.add(top, new Integer(3));  
      }  
}
