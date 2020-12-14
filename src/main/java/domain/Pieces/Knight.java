package domain.Pieces;

import java.util.ArrayList;
import java.util.List;

import domain.Logic.Pair;
import domain.Logic.Color.ColorType;

public class Knight extends Piece {
    public static final PieceType TYPE = PieceType.KNIGHT;

    public Knight(ColorType color) {
        super(TYPE, color);
    }

    @Override
    public String toString() {
        return "?";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        int startx = start.getX();
        int starty = start.getY();
        int endx = end.getX();
        int endy = end.getY();
        if(Math.abs(endy - starty) == 2 && Math.abs(endx - startx) ==1){
            return true;
        }
        else if(Math.abs(endy - starty) == 1 && Math.abs(endx - startx) ==2){
            return true;
        }
        return false;
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        return new ArrayList<>(); //can jump so return an empty list as we do not need to check for pieces in the way
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitKnight(this);
    }

    @Override
    public String getReadablePiece() {
        return "Knight";
    }
    
}
