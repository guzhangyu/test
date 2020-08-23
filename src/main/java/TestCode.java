public class TestCode {

//    private void doAcquireShared(int arg) {
//        final Node node = addWaiter(Node.SHARED);
//        boolean failed = true;
//        try{
//            boolean interrupted = false;
//            for(;;){
//                final Node p = node.predecessor();
//                if (p == head) { //如果是头结点的下一个节点，可能在这个过程中，前面的x锁释放
//                    int r = tryAcquireShared(arg);
//                    if (r >= 0) {
//                        setHeadAndPropagate(node, r); //设置新头，并且传递释放后续读锁
//                        p.next = null;
//                        if (interrupted)
//                            selfInterrupt();
//                        failed = false;
//                        return ;
//                    }
//                }
//                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
//                    interrupted = true;
//            }
//        } finally{
//            if (failed)
//                cancelAcquire(node);
//        }
//    }
//
//    private void setHeadAndPropagate(Node node, int propagate) {
//        Node h = head;
//        setHead(node);
//        if (propagate > 0 || h == null || h.waitStatus < 0 ||
//                (h = head) ==null || h.waitStatus <0) {
//            Node s = node.next;
//            if (s == null || s.isShared())
//                doReleaseShared();
//        }
//    }
//
//    public final boolean releaseShared(int arg){
//        if (tryReleaseShared(arg)) {
//            doReleaseShared();
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Release action for shared mode -- signals successor and ensures
//     * propagation. (Note: For exclusive mode, release just amounts
//     * to calling unparkSuccessor of head if it needs signal.)
//     */
//    // 通知后续节点并保证传播
//    private void doReleaseShared() {
//        /**
//         * Ensure that a release propagates, even if there are other
//         * in-progress acquires/releases.
//         * This proceeds in the usual way of trying to unparkSuccessor of head if it needs
//         * signal.
//         * But if it does not, status is set to PROPAGATE to ensure that upon release, propagation continues.
//         * Additionally, we must loop in case a new node is added while we are doing this.
//         * Also, unlike other uses of unparkSuccessor, we need to know if CAS to reset status fials, if so rechecking.
//         */
//        for (;;) {
//            Node h = head;
//            if(h != null && h != tail) { //表示后面有等待节点
//                int ws = h.waitStatus;
//                if (ws == Node.SIGNAL) {
//                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0)) //通过cas将节点等待状态从待通知变为已获得锁，如果失败，表示有其他线程在进行了
//                        continue; // loop to recheck cases
//                    unparkSuccessor(h); //unpark 后续节点
//                }else if (ws == 0 &&
//                            !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
//                    //如果有新的进程修改了head节点，会出现waitStatus为0， 要将其改为PROPAGATE, 来保证在新节点release之后，传播行为继续
//                    continue;
//            }
//            if (h == head)
//                break;
//            //如果有新的进程修改了head节点，循环继续
//        }
//    }

    public static void main(String[] args) {
//        ClassLoader.getSystemClassLoader().
//        System.out.println(System.getenv());
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Thread t=Thread.currentThread();
                System.out.println(t.getId());
                while (!t.isInterrupted());
            }
        });
        t.start();
        System.out.println("main thread:"+Thread.currentThread().getId());
        t.interrupt();
    }
}


