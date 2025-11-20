package dataaccess;

import com.google.gson.Gson;
import service.ListGamesItem;

import java.util.ArrayList;
import java.util.Collection;

public class GamesList extends ArrayList<ListGamesItem> {
    public GamesList() {

    }

    public GamesList(Collection<ListGamesItem> games) {
        super(games);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this.toArray());
    }
}