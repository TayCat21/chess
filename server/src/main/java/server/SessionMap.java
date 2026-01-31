package server;

import org.eclipse.jetty.websocket.api.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;

public record SessionMap(Map<Integer, Set<Session>> gameSessions) {

    public SessionMap {
        if (gameSessions == null) {
            gameSessions = new ConcurrentHashMap<>();
        }
    }

}
