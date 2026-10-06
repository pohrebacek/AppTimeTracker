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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public String toString() {
        return this.title + ";" + this.file + ";" + this.hours;
    }
}
