package cas.collections;

import java.util.concurrent.atomic.AtomicReference;

import base.Dealer;
import base.Node;


/**
 * 通过节点对实现的队列
 * Created by guzy on 16/7/26.
 */
public class CasDeque2<E> {

    class NodePair<E>{
        Node<E> head;
        Node<E> tail;

        public NodePair(){

        }

        public NodePair(Node<E> tail){
            this.tail=tail;
        }

        public NodePair(Node<E> head, Node<E> tail){
            this.head=head;
            this.tail=tail;
        }
    }

    private final AtomicReference<NodePair<E>> nodePair=new AtomicReference<NodePair<E>>();

    /**
     * 队头入列
     * @param e
     */
    public void put(E e){
        NodePair<E> oldNodePair;
        Node<E> newTailNode=new Node<E>(e);
        NodePair<E> newNodePair=new NodePair<E>(newTailNode);
        int i=0;
        //TODO:此处同时比较头部以及尾部的一致性，会多一些重试，但是可以保证头尾更新的一致性
        do{
            if(i++>0){
                System.out.println(Thread.currentThread().getName()+" put retry:"+i);
            }
            oldNodePair=this.nodePair.get();
            if(oldNodePair==null || oldNodePair.tail==null) {
                newNodePair.head=newTailNode;
            }else{
                newNodePair.head=oldNodePair.head;

                Node<E> tailNode=oldNodePair.tail;
                tailNode.next=newTailNode;
                newTailNode.pre=tailNode;
            }
        }while (!this.nodePair.compareAndSet(oldNodePair,newNodePair));
    }

    /**
     * 队尾出列
     * @return
     */
    public E poll(){
        NodePair<E> oldNodePair;
        Node<E> oldTailNode;
        NodePair<E> newNodePair=new NodePair<E>();
        int i=0;
        //TODO:此处同时比较头部以及尾部的一致性，会多一些重试，但是可以保证头尾更新的一致性
        do{
            if(i++>0){
                System.out.println(Thread.currentThread().getName()+" poll retry:"+i);
            }
            oldNodePair=this.nodePair.get();
            if(oldNodePair==null || oldNodePair.tail==null){
                return null;
            }

            oldTailNode=oldNodePair.tail;
            newNodePair.tail=oldTailNode.pre;
            newNodePair.tail.next=null;

            if(oldTailNode==oldNodePair.head){//如果原先就只有一个元素
                newNodePair.head=null;
            }else {
                newNodePair.head=oldNodePair.head;
            }

        }while (!this.nodePair.compareAndSet(oldNodePair,newNodePair));
        return oldTailNode.value;
    }

    /**
     * 遍历队列
     * @param dealer
     */
    public void iterate(Dealer<Node<E>> dealer){
        NodePair<E> nodePair=this.nodePair.get();
        if(nodePair==null){
            return;
        }
        Node<E> head=nodePair.head;
        while(head!=null){
            dealer.deal(head);
            head=head.next;
        }
    }
}
