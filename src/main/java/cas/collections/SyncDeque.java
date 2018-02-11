package cas.collections;

import base.Node;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guzy on 18/1/26.
 */
public class SyncDeque {

    volatile Node head,tail;

    Lock headLock=new ReentrantLock(),tailLock=new ReentrantLock();

    public void putTail(Object e){
        Node node=new Node(e);

        tailLock.lock();
        if(tail==null){
            headLock.lock();
            head=tail=node;
            headLock.unlock();
        }else{
            tail.next=node;
            node.pre=tail;
            tail=node;
        }
        tailLock.unlock();
    }

    public void putHead(Object e){
        Node node=new Node(e);

        headLock.lock();
        if(head==null){
            tailLock.lock();
            head=tail=node;
            tailLock.unlock();
        }else{
            head.pre=node;
            node.next=head;
            head=node;
        }
        headLock.unlock();
    }

    public Object pollHead(){
        headLock.lock();
        try{
            if(head==null){
                throw new IllegalArgumentException("队列为空！");
            }

            Object v=head.value;
            if(head.next!=null){
                head.next.pre=null;
                head=head.next;
            }else{
                tailLock.lock();
                tail=head=null;
                tailLock.unlock();
            }
            return v;
        }finally {
            headLock.unlock();
        }
    }

    public Object pollTail(){
        tailLock.lock();
        try{
            if(tail==null){
                throw new IllegalArgumentException("队列为空！");
            }

            Object v=tail.value;
            if(tail.pre!=null){
                tail.pre.next=null;
                tail=tail.next;
            }else{
                headLock.lock();
                tail=head=null;
                headLock.unlock();
            }
            return v;
        }finally {
            tailLock.unlock();
        }
    }

    public static void main(String[] args) {
        SyncDeque syncDeque=new SyncDeque();
        syncDeque.putTail(1);
        syncDeque.putTail(2);

        System.out.println(syncDeque.pollHead());
       // System.out.println(syncDeque.pollHead());
        System.out.println(syncDeque.pollTail());
    }
}
