package domain.Pieces;

import domain.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        if (start.equals(end))
            return false; // did not move squares

        int startx = start.getX();
        int starty = start.getY();
        int endx = end.getX();
        int endy = end.getY();

        return startx == endx || starty == endy; // one has moved and one has stayed
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        int startx = start.getX();
        int starty = start.getY();
        int endx =  end.getX();
        int endy = end.getY();

        int length = Math.abs(startx - endx) + Math.abs(starty - endy);

        Pair[] temp = new Pair[length];
        for (int i = 0; i < length ; i++) {
            if(startx == endx){
                temp[i] = new Pair(startx, Math.min(starty,endy) + i);
            }
            else{
                temp[i] = new Pair(Math.min(startx,endx) + i, starty);
            }
        }
    
        return Arrays.stream(temp).collect(Collectors.toList());
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitRook(this);
    }

}
