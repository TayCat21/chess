package server;

import jakarta.websocket.Session;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketSessions {
    public final SessionMap connections = new SessionMap(new ConcurrentHashMap<>());

    public void addSession(int gameID, Session session) {
        connections.gameSessions().computeIfAbsent(gameID, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
                .add(session);
    }

    public void removeSession(int gameID, Session session) {
        Set<Session> sessions = connections.gameSessions().get(gameID);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                connections.gameSessions().remove(gameID);
            }
        }
    }

    public Set<Session> getSessions(int gameID) {
        return connections.gameSessions().getOrDefault(gameID, Collections.emptySet());
    }
}
