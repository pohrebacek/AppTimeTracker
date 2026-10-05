import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class AppForm extends JFrame {
    public AppForm() {
        setLocationRelativeTo(null);
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel fileLabel = new JLabel(".exe file:");
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Select a .exe file");
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .exe files", "exe");
        chooser.addChoosableFileFilter(restrict);
        JPanel choosePanel = new JPanel();
        choosePanel.setLayout(new FlowLayout());
        JLabel selectLabel = new JLabel("No file selected");
        JButton selectBtn = new JButton("Select file");
        selectBtn.addActionListener(e -> {
            int r = chooser.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                selectLabel.setText(chooser.getSelectedFile().getName());
            } else {
                System.out.println("kok");
            }

        });
        choosePanel.add(selectLabel);
        choosePanel.add(selectBtn);
        formPanel.add(fileLabel);
        formPanel.add(choosePanel);

        JLabel nameLabel = new JLabel("App name: ");
        JTextField nameTf = new JTextField();
        formPanel.add(nameLabel);
        formPanel.add(nameTf);

        JLabel hoursLabel = new JLabel("Total hours to count from (eg. 14.6):");
        JTextField hoursTf = new JTextField();
        formPanel.add(hoursLabel);
        formPanel.add(hoursTf);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.addActionListener(e -> {
            StringBuilder errors = new StringBuilder();
            double hours = 0.0;
            if (chooser.getSelectedFile() == null) {
                errors.append("- Select a .exe file.\n");
            }

            if (nameTf.getText().isEmpty()) {
                errors.append("- Name field cannot be empty.\n");
            }

            if (hoursTf.getText().isEmpty()) {
                errors.append("- Hours field cannot be empty.\n");
            } else {
                try {
                    hours = Double.parseDouble(hoursTf.getText());
                } catch (NumberFormatException ex) {
                    errors.append("- Set hours in displayed format.\n");
                }
            }

            if (!errors.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Following errors occurred:\n" + errors, "Errors", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("gut");
                App app = new App(nameTf.getText(), chooser.getSelectedFile().getName(), hours);
                Main.apps.add(app);
                Main.renderApps();
                dispose();
            }
        });
        cancelBtn.addActionListener(e -> {
            dispose();
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
