package dataaccess;

import model.Gamedata;

public interface GameDataAccess {
    Gamedata getGame(int gameID);

    int createGame(String gameName);

    void listGames();

    void updateGame(String gameID);

    void clear();
}
