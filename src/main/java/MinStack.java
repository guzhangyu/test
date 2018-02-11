/**
 * Created by guzy on 16/4/24.
 */
public class MinStack {
    int arr[]=new int[100];
    int curIndex=0;
    int min=0;
    public void push(int x) {
        if(curIndex>=arr.length){
            int[] temp=new int[arr.length+100];
            for(int i=0;i<arr.length;i++){
                temp[i]=arr[i];
            }
            arr=temp;
        }
        arr[curIndex]=x;
        if(curIndex==0){
            min=x;
        }else{
            if(min>x){
                min=x;
            }
        }
        curIndex++;
    }

    public void pop() {
        if(curIndex==0){
            return;
        }
        if(min==arr[curIndex-1]){
            if(curIndex>1){
                min=arr[0];
                for(int i=1;i<curIndex-1;i++){
                    if(min>arr[i]){
                        min=arr[i];
                    }
                }
            }else{
                min=0;
            }
        }
        curIndex--;
    }

    public int top() {
        if(curIndex>0){
            return arr[curIndex-1];
        }
        return 0;
    }

    public int getMin() {
        return min;
    }

    public static void main(String[]args){
        MinStack minStack=new MinStack();
        minStack.push(2);
        minStack.push(0);
        minStack.push(3);
        minStack.push(0);

        minStack.pop();
        minStack.pop();
        minStack.pop();
        System.out.println(minStack.getMin());

//        minStack.push(1);
//        minStack.printStack();
//
//        minStack.pop();
//        minStack.printStack();
    }

    public void printStack(){
//        for(int i=0;i<curIndex;i++){
//            System.out.println(arr[i]);
//        }
        System.out.println("top:"+top());
        System.out.println("min:"+getMin());
    }
}
