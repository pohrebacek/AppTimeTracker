public class App {
    private String title;
    private String file;
    private double hours;

    public App(String title, String file, double hours) {
        this.title = title;
        this.file = file;
        this.hours = hours;
    }

    public String getTitle() {
        return this.title;
    }

    public String getFile() {
        return this.file;
    }

    public double getHours() {
        return this.hours;
    }
}
