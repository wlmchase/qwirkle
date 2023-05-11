package ca.mta.COMP4721.DTO;

import ca.mta.COMP4721.model.Tile;

import java.util.ArrayList;

/**
 * The type Discard tiles info.
 */
public record DiscardTilesInfo(String playerId, ArrayList<Tile> tiles) {
}
