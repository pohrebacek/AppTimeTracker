import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

public class Main extends JFrame {
    public static LocalDateTime start;
    public static LocalDateTime end;


    public Main() {
        setLocationRelativeTo(null);
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JButton trackBtn = new JButton("track");

        trackBtn.addActionListener(e -> {
            trackBtn.setEnabled(false);

            ProcessHandle process = null;

            while (process == null) {

                System.out.println("searching");
                process = ProcessHandle.allProcesses()
                        .filter(ph -> ph.info().command().toString().contains("FrostyModManager.exe"))
                        .findFirst()
                        .orElse(null);
            }

            start = LocalDateTime.now();

            System.out.println(process.pid());

            if (process.isAlive()) {
                System.out.println("here");
                process.onExit().thenAccept(pp -> {
                    end = LocalDateTime.now();
                    System.out.println(end);
                    trackBtn.setEnabled(true);

                    long seconds = Duration.between(start, end).getSeconds();
                    System.out.println(seconds);
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