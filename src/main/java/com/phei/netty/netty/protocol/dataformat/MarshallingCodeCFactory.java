package com.phei.netty.netty.protocol.dataformat;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * Created by guzy on 16/8/15.
 */
public class MarshallingCodeCFactory {

    public static Marshaller buildMarshalling() throws IOException {
        MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        return Marshalling.getProvidedMarshallerFactory("serial").createMarshaller(configuration);
    }


    public static Unmarshaller buildUnMarshalling() throws IOException {
        MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        return Marshalling.getProvidedMarshallerFactory("serial").createUnmarshaller(configuration);
    }
}
