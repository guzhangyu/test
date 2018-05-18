package concurrent.cas.collections;

import base.Node;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 通过AtomicReferenceFieldUpdater实现的双端队列
 * Created by guzy on 18/1/27.
 */
public class ReffUpdateCasDeque {

    AtomicReferenceFieldUpdater headUpdater=AtomicReferenceFieldUpdater.newUpdater(Node.class,Node.class,"next");

    AtomicReferenceFieldUpdater tailUpdater=AtomicReferenceFieldUpdater.newUpdater(Node.class,Node.class,"pre");

    public void putHead(Object e){
        Node node=new Node(e);
    }

}
