package client;

import java.util.List;

public record GameResponse(List<ListGamesItem> games) {
}
