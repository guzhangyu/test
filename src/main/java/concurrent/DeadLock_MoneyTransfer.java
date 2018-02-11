//package concurrent;
//
//import java.util.concurrent.TimeUnit;
//
///**
// *
// * Created by guzy on 16/7/15.
// */
//public class DeadLock_MoneyTransfer {
//
//    private static final Object tieLock=new Object();
//
//    /**
//     * 加上锁定的转账方案，有时间限制
//     * @param fromAcct
//     * @param toAcct
//     * @param amount
//     * @param timeout
//     * @param unit
//     * @return
//     * @throws InsufficientFundsException
//     * @throws InterruptedException
//     */
//    public boolean transferMoney(Account fromAcct,Account toAcct,DollarAmount amount,long timeout,TimeUnit unit) throws InsufficientFundsException,InterruptedException{
//        long fixedDelay = getFixedDelayComponentNanos(timeout,unit);
//        long randMod = getRandomDelayModulesNanos(timeout,unit);
//        long stopTime = System.nanoTime()+unit.toNanos(timeout);
//        while(true){
//            if(fromAcct.lock.tryLock()){
//                try{
//                    if(toAcct.lock.tryLock()){
//                        try{
//                            if(fromAcct.getBalance().compareTo(amount)<0){
//                                throw new InsufficientFundsException();
//                            }else{
//                                fromAcct.debit(amount);
//                                toAcct.credit(amount);
//                                return true;
//                            }
//                        }finally {
//                            toAcct.lock.unlock();
//                        }
//                    }
//                }finally {
//                    fromAcct.lock.unlock();
//                }
//            }
//            if(System.nanoTime()>stopTime){
//                return false;
//            }
//        }
//        NANOSECONDS.sleep(fixedDelay+rnd.nextLong()%randMod);
//    }
//
//    /**
//     * 防止死锁的转账方案
//     * @param fromAcct
//     * @param toAcct
//     * @param amount
//     * @throws InsufficientFundsException
//     */
//    public void transferMoney(final Account fromAcct,final Account toAcct,final DollarAmount amount) throws InsufficientFundsException{
//        class Helper{
//            public void transfer() throws InsufficientFundsException{
//                if(fromAcct.getBalance().compareTo(amount)<0){
//                    throw new InsufficientFundsException();
//                }else {
//                    fromAcct.debit(amount);
//                    toAcct.credit(amount);
//                }
//            }
//        }
//
//        int fromHash = System.identityHashCode(fromAcct);
//        int toHash=System.identityHashCode(toAcct);
//
//        if(fromHash<toHash){
//            synchronized (fromAcct){
//                synchronized (toAcct){
//                    new Helper().transfer();
//                }
//            }
//        }else if(fromHash<toHash){
//            synchronized (toAcct){
//                synchronized (fromAcct){
//                    new Helper().transfer();
//                }
//            }
//        }else {
//            synchronized (tieLock){
//                new Helper().transfer();
//            }
//        }
//    }
//}
