package dataaccess;

import model.Gamedata;
import service.ListGamesItem;
import java.util.List;

public interface GameDataAccess {
    Gamedata getGame(int gameID);

    int createGame(String gameName);

    List<ListGamesItem> listGames();

    void updateGame(String gameID);

    void clear();
}
