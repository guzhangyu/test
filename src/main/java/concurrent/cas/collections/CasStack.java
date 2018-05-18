package concurrent.cas.collections;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于cas实现的 并发stack
 * Created by guzy on 16/7/26.
 */
public class CasStack<E> {

    AtomicReference<Node<E>> top=new AtomicReference<Node<E>>();

    public void push(E e){
        Node<E> newHead=new Node<E>(e);
        Node<E> head;
        do{
            head=top.get();
            newHead.next=head;
        }while (!top.compareAndSet(head,newHead));
    }

    public E pop(){
        Node<E> topHead;
        Node<E> nextHead;
        do{
            topHead=top.get();
            if(topHead==null){
                return null;
            }
            nextHead=topHead.next;
        }while(!top.compareAndSet(topHead,nextHead));
        return topHead.value;
    }

    class Node<E>{
        Node<E> next;
        E value;

        public Node(E value){
            this.value=value;
        }
    }

    public static void main(String[] args) {
        CasStack<Integer> cs=new CasStack();
        cs.push(1);
        cs.push(2);

        System.out.println(cs.pop());
        System.out.println(cs.pop());
    }
}
