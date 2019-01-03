package hadoop.common;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TextPair2 implements Writable, WritableComparable<TextPair2> {

    private Text t1=new Text();

    private Text t2=new Text();

    public TextPair2() {
    }

    public TextPair2(String key, String key1) {
        t1.set(key);
        t2.set(key1);
    }


    @Override
    public int compareTo(TextPair2 o) {
        int c= t1.toString().compareTo(o.t1.toString());
        if(c!=0){
            return c;
        }
        return t2.toString().compareTo(o.t2.toString());
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
