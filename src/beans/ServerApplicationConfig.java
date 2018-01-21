package beans;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by costas on 21/1/2018.
 */

public class ServerApplicationConfig implements javax.websocket.server.ServerApplicationConfig{

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {

        Set<ServerEndpointConfig> serverEndpointConfigSet = new HashSet<ServerEndpointConfig>();

        serverEndpointConfigSet.add(ServerEndpointConfig.Builder.create(EndPointServer.class, "/endPointServer").build());




        return serverEndpointConfigSet;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
        return null;
    }
}
