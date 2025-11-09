package dataaccess;

import chess.ChessGame;
import model.Gamedata;
import java.util.HashSet;

public class MemoryGameDataAccess implements GameDataAccess {

    private final HashSet<Gamedata> gameBank;
    private int idCounter = 0;

    public MemoryGameDataAccess() {
        gameBank = HashSet.newHashSet(16);
    }

    @Override
    public Gamedata getGame(int gameID) {
        for (Gamedata game : gameBank) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public int createGame(String gameName) {
        int gameID = idCounter;
        idCounter++;
        ChessGame newChessGame = new ChessGame();

        Gamedata newGame = new Gamedata(gameID, null, null, gameName, newChessGame);
        gameBank.add(newGame);

        return gameID;
    }

    @Override
    public void listGames() {

    }

    @Override
    public void updateGame(String gameID) {

    }

    @Override
    public void clear() {
        gameBank.clear();
    }
}
