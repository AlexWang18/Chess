package domain.Pieces;

import domain.Pair;

import java.util.Arrays;
import java.util.List;

import domain.Color.ColorType;

public class Bishop extends Piece {
    public static final PieceType TYPE = PieceType.BISHOP;

    public Bishop(ColorType color) {
        super(TYPE, color);
    }

    @Override
    public String toString() {
        return "B";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        int starty = start.getY();
        int startx = start.getX();
        int endx = end.getX();
        int endy = end.getY();
        return Math.abs(starty - endy) == Math.abs(startx - endx); // for diagnals will always have same x and y values
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        int ylength = Math.abs(end.getY() - start.getY());
    
        Pair[] temparr = new Pair[ylength];
        int signX=Integer.signum(end.getX()-start.getX()); //direction of the move, left or right
        int signY=Integer.signum(end.getY()-start.getY());
        
        for (int i = 1; i <= ylength; i++) {
            temparr[i-1] = new Pair(start.getX() + signX * i, start.getY()+ signY * i);
        }
        //System.out.println(Arrays.toString(temparr));
        return Arrays.asList(temparr);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBishop(this);
    }
    
}
