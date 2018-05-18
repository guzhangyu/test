package interview;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guzy on 2018-03-28.
 */
public class TpsTest {

    static Connection con;
    static volatile int c;
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","root");
            con.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        PreparedStatement ps=con.prepareStatement("update zyh_hosp set password=? where id=3");

        new Timer("d").schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(c);
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.exit(1);
            }
        },1000*10);

        while (true){
            ps.setString(1,(++c)+"");
            ps.execute();
            con.commit();
        }
    }
}
