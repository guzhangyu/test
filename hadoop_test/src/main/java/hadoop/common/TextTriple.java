package hadoop.common;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TextTriple implements Writable, WritableComparable<TextTriple> {

    private Text t1=new Text();

    private Text t2=new Text();

    private Text t3=new Text();

    public TextTriple() {
    }

    public TextTriple(String key, String key1,String key3) {
        t1.set(key);
        t2.set(key1);
        t3.set(key3);
    }


    @Override
    public int compareTo(TextTriple o) {
        return t1.toString().compareTo(o.t1.toString());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        t1.write(dataOutput);
        t2.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        t1.readFields(dataInput);
        t2.readFields(dataInput);
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)",t1.toString(),t2.toString());
    }

    public Text getT1() {
        return t1;
    }

    public void setT1(Text t1) {
        this.t1 = t1;
    }

    public Text getT2() {
        return t2;
    }

    public void setT2(Text t2) {
        this.t2 = t2;
    }
}
