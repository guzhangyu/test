package com.phei.netty.xml;

import com.phei.netty.xml.pojo.Customer;
import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Created by guzy on 16/8/10.
 */
public class TestOrder {

    private IBindingFactory factory=null;

    private final static String CHARSET_NAME="UTF-8";

    public TestOrder() throws JiBXException {
        factory= BindingDirectory.getFactory(Customer.class);
    }

    private String encode2Xml(Customer customer)throws JiBXException,IOException{
        IMarshallingContext mctx=factory.createMarshallingContext();
        mctx.setIndent(2);
        StringWriter writer=new StringWriter();
        mctx.marshalDocument(customer,CHARSET_NAME,null,writer);

        String xmlStr=writer.toString();
        writer.close();

        System.out.println(xmlStr.toString());
        return xmlStr;
    }

    private Customer decode2Obj(String xml) throws JiBXException {
        IUnmarshallingContext cutx=factory.createUnmarshallingContext();
        Customer customer=(Customer)cutx.unmarshalDocument(new StringReader(xml));
        return customer;
    }

    public static void main(String[] args) throws JiBXException, IOException {
        TestOrder testOrder=new TestOrder();

        Customer customer=new Customer();
        customer.setCustomerNumber(1l);
        customer.setFirstName("f");
        customer.setMiddleNames(Arrays.asList("2"));
        customer.setLastName("l");

        String xml=testOrder.encode2Xml(customer);
       // System.out.println(xml);

        customer=testOrder.decode2Obj(xml);
        System.out.println(customer);
    }
}
