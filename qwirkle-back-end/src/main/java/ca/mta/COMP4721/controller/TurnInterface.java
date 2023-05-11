package ca.mta.COMP4721.controller;

import ca.mta.COMP4721.DTO.DiscardTilesInfo;

/**
 * The interface Turn interface.
 */
public interface TurnInterface {

    /**
     * Check valid move boolean.
     *
     * @return the boolean
     */
    public boolean checkValidMove();

    /**
     * Valid tile placement boolean.
     *
     * @return the boolean
     */
    public boolean validTilePlacement();

    /**
     * Discard tiles.
     *
     * @param discardTilesInfo the discard tiles info
     */
    void discardTiles(DiscardTilesInfo discardTilesInfo);

    void checkIfOver();

    void disconnect(String playerId);
}
