package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import com.mysql.jdbc.Driver;
public class JdbcTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
//        Class.forName("com.mysql.jdbc.Driver");
////        Connection connection=DriverManager.getConnection("jdbc:mysql://172.31.0.219:3306/confluence?sessionVariables=storage_engine%3DInnoDB&amp;useUnicode=true&amp;characterEncoding=utf8",
////                "confluence","123456");
//
////        Connection connection=DriverManager.getConnection("jdbc:mysql://172.31.0.67:3306/xxl-job?sessionVariables=storage_engine%3DInnoDB&amp;useUnicode=true&amp;characterEncoding=utf8",
////                "unitymob","unitymob@2018");
//        Connection connection=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test",
//                "root","root");
//        connection.prepareStatement("select 1 from dual").execute();
//
//        while(true){
//
//        }

        JdbcTest jdbcTest=new JdbcTest();
        jdbcTest.start();

        Thread.sleep(100000l);
        synchronized (jdbcTest.a){
            jdbcTest.a.notify();
        }
        ListIterator listIterator= new ArrayList().listIterator();


    }

    Thread thread;

    Object a = new Object();

    public void start(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                for(int i=0;i<100;i++){
//                    System.out.println("number: " + i);
//                    try {
//                        Thread.sleep(1000l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                while (true){
//
//                }
                try {
                    synchronized (a){
                        a.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "test");
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
}