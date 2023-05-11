package ca.mta.COMP4721.controller;

import ca.mta.COMP4721.DTO.DiscardTilesInfo;
import ca.mta.COMP4721.DTO.PlayTileInfo;
import ca.mta.COMP4721.DTO.PlayerInfo;
import ca.mta.COMP4721.model.Game;
import ca.mta.COMP4721.model.GameStateModel;
import ca.mta.COMP4721.model.WaitingRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class GameSetController {


    HashMap<String, Game> gameSet = new HashMap<>();

    ArrayDeque<String> quickGame = new ArrayDeque<>();

    /**
     * The createGame function creates a new game and returns the id of that game.
     *
     * @return A string that is a random id
     *
     * @docauthor Trelent
     */
    public String createGame() {
        System.out.println("IN CREATE GAME");
        String randomId;
        System.out.println(gameSet.toString());
        while(gameSet.containsKey(randomId = generateRandomId())){
            System.out.println(randomId);
        }
        System.out.println("AFTER WHILE");
        Game game = new Game(randomId, 4);
        gameSet.put(randomId, game);
        System.out.println("AFTER PUT");
        System.out.println(gameSet.toString());
        return randomId;
    }

    public String createQuickGame() {
        String randomId;
        while(gameSet.containsKey(randomId = generateRandomId())){}
        Game game = new Game(randomId);
        gameSet.put(randomId,game);
        quickGame.push(randomId);
        System.out.println("QUEUE: " + quickGame.toString());
        return randomId;
    }

    /**
     * from https://www.baeldung.com/java-random-string
     */
    private String generateRandomId() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 4;
        Random random = new Random();

        String randomId = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();
        return randomId;
    }


    public String getGame() {
        System.out.println("IN GET GAME");
        System.out.println("QUEUE: " + getQuickGame().toString());
        if (!quickGame.isEmpty()) {
            return quickGame.peek();
        } else {
            return null;
        }
    }

    public String popGame() {
        if (!quickGame.isEmpty()) {
            return quickGame.pop();
        }
        return null;
    }

    public Game getGame(String roomCode) {
        System.out.println(gameSet.toString());
        if (gameSet.containsKey(roomCode)) {
            return gameSet.get(roomCode);
        } else {
            return null;
        }
    }

    public String joinGame(String playerId) throws Exception {
        System.out.println("POP QUEUE");
        String gameId = popGame();
        joinGame(gameId, playerId);
        return gameId;
    }

    public String joinGame(String gameCode, String playerId) throws Exception {
        System.out.println("IN JOIN GAME");
        System.out.println(getGame(gameCode).getStringId());
        if (getGame(gameCode).isJoinable()) {
            System.out.println("JOINABLE");
            getGame(gameCode).joinGame(playerId);
            return getGame(gameCode).getStringId();
        }
        throw new Exception("Game is not join-able");
    }

    public void voteToStart(String gameCode, String playerId) {
        getGame(gameCode).voteToStart(playerId);
    }

    public GameStateModel getGameState(String gameCode) throws Exception {
        if (getGame(gameCode) != null) {
            return getGame(gameCode).getGameState();
        }
        throw new Exception("Game does not exist");
    }

    public void disconnect(String gameCode, String playerId) {
        getGame(gameCode).disconnect(playerId);
    }

    public void playTile(String gameCode, PlayTileInfo playTileInfo) {
        getGame(gameCode).playTile(playTileInfo);
    }

    public void discardTile(String gameCode, DiscardTilesInfo discardTilesInfo) {
        getGame(gameCode).discardTile(discardTilesInfo);
    }

    public void endTurn(String gameCode, PlayerInfo playerInfo) {
        getGame(gameCode).endTurn(playerInfo);
    }

    public WaitingRoom getWaitingRoom(String gameId) {
        return getGame(gameId).getGameStateController().getWaitingRoom();
    }

    public boolean lobbyReady(String gameCode) {
        return getGame(gameCode).isReady();
    }

    public void disconnectWaitingRoom(String gameCode, String playerId) {
        getGame(gameCode).getGameStateController().disconnectFromLobby(playerId);
        if (getGame(gameCode).getGameStateController().getWaitingRoom().getWaitingPlayerList().size() == 0) {
            delete(gameCode);
        }
    }

    public void delete(String gameCode) {
        gameSet.remove(gameCode);
    }

    public boolean needsReset(String gameCode) {
        if (getGame(gameCode).getGameStateController().getWaitingRoom().getWaitingPlayerList().size() == 1) {
            return true;
        } else {
            return false;
        }
    }

//    public void resetGame(String gameCode) {
//        getGame(gameCode).getGameStateController().resetGame();
//    }
}
