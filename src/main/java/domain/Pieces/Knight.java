package domain.Pieces;

import domain.Pair;
import domain.Color.ColorType;

public class Knight extends Piece {
    public static final PieceType TYPE = PieceType.KNIGHT;

    public Knight(ColorType color){
        super(TYPE, color);
    }
    @Override
    public String toString() {
        return "?";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        // 3 y, 1 x
        return false;
    }
    
}
