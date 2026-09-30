public class App {
    private String title;
    private String file;
    private long seconds;

    public App(String title, String file, long seconds) {
        this.title = title;
        this.file = file;
        this.seconds = seconds;
    }

    public String getTitle() {
        return this.title;
    }

    public String getFile() {
        return this.file;
    }

    public long getSeconds() {
        return this.seconds;
    }
}
