/**
 * Created by guzy on 16/4/24.
 */
public class StringToInt {

    public int myAtoi(String str) {
        if(str==null || str.length()==0){
            return 0;
        }
        char[]cs=str.toCharArray();
        int negCount=0;
        int egCount=0;
        int start=0;
        while(start<str.length()&&(cs[start]=='+' || cs[start]=='-' || cs[start]==' ' )){
            if(cs[start]!=' '){
                if(cs[start]=='-'){
                    negCount++;
                }else if(cs[start]=='+'){
                    egCount++;
                }
            }
            start++;
        }
        int v=0;
        for(int i=start;i<cs.length && cs[i]!=' ';i++){
            v*=10;
            v+=(cs[i]-'0');
        }
        if(negCount!=egCount){
            return (egCount-negCount)*v;
        }else if(negCount==0){
            return v;
        }else{
            return 0;
        }
    }

    public static void main(String[]args){
        StringToInt stt=new StringToInt();
        //System.out.println(2147483648);
        System.out.println(stt.myAtoi("2147483648"));
    }


}

