package jvm.mytools;

import concurrent.cas.collections.MyConcurrentLinkedQueue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试语法糖
 */
public class TestSugar {


    /**
     * 去掉除了testName以外的test代码
     * @param cls
     * @param testName
     * @return
     * @throws IOException
     */
    public String cleanExcept(Class cls,String testName) throws IOException {
        String fileName=System.getenv("PWD")+"/src/main/java/"+(cls.getCanonicalName().replaceAll("\\.","/"))+".java";

        FileInputStream fis=new FileInputStream(fileName);

        StringBuffer sb=new StringBuffer();
        byte[] bytes=new byte[1024];
        int len=0;
        while((len=fis.read(bytes))>0){
            sb.append(new String(bytes,0,len));
        }

        fis.close();

        String code=sb.toString();
        if(testName==null){
            testName="[^\"]+";
        }else{
            testName="((?!"+testName+")[^\"]+)";
        }
        System.out.println("\\s+//@test\\_s\\(\""+testName+"\"\\)[\\S\\s]+?//@test\\_e\\(\""+testName+"\"\\).*");
        Pattern testPat=Pattern.compile("\\s+//@test\\_s\\(\""+testName+"\"\\)[\\S\\s]+?//@test\\_e\\(\""+testName+"\"\\).*",Pattern.MULTILINE);
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

        StringBuffer sb=new StringBuffer();
        byte[] bytes=new byte[1024];
        int len=0;
        while((len=fis.read(bytes))>0){
            sb.append(new String(bytes,0,len));
        }

        fis.close();

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
