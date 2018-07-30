package interview;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
//        Connection connection=DriverManager.getConnection("jdbc:mysql://172.31.0.219:3306/confluence?sessionVariables=storage_engine%3DInnoDB&amp;useUnicode=true&amp;characterEncoding=utf8",
//                "confluence","123456");
//        connection.prepareStatement("select 1 from dual").execute();
//        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/unitymob",//sessionVariables=storage_engine%3DInnoDB&amp;useUnicode=true&amp;characterEncoding=utf8
//                "root","root");
        //?sessionVariables=storage_engine%3DInnoDB&amp;useUnicode=true&amp;characterEncoding=utf8mb4
        Connection connection=DriverManager.getConnection("jdbc:mysql://172.31.0.219:3306/test",
                "root","123456");
        PreparedStatement ps=connection.prepareStatement("insert into a(a) values(?)");
        ps.setString(1,"⚽");
        ps.execute();
    }
}
