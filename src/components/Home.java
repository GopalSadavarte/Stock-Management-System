/**
 * 
 * The Home component is the initial window after authentication.
 * This component have an basic information about the application and user details and 
 * stakeholder details.
 *  
 */
package components;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {
    // instance variables.
    JLabel homeScreenNameLabel, description, footer, contact, email, address, year;
    JPanel headingPanel, subPanel, labelPanels[];

    /**
     * A constructor which create a default home page component.
     */
    public Home() {
        // This panel are the main panel of all other panel to hold other panels.
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder(75, 0, 0, 0));

        // This panel is a heading panel to hold the window heading label of user firm
        // name.
        headingPanel = new JPanel();
        headingPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // This is name label for hold the users firm name.
        homeScreenNameLabel = new JLabel("KK Enterprises");
        homeScreenNameLabel.setFont(new Font("cambria", 50, 100));
        homeScreenNameLabel.setForeground(Color.red);
        headingPanel.add(homeScreenNameLabel);
        headingPanel.setBackground(Color.white);

        // the heading will added into the top of main panel
        add(headingPanel, BorderLayout.NORTH);

        // the sub panel is hold the other panels
        subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(12, 1));
        subPanel.setBackground(Color.white);

        // these are label panels which holds each label on each panel and add it on sub
        // panel.
        labelPanels = new JPanel[6];
        for (int i = 0; i < labelPanels.length; i++) {
            labelPanels[i] = new JPanel();
            labelPanels[i].setLayout(new FlowLayout(FlowLayout.CENTER));
            labelPanels[i].setSize(new Dimension(getWidth(), getHeight()));
            labelPanels[i].setBackground(Color.white);
        }

        // this is description label to hold the description.
        description = new JLabel("version 1.0 - Stock Management System");
        description.setFont(new Font("cambria", 20, 25));
        // description will added into the first label panel.
        labelPanels[0].add(description);

        // this is footer label to hold the footer.
        footer = new JLabel("Design and Develop by Gopal Sadavarte & Siddharth Sarode");
        footer.setFont(new Font("cambria", 20, 20));
        // footer will added into the second label panel.
        labelPanels[1].add(footer);

        // this is contact label to hold the contact info.
        contact = new JLabel("<html><strong>Contact</strong> : 8956434705/9561734705</html>");
        contact.setFont(new Font("cambria", 20, 18));
        // contact will added into the third label panel.
        labelPanels[2].add(contact);

        // this is email label to hold the email info.
        email = new JLabel("<html><strong>Email</strong> : technology@softtech.com</html>");
        email.setFont(new Font("cambria", 20, 18));
        // email will added into the fourth label panel.
        labelPanels[3].add(email);

        // this is address label to hold the address info.
        address = new JLabel("<html><strong>Address</strong> : Gandhinagar,satara road,Shrirampur-413709</html>");
        address.setFont(new Font("cambria", 20, 18));
        // address will added into the fifth label panel.
        labelPanels[4].add(address);

        // this is year label to hold the year of buid.
        year = new JLabel("<html><strong>2024-25</strong></html>");
        year.setFont(new Font("cambria", 20, 17));
        // year will added into the sixth label panel.
        labelPanels[5].add(year);

        // Above all label panels will added into the sub panel .
        subPanel.add(labelPanels[0]);
        subPanel.add(labelPanels[1]);
        subPanel.add(labelPanels[2]);
        subPanel.add(labelPanels[3]);
        subPanel.add(labelPanels[4]);
        subPanel.add(labelPanels[5]);

        // And then sub panel will added center of the main panel.
        add(subPanel, BorderLayout.CENTER);

        // set visibility for frame.
        setVisible(true);
    }
}
/**
 * This component end here...
 */
