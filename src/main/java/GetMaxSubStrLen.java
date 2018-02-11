/**
 * Created by guzy on 17/2/20.
 */
public class GetMaxSubStrLen {

    public int lengthOfLongestSubstring(String s) {
        if(s==null || s.length()==0){
            return 0;
        }

        int max=1;
        char[] cs=s.toCharArray();
        int fromIndex=0;
        for(int i=1;i<cs.length;i++){
            int index=indexOf(cs,cs[i],fromIndex,i);
            if(index>=0){
                int now=i-fromIndex;
                if(now>max){
                    max=now;
                }
                if(cs.length-index-1<=max){
                    return max;
                }
                fromIndex=index+1;
            }
        }
        if(fromIndex!=cs.length-1){
            int now=cs.length-fromIndex;
            if(now>max){
                max=now;
            }
        }

        return max;
    }

    private int indexOf(char[] cs,char c,int fromIndex,int toIndex){
        for(int i=fromIndex;i<toIndex;i++){
            if(cs[i]==c){
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(new GetMaxSubStrLen().lengthOfLongestSubstring("ababc"));
    }
}
