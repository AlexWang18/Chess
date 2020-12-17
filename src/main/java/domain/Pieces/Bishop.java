package domain.Pieces;



import java.util.Arrays;
import java.util.List;

import domain.Logic.Pair;
import domain.Logic.Color.ColorType;

public class Bishop extends Piece {

    public static final PieceType TYPE = PieceType.BISHOP;

    public Bishop(ColorType color) {
        super(TYPE, color);
    }

    @Override
    public String toString() {
        if(super.getColor() == ColorType.Black) return "b";

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
        int starty = start.getY();
        int startx = start.getX();
        int endx = end.getX();
        int endy = end.getY();
        int length = Math.abs(endy - starty);

        Pair[] temparr = new Pair[length];
        int signX = Integer.signum(endx - startx); // direction of the move, left or right
        int signY = Integer.signum(endy - starty);

        for (int i = 1; i <= length; i++) {
            temparr[i - 1] = new Pair(startx + signX * i, starty + signY * i); //signY * i is direction times growing length
        }
        // System.out.println(Arrays.toString(temparr));
        return Arrays.asList(temparr);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBishop(this);
    }

    @Override
    public String getReadablePiece() {
        return "Bishop";
    }

}
