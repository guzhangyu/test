package com.test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SteamTest {

    public static void main(String[] args) {
        test26();
    }

    public static void test1(){
        List<Transaction> transactions=new ArrayList<>();
        transactions.add(new Transaction(1,10,Transaction.Type.GEOCERY));
        transactions.add(new Transaction(3,30,Transaction.Type.GEOCERY));
        transactions.add(new Transaction(6,60,Transaction.Type.GEOCERY));
        transactions.add(new Transaction(5,50,Transaction.Type.GEOCERY));
        transactions.add(new Transaction(2,20,Transaction.Type.A));
        transactions.add(new Transaction(4,40,Transaction.Type.C));

        List<Integer> transactionIds=transactions.parallelStream()
                .filter(t->t.getType()==Transaction.Type.GEOCERY)
                .sorted(Comparator.comparing(Transaction::getValue1).reversed())
                .map(Transaction::getId)
                .collect(Collectors.toList());

        System.out.println(transactionIds);
    }

    public static void test5(){
        IntStream.of(new int[]{1,2,3}).forEach(System.out::print);
        IntStream.range(1,3).forEach(System.out::print);
        IntStream.rangeClosed(1,3).forEachOrdered(System.out::print);
    }

    public static void test6(){
        Stream stream=Stream.of("a","b","c");
        String[] strArr=(String[])stream.toArray(String[]::new);
    }

    public static void test7(){
        Stream<String> stream=Stream.of("a","b","c");
        List<String> wordList=stream.map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(wordList);
    }

    public static void test26(){
        SteamTest test=new SteamTest();
        Map<Boolean,List<User>> children=Stream.generate(test.new UserSupplier())
                .limit(100)
                .collect(Collectors.partitioningBy(p->p.age>18));
        System.out.println("Children number:"+children.get(false).size());
        System.out.println("Adult number:"+children.get(true).size());
    }

    class UserSupplier implements Supplier<User> {

        private int index=0;
        private final Random random=new Random();

        @Override
        public User get() {
            return new User(index++,"name_"+index,random.nextInt(100));
        }
    }

    class User{
        public int no;
        public String name;
        public int age;

        public User(int no, String name, int age) {
            this.no = no;
            this.name = name;
            this.age = age;
        }
    }

}
