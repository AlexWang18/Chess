package domain.Pieces;

import domain.Pair;

import java.util.ArrayList;
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

        if(start.equals(end)) return false; //did not move squares

        int startx = start.getX();
        int starty = start.getY();
        int endx =  end.getX();
        int endy = end.getY();

        if(startx == endx || starty == endy) return true; //one has moved and one has stayed

        return false;
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        int length = Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY()) + 1;
        return new ArrayList<>();
    }
    
}
