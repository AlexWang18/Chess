package domain.Pieces;

import domain.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if(start.equals(end)) return false;

        int startx = start.getX();
        int starty = start.getY();
        int endx =  end.getX();
        int endy = end.getY();

        return Math.abs(endx - startx) <= 1 && Math.abs(endy - starty) <= 1;
    }
    
    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        
        Pair[] temp = {start, end}; 
        
        return Arrays.stream(temp).collect(Collectors.toList());
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitKing(this);
    }
    
}
