package purchase_combine_filter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DataTuples implements Serializable {

    private String [] arr;

    public DataTuples(String[] arr){
        this.arr=arr;
        Arrays.sort(arr);
    }

    @Override
    public int hashCode() {
        int s=0;
        for(String a:arr){
            s+=a.hashCode();
        }
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !(obj instanceof DataTuples)){
            return false;
        }
        if(obj==this){
            return true;
        }
        DataTuples other=(DataTuples)obj;
        if(this.arr==other.arr){
            return true;
        }

        if(this.arr.length!=other.arr.length){
            return false;
        }
        for(int i=0;i<this.arr.length;i++){
            if(!this.arr[i].equals(other.arr[i])){
                return false;
            }
        }
        return true;
    }

    public String[] getArr() {
        return arr;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }

    @Override
    public String toString() {
        return Arrays.toString(arr);
    }

    public static void main(String[] args) {
        Set<String[]> s=new HashSet<>();
        String[] ss=new String[]{"1","2"};
        s.add(ss);
        String[] test=ss.clone();
        test[0]="2";
        s.add(test);
        s.add(new String[]{"1","x"});
        System.out.println(s);

        for(String[] se:s){
            System.out.println(se[1]+","+se[0]);
        }


    }
}
