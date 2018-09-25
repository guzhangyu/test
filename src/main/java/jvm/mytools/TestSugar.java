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
     * @param testNames
     * @return
     * @throws IOException
     */
    public String cleanExcept(Class cls,String... testNames) throws IOException {
        String fileName=System.getenv("PWD")+"/src/main/java/"+(cls.getCanonicalName().replaceAll("\\.","/"))+".java";

        String code = getFileContent(fileName),preCode=code;

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
            barkFos.write(preCode.getBytes());
            barkFos.close();
        }
        return null;
    }

    private String getFileContent(String fileName) throws IOException {
        FileInputStream fis=new FileInputStream(fileName);

        byte[] bytes=new byte[(int)new File(fileName).length()];
        int len=0;
        String s=null;
        while((len=fis.read(bytes))>0){
            s= new String(bytes,0,len);
        }

        fis.close();
        return s;
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
        String code=getFileContent(barkFile.getPath());

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

