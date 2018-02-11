package jmm;

/**
 * Created by guzy on 16/7/27.
 */
public class EagerInitialization {

    private static Resource resource=new Resource();

    public static Resource getResource(){
        return resource;
    }
}
