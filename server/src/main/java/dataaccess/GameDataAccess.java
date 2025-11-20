package dataaccess;

import model.Gamedata;
import chess.ChessGame;
import service.ListGamesItem;

import javax.xml.crypto.Data;
import java.util.List;

public interface GameDataAccess {
    Gamedata getGame(int gameID) throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    List<ListGamesItem> listGames() throws DataAccessException;

    void updateGame(ChessGame.TeamColor color, String username, int gameID) throws DataAccessException;

    void clear() throws DataAccessException;
}
