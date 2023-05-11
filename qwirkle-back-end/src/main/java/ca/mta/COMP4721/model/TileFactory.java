package ca.mta.COMP4721.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The type Tile factory.
 */
public class TileFactory {

    /**
     * Build tile bank array deque.
     *
     * @return the array deque
     */
    ArrayDeque<Tile> buildTileBank() {
        ArrayDeque<Tile> tileBank = new ArrayDeque<>();
        ArrayList<Tile> tileList = tilesGenerator();
        for (Tile t: tileList) {
            tileBank.add(t);
        }
        tileList.clear();
        return tileBank;
    }

    private ArrayList<Tile> tilesGenerator() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Shape shape: Shape.values()) {
            for (Color color: Color.values()) {
                tiles.add(new Tile(shape, color));
                tiles.add(new Tile(shape, color));
                tiles.add(new Tile(shape, color));
            }
        }
        Collections.shuffle(tiles);
        return tiles;
    }
    
}
