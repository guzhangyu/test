/**
 * Created by guzy on 16/4/24.
 */
public class HappyNumber {

    public boolean isHappy(int n) {
        if(n==1){
            return true;
        }
        int pre=0;
        while(n>10 || pre!=n){
            if(n<10){
                if(n==1){
                    return true;
                }
                pre=n;
            }
            int num=0;
            while(n>0){
                num+=(n%10)*(n%10);
                n/=10;
            }
            n=num;
        }
        return n==1;
    }

    public static void main(String[]args){
        HappyNumber hn=new HappyNumber();
        System.out.println(hn.isHappy(7));
    }
}
