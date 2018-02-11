package reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by guzy on 16/10/10.
 */
public class Test<T> {

    public Test(){
    }

    public void test(){

    }

    public <T2> T2 get(List<T2> t){
        return null;
    }


    public <T1> void test(List<T1> list){

    }

    public void test1(List<T> list){

    }

    public static void main(String[] args) throws NoSuchMethodException {
        Test<String> test=new Test<String>();
        Method method=Test.class.getDeclaredMethod("test1",List.class);
        System.out.println(getGenericTypeFromMethod(method));
    }

    private static Class getGenericTypeFromMethod(Method method){
        Type type=method.getGenericParameterTypes()[0];
        if(type instanceof  ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type actualType=pt.getActualTypeArguments()[0];
            if(actualType instanceof Class){
                return (Class)actualType;
            }
        }
        return null;
    }

    private static Class getGenericType(Object o) {
        Type type=o.getClass().getGenericSuperclass();
        //.getGenericInterfaces()[0];
        //.getGenericSuperclass();

        if (type  instanceof ParameterizedType) {

            ParameterizedType  paramType  =  (ParameterizedType) type;

            Type[] args = paramType.getActualTypeArguments();

            if (args != null && args.length>0) {

                Type arg = args[0];

                if (arg instanceof Class) {
                    return (Class)arg;
                    //System.out.println(t.getName());
                }
            }

        }
        return null;
    }
}
