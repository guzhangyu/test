//package concurrent.cas.collections;
//
///**
// * Created by guzy on 18/1/26.
// */
//public class Node<E> {
//    public Node<E> next;
//    public Node<E> pre;
//    public E value;
//
//    public Node(E value) {
//        this.value = value;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(obj==null || !(obj instanceof Node)){
//            return false;
//        }
//        Node<E> o=(Node<E>)obj;
//        return o.value==this.value && o.next==this.next && o.pre==this.pre;
//    }
//
//
//
//}
