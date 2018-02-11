package base;

import java.util.Iterator;

/**
 * Created by guzy on 16/7/26.
 */
public class Node<E> implements Iterator<E>{
    public Node<E> next;
    public Node<E> pre;
    public E value;

    public Node(E value) {
        this.value = value;
    }

    @Override
    public boolean hasNext() {
        return next!=null;
    }

    @Override
    public E next() {
        if(next==null){
            throw new IllegalArgumentException("下标越界");
        }
        E value=next.value;
        next=next.next;
        return value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "next=" + str(next) +
                ", pre=" + str(pre) +
                ", value=" + value +
                '}';
    }

    public Object str(Node node){
        return node==null?null:node.value;
    }
}
