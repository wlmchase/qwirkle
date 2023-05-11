package ca.mta.COMP4721.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * The type Player.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Player {
    private String stringId;
    private ArrayList<Tile> hand;
    private int score;
    private static final int STARTING_SCORE = 0;
    private static final int MAX_TILES = 6;

    private Shape shapeMax;
    private Color colorMax;

    private boolean vote;

    /**
     * Instantiates a new Player.
     *
     * @param stringId the string id
     * @param hand     the hand
     * @param score    the score
     */
    public Player(String stringId, ArrayList<Tile> hand, int score) {
        this.stringId = stringId;
        this.hand = hand;
        this.score = score;
        this.vote = false;
    }

    /**
     * Create player.
     *
     * @param stringId the stringId
     * @return the player
     */
    public static Player create(String stringId) {

        return new Player(stringId, new ArrayList<Tile>(), 0);
    }


    /**
     * Gets tiles.
     *
     * @return the tiles
     */
    public ArrayList<Tile> getTiles() {
        return this.hand;
    }

    /**
     * Sets tiles.
     *
     * @param tiles the tiles
     */
    public void setTiles(ArrayList<Tile> tiles) {
        this.hand = tiles;
    }


    /**
     * The drawTile function adds a tile to the player's hand.
     *
     *
     * @param tile Get the tile that is being drawn
     *
     * @return Nothing
     *
     * @docauthor Trelent
     */
    public void drawTile(Tile tile) {
        if(this.hand.size() < MAX_TILES){
            this.hand.add(tile);
        }
        else{
            throw new IllegalStateException("This player already has " + MAX_TILES + " tiles!");
        }
    }

    /**
     * Remove tiles.
     *
     * @param tiles the tiles
     * @return the array list
     */
    public ArrayList<Tile> removeTiles(ArrayList<Tile> tiles) {
        System.out.println("REMOVING TILES");
        ArrayList<Tile> discardHand = new ArrayList<>();
        System.out.println("tiles length: " + tiles.size());
        int i = 0;
        int j = 0;
        for (Tile t : tiles) {
            i++;
            // System.out.println("i: " + i);
            for (Tile k: getTiles()) {
                j++;
                // System.out.println("j: " + j);
                if (t.equals(k)) {
                    discardHand.add(t);
                }
            }
        }
        // System.out.println("done first loops into second");
        int p = 0;
        for (Tile t : discardHand) {
            p++;
            // System.out.println("p: " + p);
            if (getTiles().contains(t)) {
                getTiles().remove(t);
            }
        }

        return discardHand;
    }

    /**
     * Add tiles.
     *
     * @param tempHand the temp hand
     */
    public void addTiles(ArrayList<Tile> tempHand) {
        for (Tile t: tempHand) {
            hand.add(t);
        }
    }

    void removeTile(Tile t) {
        hand.remove(t);
    }

    public void setVote() {
        vote = !vote;
    }
}
