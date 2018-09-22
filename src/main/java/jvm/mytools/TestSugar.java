package jvm.mytools;

import concurrent.cas.collections.MyConcurrentLinkedQueue;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试语法糖
 */
public class TestSugar {


    /**
     * 去掉除了testName以外的test代码
     * @param cls
     * @param testNames
     * @return
     * @throws IOException
     */
    public String cleanExcept(Class cls,String... testNames) throws IOException {
        String fileName=System.getenv("PWD")+"/src/main/java/"+(cls.getCanonicalName().replaceAll("\\.","/"))+".java";

        FileInputStream fis=new FileInputStream(fileName);
        InputStreamReader isr=new InputStreamReader(fis,"utf-8");

        StringBuffer sb=new StringBuffer();
        char[] bytes=new char[1024];
        int len=0;
        while((len=isr.read(bytes))>0){
            sb.append(new String(bytes,0,len));
        }

        fis.close();
        isr.close();

        String code=sb.toString();
        String testName="[^\\)]+";
        if(testNames.length>0){
            for(String s:testNames){
                testName="(?!"+s+")"+testName;
            }
            testName=String.format("(%s)",testName);
        }

        String pt="\\s+//@test\\_s\\("+testName+"\\)[\\S\\s]+?//@test\\_e\\("+testName+"\\).*";
        System.out.println(pt);
        Pattern testPat=Pattern.compile(pt,Pattern.MULTILINE);
        Matcher matcher=testPat.matcher(code);

        boolean changed=false;
        while(matcher.find()){
            code=code.replace(matcher.group(),"");
            changed=true;
        }

        if(changed){
            FileOutputStream fos=new FileOutputStream(fileName);
            fos.write(code.getBytes());
            fos.close();

            FileOutputStream barkFos=new FileOutputStream(fileName+".bark");
            barkFos.write(sb.toString().getBytes());
            barkFos.close();
        }
        return null;
    }

    /**
     * 恢复 test代码
     * @param cls
     * @return
     * @throws IOException
     */
    public String revert(Class cls) throws IOException {
        String fileName=System.getenv("PWD")+"/src/main/java/"+(cls.getCanonicalName().replaceAll("\\.","/"))+".java";

        File barkFile=new File(fileName+".bark");
        if(!barkFile.exists()){
            return null;
        }
        FileInputStream fis=new FileInputStream(barkFile);
        InputStreamReader isr=new InputStreamReader(fis,"utf-8");

        StringBuffer sb=new StringBuffer();
        char[] bytes=new char[1024];
        int len=0;
        while((len=isr.read(bytes))>0){
            sb.append(new String(bytes,0,len));
        }

        fis.close();
        isr.close();

        String code=sb.toString();
        FileOutputStream fos=new FileOutputStream(fileName);
        fos.write(code.getBytes());
        fos.close();

        barkFile.delete();
        return null;
    }

    public static void main(String[] args) throws IOException {
        TestSugar testSugar=new TestSugar();
        //testSugar.cleanExcept(MyConcurrentLinkedQueue.class,"test1");
        testSugar.revert(MyConcurrentLinkedQueue.class);
    }
}

