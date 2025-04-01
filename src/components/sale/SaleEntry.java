package components.sale;

import javax.swing.*;
import partial.interfaces.*;
import java.awt.*;

public class SaleEntry extends JPanel implements FontInterface{
    public SaleEntry(){
        JLabel label=new JLabel("Sale Entry component");
        label.setFont(headingFont);
        add(label,BorderLayout.CENTER);
        setBackground(Color.white);
        setVisible(true);
    }
}
