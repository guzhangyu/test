package jvm.hotswap;

/**
 * Created by guzy on 16/12/29.
 */
public class HotswapClassLoader extends ClassLoader {

    public HotswapClassLoader(){
        super(HotswapClassLoader.class.getClassLoader());
    }

    public Class loadBytes(byte[] b){
        return defineClass(null,b,0,b.length);
    }
}
