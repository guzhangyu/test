package jmm;

/**
 * Created by guzy on 16/7/27.
 */
public class ResourceFactory {

    private static class ResourceHolder{
        static Resource resource=new Resource();
    }

    public static Resource getResource(){
        return ResourceHolder.resource;
    }
}
