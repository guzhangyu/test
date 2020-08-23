package jmocks;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class MockTest {

    @Mocked
    private PersonService personService;

    @Injectable
    private CoderService coderService;

    @Test
    public void testInstance(){
        new Expectations(){
            {
                personService.showAge(anyInt);
                result=-1;

                personService.getDefaultPerson();
                result = new Person("me",4,null);

                Deencapsulation.invoke(coderService,"showWork",anyString);
                result="java";
            }
        };

        //record的方法，按照给定的结果返回
        Assert.assertTrue(-1==personService.showAge(11));
        Assert.assertTrue("java".equals(coderService.showWork("nothing")));
        Assert.assertTrue(4==personService.getDefaultPerson().getAge());

        //没有录制的方法，返回默认值
        Assert.assertTrue(personService.showName("testName")==null);
        Assert.assertTrue(coderService.showSalary(100)==0);

        //Mock 所有PersonServive 实例
        PersonService personService=new PersonService();
        Assert.assertTrue(-1==personService.showAge(11));
        Assert.assertTrue(personService.showName("testName")==null);
    }

    @Test
    public void partiallyMock(){
        new Expectations(personService){
            {
                personService.showAge(anyInt);
                result=-1;
            }
        };

        //被录制的方法，按照record结果返回
        Assert.assertTrue(-1==personService.showAge(11));
        //未录制的方法，调用原有代码
        Assert.assertTrue("testName".equals(personService.showName("testName")));
    }

    public void testStaticMethod(){

    }
}
