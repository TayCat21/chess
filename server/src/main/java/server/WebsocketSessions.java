package server;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketSessions {
    public final SessionMap sessionMap = new SessionMap(new ConcurrentHashMap<>());

    public void addSession(int gameID, Session session) {
        sessionMap.gameSessions().computeIfAbsent(gameID, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
                .add(session);
    }

    public void removeSession(int gameID, Session session) {
        Set<Session> sessions = sessionMap.gameSessions().get(gameID);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionMap.gameSessions().remove(gameID);
            }
        }
    }

    public Set<Session> getSessions(int gameID) {
        return sessionMap.gameSessions().getOrDefault(gameID, Collections.emptySet());
    }
}
