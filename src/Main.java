import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class Main extends JFrame {

    public Main() {
        setLocationRelativeTo(null);
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JButton trackBtn = new JButton("track");

        trackBtn.addActionListener(e -> {
            trackBtn.setEnabled(false);

            Optional<ProcessHandle> process = ProcessHandle.allProcesses()
                    .filter(ph -> ph.info().command().toString().contains("FrostyModManager.exe"))
                    .findFirst();

            ProcessHandle p = process.get();

            if (p.isAlive()) {
                System.out.println("here");
                p.onExit().thenAccept(pp -> {
                    System.out.println(LocalDateTime.now());
                    trackBtn.setEnabled(true);
                });
            }
        });

        add(trackBtn, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    public void test() {

    }
}