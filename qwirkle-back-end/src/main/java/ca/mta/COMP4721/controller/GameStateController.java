package ca.mta.COMP4721.controller;

import ca.mta.COMP4721.DTO.DiscardTilesInfo;
import ca.mta.COMP4721.DTO.PlayTileInfo;
import ca.mta.COMP4721.DTO.PlayerInfo;
import ca.mta.COMP4721.model.GameStateModel;
import ca.mta.COMP4721.model.Player;
import ca.mta.COMP4721.model.WaitingRoom;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * The type Game state controller.
 */
@Component
@Data
public class GameStateController implements TurnInterface {

    private GameStateModel gameStateModel;
    private WaitingRoom waitingRoom;
    private boolean lobbyReady = false;

    /**
     * Instantiates a new Game state controller.
     */
    public GameStateController() {
        this.waitingRoom = new WaitingRoom();
        setupGame();
    }


    /**
     * The checkIfCurrentPlayer function checks if the playerId passed in is equal to the currentPlayer's id.
     * If it is, then it returns true, otherwise false.

     *
     * @param playerId Determine if the current player is the one who has to play
     *
     * @return True if the playerid passed in is equal to the currentplayer
     *
     * @docauthor Trelent
     */
    boolean checkIfCurrentPlayer(String playerId) {
        if (gameStateModel.getCurrentPlayer().getStringId().equals(playerId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * The endTurn function is called when the current player ends their turn.
     * It will check if the current player is indeed the one who ended their turn, and if so it will end that players turn.
     * If not, then it will print out a message saying that this isn't the correct player to end their turn.

     *
     * @param playerInfo Get the playerid of the current player
     *
     * @return Nothing
     *
     * @docauthor Trelent
     */
    public void endTurn(PlayerInfo playerInfo) {
        if (checkIfCurrentPlayer(playerInfo.playerId())) {
            gameStateModel.endTurn(playerInfo.playerId());
        } else {
            System.out.println("Not the current player");
        }
    }

    @Override
    public boolean checkValidMove() {
        return false;
    }

    @Override
    public boolean validTilePlacement() {
        return false;
    }

    /**
     * The discardTiles function takes in a DiscardTilesInfo object and discards the tiles
     * from the player's hand. It also updates the game state model to reflect this change.

     *
     * @param discardTilesInfo Get the tiles to be discarded
     *
     * @return A void
     *
     * @docauthor Trelent
     */
    @Override
    public void discardTiles(DiscardTilesInfo discardTilesInfo) {
        System.out.println("Discarding tiles");
        String playerId = discardTilesInfo.playerId();
        for (Player p: gameStateModel.getPlayerList()) {
            if (p.getStringId().equals(playerId))   {
                System.out.println("GameStateController: inside if");
                gameStateModel.discardTiles(p, discardTilesInfo.tiles());
            }
        }
        System.out.println("GameStateController: outside of loop");
    }

    /**
     * The tempDrawTiles function is used to draw a number of tiles from the player's hand
     * and place them in the discard pile. The function takes two parameters, p (the player)
     * and tilesToDraw (the number of tiles to draw). It then draws that many tiles from
     * the player's hand, places them in the discard pile, and updates all relevant game state
     * variables accordingly. This function does not return anything.

     *
     * @param tilesToDraw Determine how many tiles to draw
     *
     * @return The number of tiles that were drawn
     *
     * @docauthor Trelent
     */
    private void tempDrawTiles(int tilesToDraw) {
        gameStateModel.tempDrawTile(tilesToDraw);
    }

    /**
     * The joinGame function takes a playerId as an argument and adds the player to the waiting room.
     * If there are two players in the waiting room, it sets up a game with those two players.
     * It then finds which of those two players is currentPlayer and places starting tiles for them.

     *
     * @param playerId Create a new player object
     *
     * @return A player object
     *
     * @docauthor Trelent
     */
    //TODO
    // redo join for 4 players and vote to start
    public Player joinGame(String playerId) {
        if(this.waitingRoom.getWaitingPlayerList().size() < waitingRoom.getSize() && waitingRoom.getWaitingPlayerList().stream().filter(player -> player.getStringId().equals(playerId)).count() == 0) {
            try {
                Player player = Player.create(playerId);
                this.waitingRoom.addPlayer(player);
                if (waitingRoom.getSize() == 2) {
                    voteToStart(playerId);
                }
                // doesn't need to return a player now
                return player;
            } catch(IllegalArgumentException e) {
                System.out.println("Invalid playerId " + playerId + " provided");
            }
        }
        return null;
    }

    public boolean lobbyIsReady() {
        return lobbyReady;
    }

    public void voteToStart(String playerId) {
        int index = 0;
        for (Player p : waitingRoom.getWaitingPlayerList()) {
            if (p.getStringId().equals(playerId)) {
                p.setVote();
                index = waitingRoom.getWaitingPlayerList().indexOf(p);
            }
        }
        waitingRoom.updateVote(playerId, index);
        System.out.println("IN VOTE TO START");
        if (waitingRoom.allReady() && (waitingRoom.getWaitingPlayerList().size() >= 2)) {
            System.out.println("ALL READY");
            prepareGame();
            lobbyReady = true;
        }
    }

    private void prepareGame() {
        setupGame();
        findCurrentPlayer();
        placeStartingTiles();
    }

    /**
     * The placeStartingTiles function places the starting tiles for each player.
     *
     *
     *
     * @return A list of tiles that are placed on the board
     *
     * @docauthor Trelent
     */
    private void placeStartingTiles() {
        gameStateModel.placeStartingTiles();
        drawTiles(gameStateModel.getCurrentPlayer());
    }

    /**
     * The findCurrentPlayer function is used to find the current player.
     *
     *
     *
     * @return The player that is currently playing
     *
     * @docauthor Trelent
     */
    private void findCurrentPlayer() {
        gameStateModel.findCurrentPlayer();
    }

    /**
     * The setupGame function is used to set up the game by creating a new GameStateModel object and
     * adding all of the players in the waiting room to it. It also draws tiles for each player, which
     * are stored in their hand. The function then returns this newly created GameStateModel object.

     *
     *
     * @return A gamestatemodel object
     *
     * @docauthor Trelent
     */
    private void setupGame() {
        System.out.println("IN SETUP GAME");
        if(gameStateModel == null) {
            System.out.println("NULL STATE");
            gameStateModel = GameStateModel.create(new ArrayList<>(), false);
        } else {
            System.out.println("CREATED STATE");
            gameStateModel.setPlayerList(waitingRoom.getWaitingPlayerList());
            gameStateModel.setGameStarted(true);
            for (Player player : gameStateModel.getPlayerList()) {
                drawTiles(player);
            }
        }
    }

    /**
     * The drawTiles function draws tiles for the player.
     *
     *
     * @param player Determine which player is being dealt tiles
     *
     * @return A void
     *
     * @docauthor Trelent
     */
    private void drawTiles(Player player) {
        int i = player.getTiles().size();
        while (i < 6) {
            gameStateModel.drawTile(player);
            i++;
        }
    }

    /**
     * Gets game state.
     *
     * @return the game state
     */
    public GameStateModel getGameState() {
        return gameStateModel;
    }

    /**
     * The playTile function is used to play a tile on the board.
     *
     *
     * @param playTileInfo Pass the information about the player and tile to be played
     *
     * @return Nothing
     *
     * @docauthor Trelent
     */
    public boolean playTile(PlayTileInfo playTileInfo) {
        if (checkIfCurrentPlayer(playTileInfo.playerId())) {
            String playerId = playTileInfo.playerId();
            for (Player p : gameStateModel.getPlayerList()) {
                if (p.getStringId().equals(playerId))   {
                    return gameStateModel.playTile(p, playTileInfo);
                }
            }
        }
        return false;
    }

    //Method currently both checks if over, and ends the game if it is over
    //Might be worth splitting in the future
    public void checkIfOver() {
        if (gameStateModel.getNumberOfTiles() == 0) {
            //coded assuming that this is called BEFORE player swap
            //TODO if broken: make it so it checks every player's hand, because why not if its broken
            if (gameStateModel.getCurrentPlayer().getHand().isEmpty()) {
                gameStateModel.setWinner(gameStateModel.getCurrentPlayer());
                //temporary arrangement just to establish that the game is over, in the future, this should be handled by the frontend
                System.out.println("One player has run out of tiles, the game is now over.");
                //TODO: add a print for players score, in order, when available from the game model
                for(int i = gameStateModel.getPlayerList().size() - 1; i >= 0; i--){
                    //scorched earth, to prevent further play
                    gameStateModel.getPlayerList().get(i).getTiles().clear();
                }
            }
        }
    }

    @Override
    public void disconnect(String playerId) {
        System.out.println("IN DISCONNECT");
        Player player = null;
        for (Player p : gameStateModel.getPlayerList()) {
            if (p.getStringId().equals(playerId)) {
                player = p;
            }
        }
        if (player != null) {
            gameStateModel.disconnectPlayer(player);
        }
        //TODO
        // delete game if player count < 2
        if (gameStateModel.getPlayerList().size() <= 1) {
            System.out.println("DELETE GAME");
            deleteGame();
        }
    }

    private void deleteGame() {
        gameStateModel.setGameStarted(false);
    }


    public void disconnectFromLobby(String playerId) {
        Player player = null;
        for (Player p : waitingRoom.getWaitingPlayerList()) {
            if (p.getStringId().equals(playerId)) {
                player = p;
            }
        }
        if (player != null) {
            waitingRoom.removePlayer(player);
        }
    }
}
