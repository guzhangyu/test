import java.util.ArrayList;
import java.util.List;

/**
 * Created by guzy on 16/4/24.
 */
public class PascalTriangle {

    public static void main(String[]args){
        PascalTriangle pt=new PascalTriangle();
        List<List<Integer>> list= pt.generate(6);
        for(List<Integer> ints:list){
            for(Integer it:ints){
                System.out.print(" "+it);
            }
            System.out.println();
        }
    }

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> list=new ArrayList<List<Integer>>(numRows);
        if(numRows==0){
            return list;
        }
        List<Integer> pre=new ArrayList<Integer>(1);
        pre.add(1);
        list.add(pre);
        for(int i=1;i<numRows;i++){
            List<Integer> temp=new ArrayList<Integer>(i+1);
            temp.add(1);
            for(int j=0;j<i-1;j++){
                temp.add(pre.get(j)+pre.get(j+1));
            }
            temp.add(1);
            pre=temp;
            list.add(temp);
        }
        return list;

    }
}

