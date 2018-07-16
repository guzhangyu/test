package com.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateTest {

    public static void main(String[] args) {
        Configuration cfg=new Configuration().configure();
        SessionFactory factory=cfg.buildSessionFactory();

        Session session=factory.openSession();
        session.beginTransaction();
        session.getTransaction().commit();
        System.out.println("end");
    }
}
