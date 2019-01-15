package hadoop.common;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class StringLongPair {

    private Text s=new Text();

    private LongWritable l=new LongWritable();

    public StringLongPair() {
    }

    public StringLongPair(Text s, LongWritable l) {
        this.s = s;
        this.l = l;
    }

    public StringLongPair(String s,Long l){
        this.s=new Text(s);
        this.l=new LongWritable(l);
    }

    public Text getS() {
        return s;
    }

    public void setS(Text s) {
        this.s = s;
    }

    public void setS(String s) {
        this.s.set(s);
    }

    public LongWritable getL() {
        return l;
    }

    public void setL(LongWritable l) {
        this.l = l;
    }

    public void setL(Long l) {
        this.l.set(l);
    }
}
