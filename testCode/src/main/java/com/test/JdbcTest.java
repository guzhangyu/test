package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
//        Connection connection=DriverManager.getConnection("jdbc:mysql://172.31.0.219:3306/confluence?sessionVariables=storage_engine%3DInnoDB&amp;useUnicode=true&amp;characterEncoding=utf8",
//                "confluence","123456");

        Connection connection=DriverManager.getConnection("jdbc:mysql://172.31.0.67:3306/xxl-job?sessionVariables=storage_engine%3DInnoDB&amp;useUnicode=true&amp;characterEncoding=utf8",
                "unitymob","unitymob@2018");
        connection.prepareStatement("select 1 from dual").execute();
    }
}