package ca.mta.COMP4721.controller;

import ca.mta.COMP4721.DTO.DiscardTilesInfo;
import ca.mta.COMP4721.DTO.PlayTileInfo;
import ca.mta.COMP4721.DTO.PlayerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * The type Web socket game controller.
 */
@CrossOrigin
@RestController
public class WebSocketGameController {

    //@Autowired
    //private GameStateController gameStateController;
    @Autowired
    private GameSetController gameSetController;
    @Autowired
    private SimpMessagingTemplate template;

//    /**
//     * Join game.
//     */
//    public void joinGame() {
//        Player player = new Player();
//        //playerController.createPlayer();
//
//        if (gameController.listJoinableGames().getStatusCode() == HttpStatus.NOT_FOUND) {
//            Game game = new Game();
//            gameController.createGame(game);
//            gameController.addPlayer(game.getId(), player);
//        } else {
//            //List<Game> games = gameController.listJoinableGames();
//        }
//    }


    /**
     * The joinGame function is used to add a player to the game.
     *
     *
     * @param playerInfo Pass the playerid to the joingame function
     *
     * @return A response entity which is the http status code for a successful request
     *
     * @docauthor Trelent
     */
//    @PostMapping("/join")
//    public ResponseEntity<Void> joinGame(@RequestBody PlayerInfo playerInfo) {
//        System.out.println("Received join request from player " + playerInfo);
//        if(gameStateController.getGameState() != null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        Player player = gameStateController.joinGame(playerInfo.playerId());
//        if(player != null) {
//            template.convertAndSend("/game/playerInfo", player);
//            if(gameStateController.getGameState() != null) {
//                System.out.println(gameStateController.getGameState().toString());
//                template.convertAndSend("/game/gameState", gameStateController.getGameState());
//            }
//            System.out.println("Successfully added player " + playerInfo );
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        System.out.println("Could not add player " + playerInfo);
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

