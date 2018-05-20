package interview.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by guzy on 16/10/10.
 */
public class GetGenericType<T> {

    public GetGenericType(){
    }

    public void test(){

    }

    public <T2> T2 get(List<T2> t){
        return null;
    }


    public <T1> void test(List<T1> list){

    }

    public T test1(List<T> list){
        return null;
    }

    //get
    public void test2(List<Object> list){
    }

    public <T2> T2 test3(T2 t){
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        GetGenericType<String> getGenericType =new GetGenericType<String>();
        Method method=GetGenericType.class.getDeclaredMethod("test3",Object.class);
        System.out.println(getGenericTypeFromMethod(method));
    }


    /**
     * 获取方法的泛型结果
     * @param method
     * @return
     */
    private static Class getGenericTypeFromMethod(Method method){
        Type type=method.getGenericParameterTypes()[0];
        if(type instanceof  ParameterizedType) {
            Type actualType=((ParameterizedType) type).getActualTypeArguments()[0];
            if(actualType instanceof Class){
                return (Class)actualType;
            }
        }
        return null;
    }

    private static Class getGenericTypeFromClass(Class cls) {
        Type type=cls.getGenericSuperclass();
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
