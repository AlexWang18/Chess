package domain.Pieces;

import domain.Pair;

import java.util.List;

import domain.Color.ColorType;

public class Queen extends Piece {
    public static final PieceType TYPE = PieceType.QUEEN;

    public Queen(ColorType color) {
        super(TYPE, color);
    }

    @Override
    public String toString() {
        return "Q";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        // TODO Auto-generated method stub
        return null;
    }

}
