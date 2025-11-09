package dataaccess;

import model.Gamedata;
import chess.ChessGame;
import service.ListGamesItem;

import javax.xml.crypto.Data;
import java.util.List;

public interface GameDataAccess {
    Gamedata getGame(int gameID);

    int createGame(String gameName);

    List<ListGamesItem> listGames();

    void updateGame(ChessGame.TeamColor color, String username, int gameID) throws DataAccessException;

    void clear();
}
