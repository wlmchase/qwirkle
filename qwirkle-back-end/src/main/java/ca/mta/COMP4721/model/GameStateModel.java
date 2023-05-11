package ca.mta.COMP4721.model;

import ca.mta.COMP4721.DTO.PlayTileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * The type Game state model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class GameStateModel {

    private ArrayList<Player> playerList = new ArrayList<>();
    private Player currentPlayer;
    private static final int STARTING_TILE_COUNT = 108;
    private static int CENTER_ROW;
    private static int CENTER_COLUMN;
    private int numberOfTiles;
    private ArrayDeque<Tile> tileBank;

    private Tile[][] board;
    private ArrayList<Tile> tempHand;

    private boolean gameStarted = false;
    private int current_row = -1;
    private int current_col = -1;
    private int score = 0;
    private ArrayList<Tile> rowList = new ArrayList<>();
    private ArrayList<Tile> colList = new ArrayList<>();
    private ArrayList<Tile> scoreList = new ArrayList<>();

    private Player winner;

    /**
     * Instantiates a new Game state model.
     *
     * @param players           the players
     * @param currentPlayer     the current player
     * @param startingTileCount the starting tile count
     * @param initTileBank      the init tile bank
     * @param board             the board
     * @param tempHand          the temp hand
     */
    public GameStateModel(ArrayList<Player> players, Player currentPlayer, int startingTileCount, ArrayDeque<Tile> initTileBank, Tile[][] board, ArrayList<Tile> tempHand, boolean gameStarted) {
        this.playerList = players;
        this.currentPlayer = currentPlayer;
        this.numberOfTiles = startingTileCount;
        this.tileBank = initTileBank;
        this.board = board;
        CENTER_ROW = board.length/2;
        CENTER_COLUMN = board.length/2;
        this.tempHand = tempHand;
        this.gameStarted = true;
    }

    /**
     * The create function creates a new GameStateModel object.
     *
     *
     * @param players Pass in the players that are currently playing
     * @param gameStarted Determine whether the game has already started or not
     *
     * @return A gamestatemodel object
     *
     * @docauthor Trelent
     */
    public static GameStateModel create(ArrayList<Player> players, boolean gameStarted) {
        return new GameStateModel(players, null, STARTING_TILE_COUNT, initTileBank(), new Tile[15][15], new ArrayList<Tile>(), gameStarted);
    }

    /**
     * The initTileBank function creates a new ArrayDeque of Tiles and populates it with the
     * tiles from the TileFactory. The function returns this newly created ArrayDeque.

     *
     *
     * @return An arraydeque of tiles
     *
     * @docauthor Trelent
     */
    private static ArrayDeque<Tile> initTileBank() {
        TileFactory tileFactory = new TileFactory();
        return tileFactory.buildTileBank();
    }

    /**
     * Add player.
     *
     * @param player the player
     */
    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

    /**
     * Draw tile tile.
     *
     * @param player the player
     * @return the tile
     */
    /**
     * The drawTile function takes a Player object as an argument and returns the tile that was drawn.
     * If there are no more tiles left in the TileBank, it prints out a message to let the user know.
     * Otherwise, it removes one tile from the bank and adds it to player's hand of tiles. It also decrements numberOfTiles by 1.

     *
     * @param player Get the player's name
     *
     * @return A tile
     *
     * @docauthor Trelent
     */
    public Tile drawTile(Player player) {
        if (numberOfTiles == 0) {
            System.out.println("No more tiles left");
            return null;
        } else {
            Tile tile = tileBank.pop();
            numberOfTiles--;
            try {
                player.drawTile(tile);
                return tile;

            } catch (IllegalStateException e) {
                tileBank.add(tile);
                numberOfTiles++;
                System.out.println("Player already has maximum tiles");
                return null;
            }
        }
    }

    /**
     * Discard tiles.
     *
     * @param player the player
     * @param tiles  the tiles
     */
    /**
     * The discardTiles function removes the tiles from a player's hand and adds them to the discard pile.
     *
     *
     * @param player Identify the player who is discarding tiles
     * @param tiles Remove the tiles from the player's hand
     *
     * @return An arraylist of the tiles that were discarded
     *
     * @docauthor Trelent
     */
    public void discardTiles(Player player, ArrayList<Tile> tiles) {
        System.out.println("in gamestate model");
        if (playerList.contains(player)) {
            ArrayList<Tile> discardHand = player.removeTiles(tiles);
            System.out.println("Discard hand: " + discardHand);
            addDiscardHand(discardHand);
            //System.out.println(tileBank.toString());
            shuffleTiles();
            //System.out.println(tileBank.toString());
        }
        else{
            System.out.println("else");
        }
    }

    private void shuffleTiles() {
        System.out.println("SHUFFLING TILES");
        ArrayList<Tile> tileList = new ArrayList<>();
        int j = 0;
        while (!tileBank.isEmpty()){
            j++;
            // System.out.println("j: " + j);
            tileList.add(tileBank.pop());
        }
        System.out.println("out of first while loop");
        Collections.shuffle(tileList);
        int i = 0;
        System.out.println("tileList size: " + tileList.size());
        while (!tileList.isEmpty()){
            tileBank.add(tileList.remove(i));
        }
        // for (int i = 0; i < tileList.size(); i++){
        //     tileBank.add(tileList.remove(i));
        // }
    }

    /**
     * The addDiscardHand function adds the tiles in discardHand to the tileBank.
     *
     *
     * @param discardHand Pass the discard hand to the adddiscardhand function
     *
     * @return Nothing
     *
     * @docauthor Trelent
     */
    private void addDiscardHand(ArrayList<Tile> discardHand) {
        for (Tile tile : discardHand) {
            tileBank.add(tile);
            numberOfTiles++;
        }

    }

    /**
     * Temp draw tile.
     *
     * @param tilesToDraw the tiles to draw
     */
    /**
     * The tempDrawTile function takes in an integer and uses it to draw that many tiles from the tile bank.
     * It then adds those tiles to the tempHand arraylist.
     *
     *
     * @param tilesToDraw Determine how many tiles to draw from the tilebank
     *
     * @return The number of tiles drawn
     *
     * @docauthor Trelent
     */
    public void tempDrawTile(int tilesToDraw) {
        for (int i = 0; i < tilesToDraw; i++) {
            tempHand.add(tileBank.pop());
            numberOfTiles--;
        }
    }

    /**
     * Find current player.
     */
    /**
     * The findCurrentPlayer function finds the player with the most cards of a particular shape or color.
     * It then sets that player to be the currentPlayer.

     *
     *
     * @return The player with the most cards of the same shape or color
     *
     * @docauthor Trelent
     */
    public void findCurrentPlayer() {
        long max_same_shape_or_color = 0;
        for (Player p : getPlayerList()) {
            long max = getMaxShapeOrColor(p);
            if (max > max_same_shape_or_color) {
                max_same_shape_or_color = max;
                setCurrentPlayer(p);
            }
        }
    }

    /**
     * The getMaxShapeOrColor function finds the maximum number of tiles in a player's hand that have the same shape or color.
     * It then sets the player's shapeMax and colorMax variables to be equal to this value, respectively.
     * If there is a tie for both values, it will set both variables to null.

     *
     * @param player Set the maximum shape or color to the player object
     *
     * @return The maximum number of tiles the player has of either shape or color
     *
     * @docauthor Trelent
     */
    public long getMaxShapeOrColor(Player player) {
        long shape = 0;
        long color = 0;

        for (Shape s : Shape.values()) {
            int shape_count = 0;
            ArrayList<Tile> tileList = new ArrayList<>();
            System.out.println("GETTING MAX SHAPE OR COLOR");
            for (Tile t : player.getTiles()) {
                if (t.getShape().equals(s)) {
                    if (!tileList.contains(t)) {
                        tileList.add(t);
                        shape_count++;
                    }
                }
            }
            if (shape_count > shape) {
                player.setShapeMax(s);
                shape = shape_count;
            }
        }

        for (Color c : Color.values()) {
            int color_count = 0;
            ArrayList<Tile> tileList = new ArrayList<>();
            for (Tile t : player.getTiles()) {
                if (t.getShape().equals(c)) {
                    if (!tileList.contains(t)) {
                        tileList.add(t);
                        color_count++;
                    }
                }
            }
            if (color_count > color) {
                player.setColorMax(c);
                color = color_count;
            }
        }

        if (shape > color) {
            player.setColorMax(null);
            return shape;
        } else {
            player.setShapeMax(null);
            return color;
        }
    }

    /**
     * Play tile.
     *
     * @param player       the player
     * @param playTileInfo the play tile info
     */
    /**
     * The playTile function places a tile on the board.
     *
     *
     * @param player Check if the player is in the list of players
     * @param playTileInfo Get the shape and color of the tile that is being played
     *
     * @return True if the tile placement is valid
     *
     * @docauthor Trelent
     */
    public boolean playTile(Player player, PlayTileInfo playTileInfo) {
        if (playerList.contains(player)) {
            if (current_row != - 1 && current_col != -1) {
                if (playTileInfo.row_position() != current_row && playTileInfo.column_position() != current_col) {
                    System.out.println("Not a valid tile placement");
                    return false;
                }
            }
            if (checkValidTilePLay(playTileInfo)) {
                if (current_row == -1 && current_col == -1) {
                    current_row = playTileInfo.row_position();
                    current_col = playTileInfo.column_position();
                    //checkValidTilePLay(playTileInfo);
                }

                placeTile(playTileInfo);
                Shape s = Shape.valueOf(playTileInfo.shape());
                Color c = Color.valueOf(playTileInfo.color());
                removeTileFromPlayer(player, s, c);
                return true;
            } else {
                System.out.println("Not a valid tile placement");
            }
        }
        return false;
    }

    /**
     * The removeTileFromPlayer function removes a tile from the player's hand.
     *
     *
     * @param player Identify the player who is being given a tile
     * @param shape Identify the shape of the tile
     * @param color Determine which color to remove from the player
     *
     * @return The tile that was removed from the player
     *
     * @docauthor Trelent
     */
    private void removeTileFromPlayer(Player player, Shape shape, Color color) {
        //ArrayList<Tile> tile = new ArrayList<>();
        Tile t = new Tile(shape, color);
        //tile.add(t);
        player.removeTile(t);
        //discardTiles(player, tile);
    }

    /**
     * The placeTile function places a tile on the board.
     *
     *
     * @param playTileInfo Get the row and column position of the tile to be placed
     *
     * @return A tile object
     *
     * @docauthor Trelent
     */
    private void placeTile(PlayTileInfo playTileInfo) {
        Shape s = Shape.valueOf(playTileInfo.shape());
        Color c = Color.valueOf(playTileInfo.color());
        Tile tileToPlace = new Tile(s, c);
        if (!rowList.isEmpty() && !colList.isEmpty()) {
//            System.out.println(rowList.toString());
//            System.out.println(colList.toString());
            scoreList.add(tileToPlace);
        }
        scoreList.add(tileToPlace);
        board[playTileInfo.row_position()][playTileInfo.column_position()] = tileToPlace;
    }

    private void tallyScore(Player player) {
        System.out.println(scoreList.toString());
        int playerScore = player.getScore();
        playerScore += checkQwirkle();
        playerScore += scoreList.size();
        player.setScore(playerScore);
        scoreList.clear();
    }

    private int checkQwirkle() {
        int qwirkle = 0;

        TreeSet shapeSet = new TreeSet<Shape>();
        TreeSet colorSet = new TreeSet<Color>();

        for (Tile t:scoreList) {
            shapeSet.add(t.getShape());
            colorSet.add(t.getColor());
        }
        if (shapeSet.size() == 6) {
            qwirkle += 6;
        }

        if (colorSet.size() == 6) {
            qwirkle += 6;
        }
        return qwirkle;
    }

    /**
     * The checkValidTilePLay function checks if the tile is valid to play.
     *
     *
     * @param playTileInfo Get the row and column position of the tile that is being played
     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    private boolean checkValidTilePLay(PlayTileInfo playTileInfo) {
        boolean validMove = false;

        int row = playTileInfo.row_position();
        int col = playTileInfo.column_position();

        if(board[row][col] == null && surroundingTile(row, col)){
            Shape s = Shape.valueOf(playTileInfo.shape());
            Color c = Color.valueOf(playTileInfo.color());
            Tile t = new Tile(s,  c);
            validMove = checkConnectedToFirstPlacedTile(row, col);
            validMove = validMove && checkRow(row, col, t) && checkColumn(row, col, t);
        }
        return validMove;
    }

    /**
     * The checkConnectedToFirstPlacedTile function checks if the tile is connected to the first placed tile.
     * If it is, then it returns true. Otherwise, false.
     *
     *
     * @param row Determine if the tile is in a row that has already been placed
     * @param col Determine the number of tiles to be placed
     *
     * @return True if the tile is connected to the first placed tile
     *
     * @docauthor Trelent
     */
    private boolean checkConnectedToFirstPlacedTile(int row, int col) {
        // first tile placed
        if (current_row != -1 && current_col != -1) {
            // tile in same row as first tile
            if (row == current_row) {
                // tile left of first tile
                if (col > current_col) {
                    // check board spaces left
                    int i = current_col;
                    while (i < col) {
                        // if no empty tiles - good
                        if (board[row][i] == null) {
                            return false;
                        }
                        i++;
                    }
                }
                // tile right of first tile
                else {
                    // check board spaces right
                    int i = current_col;
                    while (i > col) {
                        // if no empty tiles - good
                        if (board[row][i] == null) {
                            return false;
                        }
                        i--;
                    }
                }
            }
            // tile in same col as first tile
            else if (col == current_col) {
                // tile below first tile
                if (row > current_row) {
                    // check board spaces below
                    int i = current_row;
                    while (i < row) {
                        // if no empty tiles - good
                        if (board[i][col] == null) {
                            return false;
                        }
                        i++;
                    }
                }
                // tile above first tile
                else {
                    // check board spaces above
                    int i = current_row;
                    while (i > row) {
                        // if no empty tiles - good
                        if (board[i][col] == null) {
                            return false;
                        }
                        i--;
                    }
                }
            }
        }
        return true;
    }

    /**
     * The checkColumn function checks to see if the tile is in a column that has 6 tiles
     * above and below it. If there are less than 6 tiles above or below, then the function
     * returns true. Otherwise, it returns false.

     *
     * @param row Check the tiles above and below
     * @param col Determine the number of tiles in a row
     * @param t Compare the tile to the tiles in each column
     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    private boolean checkColumn(int row, int col, Tile t) {
        int tileCount = 0;

        colList.clear();

        /**
         * Check tiles below
         */
        System.out.println("CHECKING COLUMN");
        for (int i = (row + 1); i < (row + 6); i++) {
            if (i < board[row].length) {
                if (board[i][col] != null) {
                    tileCount++;
                    Tile tile = board[i][col];
                    if (!(t.getColor().equals(tile.getColor()) ^ t.getShape().equals(tile.getShape()))) {
                        return false;
                    }
                    if (board[i][col].equals(t)) {
                        return false;
                    }
                    if (!colList.contains(tile)) {
                        colList.add(tile);
                    }
                } else {
                    break;
                }
            }
        }

        /**
         * Check tiles above
         */
        for (int i = row - 1; i > row - 6; i--) {
            if (i > 0) {
                if (board[i][col] != null) {
                    tileCount++;
                    Tile tile = board[i][col];
                    if (!(t.getColor().equals(tile.getColor()) ^ t.getShape().equals(tile.getShape()))) {
                        return false;
                    }
                    if (board[i][col].equals(t)) {
                        return false;
                    }
                    if (!colList.contains(tile)) {
                        colList.add(tile);
                    }
                } else {
                    break;
                }
            }
        }

        if (tileCount != 6) {
            addToScoreList(colList);
            return true;
        }
        return false;
    }

    private boolean checkRow(int row, int col, Tile t) {
        int tileCount = 0;

        rowList.clear();

        /**
         * Check tiles right
         */
        System.out.println("CHECKING ROW");
        for (int i = (col + 1); i < (col + 6); i++) {
            if (i < board[row].length) {
                if (board[row][i] != null) {
                    tileCount++;
                    Tile tile = board[row][i];
                    if (!(t.getColor().equals(tile.getColor()) ^ t.getShape().equals(tile.getShape()))) {
                        return false;
                    }
                    if (board[row][i].equals(t)) {
                        return false;
                    }
                    if (!rowList.contains(tile)) {
                        rowList.add(tile);
                    }
                } else {
                    break;
                }
            }
        }

        /**
         * Check tiles left
         */
        for (int i = (col - 1); i > (col - 6); i--) {
            if (i > 0) {
                if (board[row][i] != null) {
                    tileCount++;
                    Tile tile = board[row][i];
                    if (!(t.getColor().equals(tile.getColor()) ^ t.getShape().equals(tile.getShape()))) {
                        return false;
                    }
                    if (board[row][i].equals(t)) {
                        return false;
                    }
                    if (!rowList.contains(tile)) {
                        rowList.add(tile);
                    }
                } else {
                    break;
                }
            }
        }

        if (tileCount != 6) {
            addToScoreList(rowList);
            return true;
        }
        return false;
    }

    private void addToScoreList(ArrayList<Tile> tileList) {
        System.out.println("ADDING SCORE");
        for (Tile t : tileList) {
            if (!scoreList.contains(t)) {
                scoreList.add(t);
            }
        }
    }

    private boolean surroundingTile(int row, int col){
        return checkNorth(row - 1, col) || checkSouth(row + 1, col) || checkEast(row, col + 1) || checkWest(row, col - 1);
    }

    /**
     * The checkWest function checks if there is a piece to the west of the current position.
     *
     *
     * @param row Check the row of the current cell
     * @param col Represent the column of the cell
     *
     * @return True if there is a piece to the west of
     *
     * @docauthor Trelent
     */
    private boolean checkWest(int row, int col) {
        if (col == -1) {
            return false;
        }
        if (board[row][col] != null) {
            return true;
        }
        return false;
    }

    /**
     * The checkEast function checks if the space to the right of a given piece is occupied.
     *
     *
     * @param row Check the row of the current cell
     * @param col Determine the column of the piece
     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    private boolean checkEast(int row, int col) {
        if (col == 15) {
            return false;
        }
        if (board[row][col] != null) {
            return true;
        }
        return false;
    }

    /**
     * The checkSouth function checks if the south direction is valid.
     *
     *
     * @param row Determine the row of the piece that is being moved
     * @param col Determine the column
     *
     * @return True if the cell in row and col is not null
     *
     * @docauthor Trelent
     */
    private boolean checkSouth(int row, int col) {
        if (row == 15) {
            return false;
        }
        if (board[row][col] != null) {
            return true;
        }
        return false;
    }

    /**
     * The checkNorth function checks if the north of a given cell is occupied.
     *
     *
     * @param row Determine the row of the board that is being checked
     * @param col Determine the column of the checknorth function

     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    private boolean checkNorth(int row, int col) {
        if (row == -1) {
            return false;
        }
        if (board[row][col] != null) {
            return true;
        }
        return false;
    }

    /**
     * Place starting tiles.
     */
    public void placeStartingTiles() {
        ArrayList<Tile> startingTiles = getMaxTiles(currentPlayer);
        for (Tile t : startingTiles) {
            board[CENTER_ROW][CENTER_COLUMN] = t;
            CENTER_ROW++;
        }
    }

    /**
     * The getMaxTiles function takes a Player object as an argument and returns
     * the maximum number of tiles that can be played on the board. The function
     * first checks if there is a shapeMax attribute in the player object, and if so,
     * it iterates through all of the player's tiles to find those with that shape. If
     * no such attribute exists, then it iterates through all of its tiles to find those
     * with that color. Once these are found, they are removed from their respective lists

     *
     * @param player Get the player's name
     *
     * @return A list of tiles that are the maximum value
     *
     * @docauthor Trelent
     */
    private ArrayList<Tile> getMaxTiles(Player player) {
        System.out.println("GETTING MAX TILES");
        ArrayList<Tile> maxTiles = new ArrayList<>();
        if (player.getShapeMax() != null) {
            for (Tile t: player.getTiles()) {
                if (t.getShape().equals(player.getShapeMax())) {
                    if (!maxTiles.contains(t)) {
                        maxTiles.add(t);
                    }
                }
            }
        } else {
            for (Tile t: player.getTiles()) {
                if (t.getColor().equals(player.getColorMax())) {
                    if (!maxTiles.contains(t)) {
                        maxTiles.add(t);
                    }
                }
            }
        }
        for (Tile t: maxTiles) {
            player.removeTile(t);
        }
        return maxTiles;
    }

    private void drawTiles(Player currentPlayer, int size) {
        while (size > 0) {
            drawTile(currentPlayer);
            size--;
        }
    }

    /**
     * The endTurn function is called when a player has finished their turn.
     * It draws tiles for the current player and then changes the currentPlayer to be the next in line.
     *
     *
     * @param playerId Identify the player who is currently playing
     *
     * @return The player's score
     *
     * @docauthor Trelent
     */
    public void endTurn(String playerId) {
        for (Player p : getPlayerList()) {
            if (p.getStringId().equals(playerId)) {
                tallyScore(p);
                resetConstants();
                drawTiles(p, 6 - p.getTiles().size());
                updateCurrentPlayer(p);
            }
        }
    }

    private void resetConstants() {
        this.current_row = -1;
        this.current_col = -1;
        this.score = 0;
    }

    public boolean isJoinable() {
        return !isGameStarted();
    }

    private void updateCurrentPlayer(Player p) {
        int index = getPlayerList().indexOf(p);
        if (index == getPlayerList().size() - 1) {
            index = -1;
        }
        index++;
        currentPlayer = getPlayerList().get(index);
    }

    public void disconnectPlayer(Player p) {
        //TODO
        // add tiles back to bank if >= 2 players remaining
        if (currentPlayer.equals(p)) {
            System.out.println("CURRENT PLAYER");
            updateCurrentPlayer(p);
        }
        ArrayList<Tile> player_hand = p.getHand();
        discardTiles(p, player_hand);
        System.out.println("DISCARD HAND");
        playerList.remove(p);
        System.out.println("PLAYER REMOVED");
    }
}
