import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import java.util.concurrent.*;

public class Main extends JFrame {
    public static LocalDateTime start;
    public static LocalDateTime end;
    static JButton trackBtn;
    static JButton stopTrackBtn;
    static Future<?> runningTask;

    public static ExecutorService startCountExecutor = Executors.newSingleThreadExecutor();


    public Main() {
        setLocationRelativeTo(null);
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        JFileChooser chooser = new JFileChooser();

        JButton addGame = new JButton("Add Game");
        addGame.addActionListener(e -> {
            int r = chooser.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                System.out.printf(chooser.getSelectedFile().getName());
            }
        });
        northPanel.add(addGame);


        trackBtn = new JButton("track");


        trackBtn.addActionListener(e -> {
            start = null;
            end = null;
            runningTask = startCountExecutor.submit(() -> {
                trackBtn.setEnabled(false);
                stopTrackBtn.setEnabled(true);
                ProcessHandle process = null;

                while (process == null && !Thread.currentThread().isInterrupted()) {
                    System.out.println("searching");
                    process = ProcessHandle.allProcesses()
                            .filter(ph -> ph.info().command().toString().contains("FrostyModManager.exe"))
                            .findFirst()
                            .orElse(null);
                }

                start = LocalDateTime.now();

                if (process != null && process.isAlive()) {
                    System.out.println(process.pid());
                    System.out.println("here");
                    process.onExit().thenAccept(pp -> {
                        end = LocalDateTime.now();
                        System.out.println(end);
                        trackBtn.setEnabled(true);
                        stopTrackBtn.setEnabled(false);
                        long seconds = Duration.between(start, end).getSeconds();
                        System.out.println(seconds);
                    });
                }

            });
        });

        stopTrackBtn = new JButton("Stop tracking");
        stopTrackBtn.setEnabled(false);
        stopTrackBtn.addActionListener(e -> {
            if (runningTask != null) {
                runningTask.cancel(true);
            }

            stopTrackBtn.setEnabled(false);
            trackBtn.setEnabled(true);

            if (start != null) {
                end = LocalDateTime.now();
                long seconds = Duration.between(start, end).getSeconds();
                System.out.println(seconds);
            }
        });

        add(trackBtn, BorderLayout.CENTER);
        add(stopTrackBtn, BorderLayout.EAST);
        add(northPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}