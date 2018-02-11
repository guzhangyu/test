/**
 * Created by guzy on 16/4/24.
 */
public class ReverseVowelsOfAString {

    public String reverseVowels(String s) {
        char[] cs=s.toCharArray();
        int start=0,end=s.length()-1;
        while(start<end){
            while(start<end && !isVowel(cs[start])) {
                start++;
            }
            while(start<end && !isVowel(cs[end])){
                end--;
            }
            if(start<end && cs[start]!=cs[end]){
                char mid=cs[start];
                cs[start]=cs[end];
                cs[end]=mid;
            }
            start++;
            end--;
        }
        return new String(cs);
    }

    public static char[]arr =new char[]{'a','e','i','o','u','A','E','I','O','U'};

    private Boolean isVowel(char c){
        for(char cr:arr){
            if(c==cr){
                return true;
            }
        }
        return false;
    }

    public boolean isPowerOfThree(int n) {
        while(n>1){
            if(n%3!=0){
                return false;
            }
            n/=3;
        }
        return n==1;
    }

    public static void main(String[]args){
        System.out.println(new ReverseVowelsOfAString().isPowerOfThree(81));
    }
}