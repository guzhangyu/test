package hadoop.common;

public class DataTuple implements Comparable<DataTuple> {

    private Integer t1;

    private String t2;

    public DataTuple(Integer t1, String t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public int compareTo(DataTuple o) {
        return o.t1-t1;
    }

    public Integer getT1() {
        return t1;
    }

    public void setT1(Integer t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }
}
