import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.*;

public class Main extends JFrame {
    public static LocalDateTime start;
    public static LocalDateTime end;
    static JPanel centerPanel;
    static Future<?> runningTask;
    static ArrayList<App> apps = new ArrayList<>();

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
            new AppForm().setVisible(true);
        });
        northPanel.add(addGame);

        centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());

        renderApps();

        add(centerPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
    }

    public static void writeApps() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("apps.txt"), 4096);    //size (SZ) = velikost bufferu
        for (int i = 0; i < apps.size(); i++) {
            System.out.println(apps.get(i).toString());
            bw.write(apps.get(i).toString());
            bw.newLine();
        }
        bw.close();
    }

    public static void loadApps(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            App app;
            while ((line = br.readLine()) != null) {
                app = new App(
                        line.split(";")[0],
                        line.split(";")[1],
                        Double.parseDouble(line.split(";")[2])
                );
                apps.add(app);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("File not found.");
        }

    }

    public static void renderApps() {
        centerPanel.setLayout(new GridLayout(apps.size(), 1));
        centerPanel.removeAll();
        for (int i = 0; i < apps.size(); i++) {
            String appFile = apps.get(i).getFile();
            JPanel appPanel = new JPanel();
            appPanel.setLayout(new FlowLayout());
            JPanel appInfoPanel = new JPanel();
            appInfoPanel.setLayout(new GridLayout(3, 1));
            JLabel appTitle = new JLabel(apps.get(i).getTitle());
            JLabel appHours = new JLabel(String.valueOf(apps.get(i).getHours()) + " hours played");
            JLabel trackStatus = new JLabel("Not tracking");
            appInfoPanel.add(appTitle);
            appInfoPanel.add(appHours);
            appInfoPanel.add(trackStatus);
            appPanel.add(appInfoPanel);

            JButton trackBtn = new JButton("Track");
            JButton stopTrackBtn = new JButton("Stop Track");
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");
            trackBtn.addActionListener(e -> {
                start = null;
                end = null;
                runningTask = startCountExecutor.submit(() -> {
                    trackBtn.setEnabled(false);
                    editBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                    stopTrackBtn.setEnabled(true);
                    ProcessHandle process = null;

                    while (process == null && !Thread.currentThread().isInterrupted()) {
                        //System.out.println("searching");
                        trackStatus.setText("Searching");
                        process = ProcessHandle.allProcesses()
                                .filter(ph -> ph.info().command().toString().contains(appFile))
                                .findFirst()
                                .orElse(null);
                    }

                    start = LocalDateTime.now();

                    if (process != null && process.isAlive()) {
                        System.out.println(process.pid());
                        //System.out.println("here");
                        trackStatus.setText("Tracking");
                        process.onExit().thenAccept(pp -> {
                            end = LocalDateTime.now();
                            System.out.println(end);
                            trackBtn.setEnabled(true);
                            editBtn.setEnabled(true);
                            deleteBtn.setEnabled(true);
                            stopTrackBtn.setEnabled(false);
                            long seconds = Duration.between(start, end).getSeconds();
                            System.out.println(seconds);
                            trackStatus.setText("Not tracking");
                        });
                    }

                });
            });

            stopTrackBtn.setEnabled(false);
            stopTrackBtn.addActionListener(e -> {
                if (runningTask != null) {
                    runningTask.cancel(true);
                }

                stopTrackBtn.setEnabled(false);
                editBtn.setEnabled(true);
                deleteBtn.setEnabled(true);
                trackBtn.setEnabled(true);

                if (start != null) {
                    end = LocalDateTime.now();
                    long seconds = Duration.between(start, end).getSeconds();
                    System.out.println(seconds);
                }

                trackStatus.setText("Not tracking");
            });

            appPanel.add(trackBtn);
            appPanel.add(stopTrackBtn);
            appPanel.add(editBtn);
            appPanel.add(deleteBtn);

            centerPanel.add(appPanel);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public static void main(String[] args) {
        loadApps("apps.txt");
        new Main().setVisible(true);
    }
}