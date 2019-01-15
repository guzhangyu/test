package hadoop.local_mem;

public class FilePos {

    private String fileName;

    private Integer start;

    private Integer stop;

    public FilePos(String fileName, Integer start, Integer stop) {
        this.fileName = fileName;
        this.start = start;
        this.stop = stop;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getStop() {
        return stop;
    }

    public void setStop(Integer stop) {
        this.stop = stop;
    }
}
