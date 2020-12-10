package domain.Pieces;

import domain.Pair;
import domain.Color.ColorType;

public class King extends Piece {
    public static final PieceType TYPE = PieceType.KING;

    public King(ColorType color) {
        super(TYPE, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        return true;
    }

}
