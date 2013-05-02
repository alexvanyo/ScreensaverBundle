import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/30/13
 * Time: 5:40 PM
 */
public class OptionSelector {

    private static final String[] booleanValues = new String[] {"true", "false"};

    private JLabel name;
    private JComboBox dropDownMenu;
    private JTextField textField;
    private JButton defaultButton;

    private FileHandler.Options option;

    public OptionSelector(JPanel parent, FileHandler.Options optionValue) {
        this.option = optionValue;

        name = new JLabel(option.getName());
        name.setFont(new Font(name.getFont().getFontName(), Font.BOLD, 13));
        name.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        parent.add(name);

        if (option.getType() == FileHandler.OptionTypes.BOOLEAN) {
            dropDownMenu = new JComboBox<String>(booleanValues);
            for (int i = 0; i < booleanValues.length; i++) {
                if (booleanValues[i].equals(option.getValue())) {
                    dropDownMenu.setSelectedIndex(i);
                }
            }
            dropDownMenu.setMaximumSize(new Dimension(200, 20));
            dropDownMenu.setEditable(false);

            parent.add(dropDownMenu);
        } else {
            textField = new JTextField(option.getValue());
            textField.setMaximumSize(new Dimension(200, 20));

            parent.add(textField);
        }

        defaultButton = new JButton("Default Value");
        defaultButton.setMaximumSize(new Dimension(200, 20));
        defaultButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDefault();
            }
        });

        parent.add(defaultButton);

        parent.add(new JLabel(" "));
    }

    public void updateValue() {
        if (option.getType() == FileHandler.OptionTypes.BOOLEAN) {
            option.setValue(dropDownMenu.getSelectedItem().toString());
        } else {
            option.setValue(textField.getText());
        }
    }

    public void setDefault() {
        if (option.getType() == FileHandler.OptionTypes.BOOLEAN) {
            for (int i = 0; i < booleanValues.length; i++) {
                if (booleanValues[i].equals(option.getDefaultValue())) {
                    dropDownMenu.setSelectedIndex(i);
                }
            }
        } else {
            textField.setText(option.getDefaultValue());
        }
    }
}