    /**
     * The joinRandom function is used to join a random game.
     *
     * @param playerInfo Pass the player's id to the joingame method
     * @return A void because it is a post request
     * @docauthor Trelent
     */
    @PostMapping("/joinRandom")
    public ResponseEntity<String> joinRandom(@RequestBody PlayerInfo playerInfo) {
        System.out.println("Received join request from player " + playerInfo + " for a random game");
        try {
            if(gameSetController.getGame() == null) {
                return createQuickGame(playerInfo);
            } else {
                String gameId = gameSetController.joinGame(playerInfo.playerId());
                System.out.println(gameId);
                System.out.println("GAME SET: " + gameSetController.getGameSet().toString());
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        broadcastGameInfo(gameId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                return ResponseEntity.ok().body(gameId);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }


    /**
     * The joinGameByCode function is used to join a game by code
     *
     *
     * @param gameCode the game code
     * @param playerInfo Pass the player's id to the joingame method
     *
     * @return A void because it is a post request
     *
     * @docauthor Trelent
     */
    @PostMapping("/joinByCode")
    public ResponseEntity<Void> joinGameByCode(@RequestParam String gameCode, @RequestBody PlayerInfo playerInfo) {
        System.out.println("Received join request from player " + playerInfo + " for game " + gameCode);
        if(gameSetController.getGame(gameCode) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            gameSetController.joinGame(gameCode, playerInfo.playerId());
            //broadcastGameInfo(gameCode);
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                    broadcastWaitingRoom(gameCode);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * The voteToStart function is used to allow a player to vote for the game to start.
     *
     *
     * @param gameCode Identify the game
     * @param @RequestBody Pass the playerinfo object to the votetostart function
     *
     * @return The status code ok
     *
     * @docauthor Trelent
     */
    @PostMapping("/voteToStart")
    public ResponseEntity<Void> voteToStart(@RequestParam String gameCode, @RequestBody PlayerInfo playerInfo) {
        System.out.println("Received vote to start request from player " + playerInfo);
        if(gameSetController.getGame(gameCode) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            gameSetController.voteToStart(gameCode, playerInfo.playerId());
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                    broadcastWaitingRoom(gameCode);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
            if (gameSetController.lobbyReady(gameCode)) {
                broadcastGameInfo(gameCode);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * The createLobby function creates a new game and returns the id of that game.
     *
     *
     * @param playerInfo Pass the player's name and ip address to the server
     *
     * @return A new gameid
     *
     * @docauthor Trelent
     */
    @PostMapping("/createLobby")
    public ResponseEntity<String> createLobby(@RequestBody PlayerInfo playerInfo) {
        System.out.println("Received create lobby request from player " + playerInfo);
        try {
            String gameId = gameSetController.createGame();
            joinGameByCode(gameId, playerInfo);
            System.out.println("GAME ID: " + gameId);
            //broadcastGameInfo(gameId);
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                    broadcastWaitingRoom(gameId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
            return ResponseEntity.ok().body(gameId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * The createQuickGame function creates a new game and returns the id of that game.
     *
     *
     * @param playerInfo Pass the player's name and ip address to the server
     *
     * @return A new gameid
     *
     * @docauthor Trelent
     */
    @PostMapping("/createQuickGame")
    public ResponseEntity<String> createQuickGame(@RequestBody PlayerInfo playerInfo) {
        System.out.println("Received create quick game request from player " + playerInfo);
        try {
            String gameId = gameSetController.createQuickGame();
            System.out.println(gameId);
            System.out.println(gameSetController.getGameSet().toString());
            joinGameByCode(gameId, playerInfo);
            //broadcastGameInfo(gameId);
            return ResponseEntity.ok().body(gameId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * The disconnect function disconnects a player of a game
     *
     *
     * @param gameCode the game code
     * @param playerInfo Pass the player's name and ip address to the server
     *
     * @docauthor Trelent
     */
    @PostMapping("/disconnect")
    public ResponseEntity<Void> disconnect(@RequestParam String gameCode, @RequestBody PlayerInfo playerInfo) {
        System.out.println("Received disconnect game request from player " + playerInfo);
        if(gameSetController.getGame(gameCode) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            gameSetController.disconnect(gameCode, playerInfo.playerId());
            disconnectWaitRoom(gameCode, playerInfo);
//            if (gameSetController.needsReset(gameCode)) {
//                gameSetController.resetGame(gameCode);
//            }
            broadcastGameInfo(gameCode);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }


    @PostMapping("/disconnectWaitRoom")
    public ResponseEntity<Void> disconnectWaitRoom(@RequestParam String gameCode, @RequestBody PlayerInfo playerInfo) {
        System.out.println("Received disconnect waiting room request from player " + playerInfo);
        if(gameSetController.getGame(gameCode) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            gameSetController.disconnectWaitingRoom(gameCode, playerInfo.playerId());
            if (gameSetController.getGame(gameCode) != null) {
                broadcastWaitingRoom(gameCode);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

//    /**
//     * The getGameState function returns the current state of a game.
//     *
//     *
//     * @param gameCode Specify the game code
//     *
//     * @return A gamestatemodel object
//     *
//     * @docauthor Trelent
//     */
//    @GetMapping("/gameState")
//    public ResponseEntity<GameStateModel> getGameState(@RequestParam String gameCode) throws Exception {
//        GameStateModel gameState = gameSetController.getGameState(gameCode);
//        if(gameState == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return ResponseEntity.ok().body(gameState);
//    }

    /**
     * The getWaitingRoom function returns the waiting room of the game.
     *
     * @return The waiting room
     *
     * @docauthor Trelent
     */
//    @SendTo("/game/waitingRoom")
//    @GetMapping("/waitingRoom")
//    public ResponseEntity<WaitingRoom> getWaitingRoom() {
//        WaitingRoom waitingRoom = gameStateController.getWaitingRoom();
//        if(waitingRoom == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return ResponseEntity.ok().body(waitingRoom);
//    }


    public void broadcastGameInfo(String gameId) throws Exception {
        //System.out.println(gameId);
        //System.out.println(gameSetController.getGameState(gameId).toString());
        template.convertAndSend("/games/gameState/" + gameId, gameSetController.getGameState(gameId));
        System.out.println("gamestate sent");
    }

    public void broadcastWaitingRoom(String gameId) throws Exception {
        //System.out.println(gameId);
        //System.out.println(gameSetController.getGameState(gameId).toString());
        template.convertAndSend("/games/waitingRoom/" + gameId, gameSetController.getWaitingRoom(gameId));
        System.out.println("waitingRoom sent");
    }

    /**
     * The playTile function is used to play a tile on the board.
     *
     *
     * @param playTileInfo Pass the information about the tile to be played
     *
     * @return A httpstatus
     *
     * @docauthor Trelent
     */
    @PostMapping("/playTile")
    public ResponseEntity<Void> playTile(@RequestParam String gameCode, @RequestBody PlayTileInfo playTileInfo) {
        System.out.println("Received play tile request from player " + playTileInfo);
        if(gameSetController.getGame(gameCode) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            gameSetController.playTile(gameCode, playTileInfo);
            broadcastGameInfo(gameCode);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * The discardTiles function takes in a DiscardTilesInfo object and discards the tiles specified by the user.
     * If there are not enough tiles to discard, then an error is returned.

     *
     * @param discardTilesInfo Specify which tiles to discard
     *
     * @return A void response
     *
     * @docauthor Trelent
     */
    @PostMapping("/discardTiles")
    public ResponseEntity<Void> discardTiles(@RequestParam String gameCode, @RequestBody DiscardTilesInfo discardTilesInfo) {
        System.out.println("Received discard tile request from player " + discardTilesInfo);
        if(gameSetController.getGame(gameCode) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            gameSetController.discardTile(gameCode, discardTilesInfo);
            broadcastGameInfo(gameCode);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * The endTurn function is called when a player ends their turn.
     * It takes in the PlayerInfo object that contains information about the player who ended their turn, and updates the game state accordingly.
     * The function then sends an updated version of the game state to all players via WebSocket.

     *
     * @param playerInfo Determine which player's turn it is
     *
     * @return A status code of 200
     *
     * @docauthor Trelent
     */
    @PostMapping("/endTurn")
    public ResponseEntity<Void> endTurn(@RequestParam String gameCode, @RequestBody PlayerInfo playerInfo) {
        System.out.println("Received end turn request from player " + playerInfo);
        if(gameSetController.getGame(gameCode) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            gameSetController.endTurn(gameCode, playerInfo);
            broadcastGameInfo(gameCode);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

//    /**
//     * Discard tiles response entity.
//     *
//     * @param discardTilesInfo the discard tiles info
//     * @return the response entity
//     */
//    /**
//     * The discardTiles function takes in a DiscardTilesInfo object and discards the tiles specified by the user.
//     * If there are not enough tiles to discard, then an error is returned.
//
//     *
//     * @param DiscardTilesInfo Specify which tiles to discard
//     *
//     * @return A void response
//     *
//     * @docauthor Trelent
//     */
//    @PostMapping("/discardTiles")
//    public ResponseEntity<Void> discardTiles(@RequestBody DiscardTilesInfo discardTilesInfo) {
//        System.out.println("Received discard tile request" + discardTilesInfo.playerId());
//        if (gameStateController.getGameState().getNumberOfTiles() < discardTilesInfo.tiles().size()) {
//            System.out.println("Received discard tile request but there are not enough tiles to draw");
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        try{
//            System.out.println("Recieved request to discard tiles");
//            gameStateController.discardTiles(discardTilesInfo);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
//        }
//    }
}
