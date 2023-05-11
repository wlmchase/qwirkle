package ca.mta.COMP4721.model;

import ca.mta.COMP4721.DTO.DiscardTilesInfo;
import ca.mta.COMP4721.DTO.PlayTileInfo;
import ca.mta.COMP4721.DTO.PlayerInfo;
import ca.mta.COMP4721.controller.GameStateController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Game.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private String stringId;

    private GameStateController gameStateController;

    public Game(String randomId) {
        this.stringId = randomId;
        this.gameStateController = new GameStateController();
    }

    public Game(String randomId, int maxPlayers) {
        this.stringId = randomId;
        this.gameStateController = new GameStateController();
        gameStateController.getWaitingRoom().setSize(maxPlayers);
    }


    public String joinGame(String playerId) {
        gameStateController.joinGame(playerId);
        return stringId;
    }


    public GameStateModel getGameState() {
        return gameStateController.getGameState();
    }

    public void disconnect(String playerId) {
        gameStateController.disconnect(playerId);
    }

    public void playTile(PlayTileInfo playTileInfo) {
        gameStateController.playTile(playTileInfo);
    }

    public void discardTile(DiscardTilesInfo discardTilesInfo) {
        gameStateController.discardTiles(discardTilesInfo);
    }

    public void endTurn(PlayerInfo playerInfo) {
        gameStateController.endTurn(playerInfo);
    }

    public boolean isJoinable() {
        return gameStateController.getWaitingRoom().isJoinable();
    }

    public void voteToStart(String playerId) {
        gameStateController.voteToStart(playerId);
    }

    public boolean isReady() {
        return gameStateController.lobbyIsReady();
    }
}
