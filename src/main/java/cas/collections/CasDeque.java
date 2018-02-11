package cas.collections;


import base.Node;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by guzy on 18/1/26.
 */
public class CasDeque<E> {

    class NodePair<E>{
        Node<E> head;

        Node<E> tail;

        public NodePair() {
        }

        public NodePair(Node<E> head, Node<E> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NodePair<?> nodePair = (NodePair<?>) o;

            if ((head ==null) != (nodePair.head ==null) || !head.equals(nodePair.head)) return false;
            return (tail ==null) == (nodePair.tail ==null) && tail.equals(nodePair.tail);

        }

        @Override
        public int hashCode() {
            int result = head ==null?0: head.hashCode();
            result = 31 * result + (tail ==null?0: tail.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "NodePair{" +
                    "head=" + head +
                    ", tail=" + tail +
                    '}';
        }
    }

    AtomicReference<NodePair<E>> pair=new AtomicReference<NodePair<E>>();

    public void putHead(E e){
        Node<E> node=new Node<E>(e);
        NodePair<E> oldPair;
        NodePair<E> newPair=new NodePair<E>();
        newPair.head=node;

        do{
            oldPair=pair.get();
            if(oldPair!=null && oldPair.tail!=null){
                newPair.tail=oldPair.tail;

                node.next=oldPair.head;
                oldPair.head.pre=node;
            }else{
                newPair.tail=node;
                node.next=null;
            }
        }while (!pair.compareAndSet(oldPair,newPair));
    }

    public void putTail(E e){
        Node<E> node=new Node<E>(e);
        NodePair<E> oldPair;
        NodePair<E> newPair=new NodePair<E>();
        newPair.tail=node;

        int i=0;
        do{
            oldPair=pair.get();
            if(oldPair!=null && oldPair.head!=null){
                newPair.head=oldPair.head;

                if(e.equals(3) && i==0){
                    System.out.println("3--");
                    try {
                        latch3.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                node.pre=oldPair.tail;
                oldPair.tail.next=node;

                if(e.equals(3)){
                    latch8.countDown();
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                if(e.equals(8) && i==0){
                    latch3.countDown();
                    System.out.println("8--");
                    try {
                        latch8.await();

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }
            }else{
                newPair.head=node;
                node.pre=null;
            }
            i++;
        }while (!pair.compareAndSet(oldPair, newPair));
        System.out.println(pair.get());

    }

    public E pollHead(){
        NodePair<E> oldPair;
        NodePair<E> newPair=new NodePair<E>();

        do{
            oldPair=pair.get();
            if(oldPair==null || oldPair.head==null){
                return null;
            }

            newPair.head=oldPair.head.next;
            if(newPair.head==null){
                newPair.tail=null;
            }else{
                newPair.tail=oldPair.tail;
                newPair.head.pre=null;
            }
        }while(!pair.compareAndSet(oldPair,newPair));
        return oldPair.head.value;
    }

    public E pollTail(){
        NodePair<E> oldPair;
        NodePair<E> newPair=new NodePair<E>();

        do{
            oldPair=pair.get();
            if(oldPair==null || oldPair.tail==null){
                return null;
            }

            newPair.tail=oldPair.tail.pre;

            if(newPair.tail==null){
                newPair.head=null;
            }else{
                newPair.head=oldPair.head;
                newPair.tail.next=null;
            }
        }while(!pair.compareAndSet(oldPair,newPair));
        return oldPair.tail.value;
    }

    public Iterator<E> iterator(){
        Node<E> node=new Node<E>(null);

        NodePair<E>nodePair=pair.get();
        if(nodePair!=null){
            node.next=nodePair.head;
        }

        return node;
    }

    static CountDownLatch latch=new CountDownLatch(2);

    static CountDownLatch latch3=new CountDownLatch(1);

    static CountDownLatch latch8=new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        final CasDeque<Integer> casDeque =new CasDeque<Integer>();
//        deque.putHead(1);
//        deque.putHead(4);

        casDeque.putTail(4);
        casDeque.putTail(5);
        new Thread(new Runnable() {
            @Override
            public void run() {
                casDeque.putTail(3);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                casDeque.putTail(8);
                latch.countDown();
            }
        }).start();

//
//        System.out.println(deque.pollTail());
//        System.out.println(deque.pollTail());


        latch.await();
        System.out.println(casDeque.pair);

               // System.out.println(deque.pollHead());

                System.out.println(casDeque.pollHead());
        System.out.println(casDeque.pair);
        System.out.println(casDeque.pollHead());
        System.out.println(casDeque.pair);

        System.out.println(casDeque.pollHead());
        System.out.println(casDeque.pair);
        System.out.println(casDeque.pollHead());
        System.out.println(casDeque.pair);
//        System.out.println(deque.pollTail());
//        System.out.println(deque.pollTail());
//                System.out.println(deque.pollTail());
//        System.out.println(deque.pollTail());
//        Iterator<Integer> itr=deque.iterator();
//        while (itr.hasNext()){
//            System.out.println(itr.next());
//        }
    }

}
