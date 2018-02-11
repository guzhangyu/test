import java.util.Stack;

/**
 * Created by guzy on 16/4/25.
 */
public class MyQueue {

    Stack<Integer> stack=new Stack<Integer>();
    int front=0;
    // Push element x to the back of queue.
    public void push(int x) {
        if(stack.isEmpty()){
            front=x;
        }
        stack.push(x);
    }

    // Removes the element from in front of queue.
    public void pop() {
        if(stack.isEmpty()){
            return;
        }
        if(stack.size()==1){
            stack.pop();
            front=0;
            return;
        }
        Stack<Integer> temp=new Stack<Integer>();
        int size=stack.size();
        for(int i=0;i<size-1;i++){
            temp.push(stack.pop());
        }
        stack.pop();
        front=temp.pop();
        stack.push(front);
        for(int i=1;i<size-1;i++){
            stack.push(temp.pop());
        }
    }

    // Get the front element.
    public int peek() {
        return front;
    }

    // Return whether the queue is empty.
    public boolean empty() {
        return stack.isEmpty();
    }

    public static void main(String[]args){
        MyQueue myQueue=new MyQueue();
        myQueue.push(1);
        myQueue.push(2);
        myQueue.pop();
        myQueue.push(3);
        myQueue.push(4);
        myQueue.pop();
        System.out.println(myQueue.peek());
    }
}