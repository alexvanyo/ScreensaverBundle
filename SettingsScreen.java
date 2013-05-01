import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/29/13
 * Time: 6:08 PM
 */
public class SettingsScreen extends JPanel {

    private ArrayList<OptionSelector> optionSelectors;

    public SettingsScreen() {

        this.setPreferredSize(new Dimension(300, 400));

        JPanel optionsPanel = new JPanel();

        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        optionsPanel.add(new JLabel(" "));
        optionSelectors = new ArrayList<OptionSelector>();
        for (FileHandler.Options optionI : FileHandler.Options.values()) {
            optionSelectors.add(new OptionSelector(optionsPanel, optionI));
        }

        JPanel buttonPanel = new JPanel(new BorderLayout());

        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (OptionSelector optionSelector : optionSelectors) {
                    optionSelector.updateValue();
                }

                FileHandler.saveOptions();
            }
        });
        buttonPanel.add(save, BorderLayout.EAST);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.close();
            }
        });
        buttonPanel.add(cancel, BorderLayout.WEST);

        optionsPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(optionsPanel);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setPreferredSize(new Dimension(300, 360));

        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        this.add(scrollPane);
    }

}
