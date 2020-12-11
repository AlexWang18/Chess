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
        if(start == end) return false;

        int startx = start.getX();
        int starty = start.getY();
        int endx =  end.getX();
        int endy = end.getY();

        return overOne(startx, endx) && overOne(starty, endy);
    }
    private boolean overOne(int start, int end){
        return Math.abs((start-end)) == 1 || start - end == 0;
    }
    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        int startx = start.getX();
        int starty = start.getY();
        int endx =  end.getX();
        int endy = end.getY();
        
        Pair[] temp = {new Pair(Math.max(startx, endx), Math.max(starty, endy))};
        
        return Arrays.stream(temp).collect(Collectors.toList());
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitKing(this);
    }
    
}
