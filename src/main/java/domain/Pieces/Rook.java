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

        if(start.equals(end)) return false; //did not move squares

        int startx = start.getX();
        int starty = start.getY();
        int endx =  end.getX();
        int endy = end.getY();

        return startx == endx || starty == endy; //one has moved and one has stayed
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        int length = Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
        Pair[] temp = new Pair[length];
        for (int i = 0; i < length + 1; i++) {
            if(start.getX() == end.getX()){
                temp[i] = new Pair(start.getX(), Math.min(start.getY(),end.getY()) + i);
            }
            else{
                temp[i] = new Pair(Math.min(start.getX(),end.getX()) + i, start.getY());
            }
        }
        System.out.println(Arrays.toString(temp));
        return Arrays.stream(temp).collect(Collectors.toList());
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visit.visitRook(this);
    }
    
}
