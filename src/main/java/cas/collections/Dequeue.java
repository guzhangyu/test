package cas.collections;


import base.Node;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 失败的尝试
 *
 * 思路，通过head与其下一个元素的compareAndSet，来实现；但是碰到一个问题，在set的时候需要更新对方元素
 *
 * ?? 一个表达式的判断 如果另一个地方正在执行，只要没有结束，就打断它；如果刚刚执行掉就回滚自己
 * Created by guzy on 18/1/26.
 */
public class Dequeue<E> {

    class NodePair<E>{
        Node<E> primary;

        Node<E> sibling;

        public NodePair(Node<E> primary, Node<E> sibling) {
            this.primary = primary;
            this.sibling = sibling;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NodePair<?> nodePair = (NodePair<?>) o;

            if ((primary==null) != (nodePair.primary==null) || !primary.equals(nodePair.primary)) return false;
            return (sibling==null) == (nodePair.sibling==null) && sibling.equals(nodePair.sibling);

        }

        @Override
        public int hashCode() {
            int result = primary==null?0:primary.hashCode();
            result = 31 * result + (sibling==null?0:sibling.hashCode());
            return result;
        }
    }

    AtomicReference<NodePair<E>> head=new AtomicReference<NodePair<E>>();

    AtomicReference<NodePair<E>> tail=new AtomicReference<NodePair<E>>();

    public void pushHead(Node<E> node){
        NodePair<E> head;
        NodePair<E> newPair;

        do{
            head=this.head.get();
            Node<E> headNode=head.primary;

            newPair=new NodePair<E>(node,headNode);
            node.next=headNode;
            headNode.pre=node;
        }while(this.head.compareAndSet(head,newPair));
    }
}
