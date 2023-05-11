package ca.mta.COMP4721.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Waiting room.
 */
@Component
@AllArgsConstructor
public class WaitingRoom {

    private ArrayList<Player> waitingPlayers;
    private HashMap<String, Boolean> votes;
    private boolean voted[];
    private int waitingRoomSize;
    private int WAITING_MAX_SIZE = 2;


    /**
     * Instantiates a new Waiting room.
     */
    public WaitingRoom() {
        this.waitingPlayers = new ArrayList<>();
        this.votes = new HashMap<>();
        this.voted = new boolean[4];
        this.waitingRoomSize = 0;
    }

    /**
     * Gets waiting player list.
     *
     * @return the waiting player list
     */
    public ArrayList<Player> getWaitingPlayerList() {
        return waitingPlayers;
    }

    /**
     * Add player.
     *
     * @param player the player
     */
    public void addPlayer(Player player) {
        waitingPlayers.add(player);
        votes.put(player.getStringId(), false);
        this.waitingRoomSize++;
    }

    public void updateVote(String playerId, int index) {
        boolean vote = votes.get(playerId);
        votes.put(playerId, !vote);
        voted[index] = !vote;
    }

    public boolean allReady() {
        boolean ready = true;
        for (Boolean b : votes.values()) {
            ready = ready && b;
        }
        return ready;
    }

    public boolean isJoinable() {
        if (this.waitingRoomSize < 4) {
            return true;
        } else {
            return false;
        }
    }

    public int getSize() {
        return WAITING_MAX_SIZE;
    }

    public void setSize(int maxPlayers) {
        this.WAITING_MAX_SIZE = maxPlayers;
    }

    public void removePlayer(Player p) {
        this.waitingRoomSize--;
        waitingPlayers.remove(p);
    }
}
