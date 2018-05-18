package concurrent.tests;

import java.util.Stack;

/**
 * Created by guzy on 18/1/24.
 */
public class BracketStack {

    public static boolean valid(String str){
        Stack<Character> stack=new Stack<Character>();
        char[]cs=str.toCharArray();
        for(char c:cs){
            if(c=='(' || c=='['){
                stack.push(c);
            }else if(stack.isEmpty()){
                return false;
            }else{
                char cur=stack.pop();
                if( (cur=='(' && c==']')||(cur=='[' && c==')') ){
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println(valid("[[(())]]"));
    }
}
