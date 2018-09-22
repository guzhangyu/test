package concurrent.cas.collections;

import base.LogUtils;

import java.util.AbstractQueue;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class MyConcurrentLinkedQueue<E> extends AbstractQueue<E> {

    AtomicReference<Node<E>> headRef=new AtomicReference<>();
    AtomicReference<Node<E>> tailRef=new AtomicReference<>();
    volatile boolean lock=false;
    volatile long changeTime=System.currentTimeMillis();

    public Iterator<E> iterator() {
        return new Iterator<E>() {

            Node<E> cur=getHead();

            long myChangeTime=changeTime;

            @Override
            public boolean hasNext() {
                if(changeTime!=myChangeTime){
                    throw new IllegalStateException("该队列已经被改动！");
                }
                return cur!=null;
            }

            @Override
            public E next() {
                if(changeTime!=myChangeTime){
                    throw new IllegalStateException("该队列已经被改动！");
                }
                Node<E> pre=cur;
                cur=cur.getNext();
                return pre.getValue();
            }
        };
    }

    public int size() {
        lock=true;
        int size=0;
        Node<E> node=getHead();
        while(node!=null && node.getNext()!=null){
            size++;
            node=node.getNext();
        }
        lock=false;
        return size;
    }

    public boolean offer(E e) {
        if(lock){
            return false;
        }
        Node<E> node=new Node<E>(e);

        Node<E> tail=getTail();
        while(!setTail(tail,node)){
            tail=getTail();
        }

        changeTime=System.currentTimeMillis();
        if(tail==null){//如果是第一个元素,此处经过cas，所以为null的那个tail肯定是被当前的操作替换
            LogUtils.log(String.format("set head node1"));
            setHead(null,node); //100%
        }else{//在setNext之前，如果poll掉了之前的tail，此处的node可能丢失 fixed

            //@test_s("test1")
            {
                LogUtils.log("offer time1");
                try {
                    Thread.sleep(1000l);//测试
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                LogUtils.log("offer time3");
            }
            //@test_e("test1")

            //1、在poll的set tail之前set 了tail，
            //2、在set next之前，原先的head被设置为null了，此时会丢失新添加的元素
            if(!tail.setNext(null,node)){//说明此时原先的tail的tail的next被修改了，不可能通过offer触发，只能通过poll最后一些元素触发，此时说明要设置新的head
                setHead(null,node); // offer 7 100%
            }

            //@test_s("test1")
            {
                LogUtils.log("offer time2");
            }
            //@test_e("test1")
        }
        return true;
    }

    public E poll() {
        if(lock){
            throw new IllegalArgumentException("queue locked");
        }
        Node<E> head=getHead();
        while(head!=null && !setHead(head,head.getNext())){
            head=getHead();
        }

        if(head==null){
            return null;
        }

        changeTime=System.currentTimeMillis();

        //如果把最后一个元素取出来了
        if(getHead()==null){
            Node<E> tail=getTail();
            LogUtils.log("last head");
            if(head==tail){

                //@test_s("test2")
                LogUtils.log("poll0");
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //@test_e("test2")

                if(!setTail(tail,null)){//test2
                    LogUtils.log("set tail fail");

                    // 在set tail之前，可能有新元素插入,此时需要设置新head
                    if(head.getNext()!=null){
                        LogUtils.log("poll1");
                        setHead(null,head.getNext()); //100%
                        //offer方法不能改变tail的next，且poll方法不可能再获取元素
                    }else{

                        //@test_s("test2")
                        System.out.println("t1");
                        //@test_e("test2")

                        if(!head.setNext(null,REPLACE_NODE)){//offer 7已经完成
                            setHead(null,head.getNext());//100%
                            head.setNext(REPLACE_NODE,null);
                            //@test_s("test2")
                            System.out.println("t2");
                            //@test_e("test2")
                        }
                    }
                }
            }else if(head.setNext(null,REPLACE_NODE)){//针对test1
                setHead(null,tail);//100%
                head.setNext(REPLACE_NODE,null); //必须，否则offer 7会重复执行，导致多一个head
                LogUtils.log(String.format("set head tail1"));
            }
        }
        return head.getValue();
    }

    private static final  Node REPLACE_NODE=new Node(null);

    public E peek() {
        Node<E> head=getHead();
        if(head==null){
            return null;
        }
        return head.getValue();
    }

    public Node<E> getHead() {
        return headRef.get();
    }

    public boolean setHead(Node<E> pre,Node<E> head) {
        return headRef.compareAndSet(pre,head);
    }

    public Node<E> getTail() {
        return tailRef.get();
    }

    public boolean setTail(Node<E> pre,Node<E> tail) {
        return tailRef.compareAndSet(pre,tail);
    }


    static volatile int count=0;
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch offerLatch=new CountDownLatch(4);
        CountDownLatch pollLatch=new CountDownLatch(4);
        CountDownLatch endLatch=new CountDownLatch(8);
        MyConcurrentLinkedQueue<String> queue=new MyConcurrentLinkedQueue<String>();

        class OfferRunnable implements Runnable{
            private int i;
            public OfferRunnable(int i){
                this.i=i;
            }

            @Override
            public void run() {
                offerLatch.countDown();
                try {
                    offerLatch.await();
                    for(int j=0;j<20;j++){
                        queue.offer(i+"_"+j);
                    }
                    pollLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i=0;i<4;i++){
            new Thread(new OfferRunnable(i)).start();
        }

        for(int i=0;i<4;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Iterator<String> itr=queue.iterator();
                    while(itr.hasNext()){
                        System.out.println(String.format("next:%s",itr.next()));
                    }
                    System.out.println("end");
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        pollLatch.await();

                        String r=null;
                        try{
                            r=queue.poll();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        while(r!=null){
                            count++;
                            System.out.println(r);
                            try{
                                r=queue.poll();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        endLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        pollLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("size:%d",queue.size()));
                    endLatch.countDown();
                }
            }).start();
        }
        endLatch.await();
        System.out.println(count);
    }
}

class Node<E>{
    private AtomicReference<Node<E>> next=new AtomicReference<>();

    private E value;

    public Node(E value) {
        this.value = value;
    }

    public Node getNext() {
        return next.get();
    }

    public boolean setNext(Node pre,Node next) {
        return this.next.compareAndSet(pre,next);
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }
}
