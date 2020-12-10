package domain.Pieces;

import domain.Pair;

import java.util.List;

import domain.Color.ColorType;

public class Rook extends Piece {
    public static final PieceType TYPE = PieceType.ROOK;

    public Rook(ColorType color) {
        super(TYPE, color);

    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        // only straights get x and y
        return true;
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
