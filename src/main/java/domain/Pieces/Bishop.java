package domain.Pieces;

import domain.Pair;
import domain.Color.ColorType;

public class Bishop extends Piece {
    public static final PieceType TYPE = PieceType.BISHOP;

    public Bishop(ColorType color) {
        super(TYPE, color);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "B";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        int starty = start.getY(); 
        int startx = start.getX();
        int endx = end.getX();
        int endy = end.getY();
        //Diagnals
        return true;
    }
    
}
