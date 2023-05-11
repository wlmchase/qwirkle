package ca.mta.COMP4721.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Tile.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tile {

    private Shape shape;
    private Color color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return shape == tile.shape && color == tile.color;
    }
}
