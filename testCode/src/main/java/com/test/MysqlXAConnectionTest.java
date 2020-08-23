package com.test;

import com.mysql.jdbc.jdbc2.optional.MysqlXAConnection;
import com.mysql.jdbc.jdbc2.optional.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlXAConnectionTest {

    public static void main(String[] args) throws SQLException {
        boolean logXaCommands = true;

        Connection conn1 = DriverManager.getConnection("jdbc:mysql://172.31.0.219:3306/atom","root","123456");
        XAConnection xaConn1 = new MysqlXAConnection((com.mysql.jdbc.Connection)conn1,logXaCommands);
        XAResource rm1 = xaConn1.getXAResource();

        Connection conn2 = DriverManager.getConnection("jdbc:mysql://172.31.0.219:3306/atom","root","123456");
        XAConnection xaConn2 = new MysqlXAConnection((com.mysql.jdbc.Connection)conn2,logXaCommands);
        XAResource rm2 = xaConn2.getXAResource();

        byte[] gtrid="g12345".getBytes();
        int formatId=1;


        try {
            byte[] bqual1 = "b00001".getBytes();
            Xid xid1=new MysqlXid(gtrid,bqual1,formatId);
            rm1.start(xid1,XAResource.TMNOFLAGS);
            PreparedStatement ps1 = conn1.prepareStatement("insert into a(a) values(2)");
            ps1.execute();
            rm1.end(xid1,XAResource.TMSUCCESS);

            byte[] bqual2 = "b00002".getBytes();
            Xid xid2=new MysqlXid(gtrid,bqual2,formatId);
            rm2.start(xid2,XAResource.TMNOFLAGS);
            PreparedStatement ps2=conn2.prepareStatement("insert into a values(3)");
            ps2.execute();
            rm2.end(xid2,XAResource.TMSUCCESS);

            int rm1_prepare = rm1.prepare(xid1);
            int rm2_prepare = rm2.prepare(xid2);

            boolean onePhase = false;
            if(rm1_prepare == XAResource.XA_OK
                && rm2_prepare == XAResource.XA_OK){
                rm1.commit(xid1,onePhase);
                rm2.commit(xid2,onePhase);
            }else{
                rm1.rollback(xid1);
                rm2.rollback(xid2);
            }

        } catch (XAException e) {
            e.printStackTrace();
        }
    }
}
