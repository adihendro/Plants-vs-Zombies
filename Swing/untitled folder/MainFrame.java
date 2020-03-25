import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;

public class MainFrame extends JFrame{
    public MainFrame(String title){
        super(title);
        setLayout(new BorderLayout());

        JTextArea text = new JTextArea();
        JButton button = new JButton("PLAY");

        Container c = getContentPane();
        c.add(text, BorderLayout.SOUTH);
        c.add(button, BorderLayout.CENTER);
        

    }
}