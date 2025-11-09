package dataaccess;

import chess.ChessGame;
import model.Gamedata;
import service.ListGamesItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MemoryGameDataAccess implements GameDataAccess {

    private final HashSet<Gamedata> gameBank;
    private int idCounter = 1;
    private final List<ListGamesItem> gamesList = new ArrayList<>();

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
    public List<ListGamesItem> listGames() {
        if (!gamesList.isEmpty()) {
        gamesList.clear();
        }

        for (Gamedata game : gameBank) {
            ListGamesItem listItem = new ListGamesItem(game.gameID(), game.whiteUsername(),
                    game.blackUsername(), game.getGameName());
            gamesList.add(listItem);
        }
        return gamesList;
    }

    @Override
    public void updateGame(ChessGame.TeamColor color, String username, int gameID) throws DataAccessException {
        Gamedata myGame = getGame(gameID);
        switch (color) {
            case ChessGame.TeamColor.WHITE -> {
                if (myGame.whiteUsername() == null) {
                    Gamedata myNewGame = new Gamedata(gameID, username, myGame.blackUsername(),
                            myGame.gameName(), myGame.game());
                    gameBank.add(myNewGame);
                } else {
                    throw new DataAccessException("already taken");
                }
            }
            case ChessGame.TeamColor.BLACK -> {
                if (myGame.blackUsername() == null) {
                    Gamedata myNewGame = new Gamedata(gameID, myGame.whiteUsername(), username,
                            myGame.gameName(), myGame.game());
                    gameBank.add(myNewGame);
                } else {
                    throw new DataAccessException("already taken");
                }
            }
            default ->
                throw new DataAccessException("color selection error");

        }

        gameBank.remove(myGame);
    }

    @Override
    public void clear() {
        gameBank.clear();
    }
}
