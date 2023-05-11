package ca.mta.COMP4721.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Board.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    private Tile[][] board;

}
