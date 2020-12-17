package domain.Pieces;



import java.util.Arrays;
import java.util.List;

import domain.Logic.Color.*;
import domain.Logic.Pair;

public class Pawn extends Piece {

    public static final PieceType TYPE = PieceType.PAWN; // final for each instance

    public Pawn(ColorType color) {

        super(TYPE, color);

    }

    @Override
    public String toString() {
        if(super.getColor() == ColorType.Black) return "p";

        return "P";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) { // if that move with valid coordinates is allowed by rules

        return validOrNah(start, end, null);

    }

    public boolean validOrNah(Pair startXY, Pair endXY, Piece killed) {
        if(startXY.equals(endXY)) return false;

        int starty = startXY.getY();
        int startx = startXY.getX();

        int endx = endXY.getX();
        int endy = endXY.getY();

        if (this.getColor() == ColorType.White)
            return doTestsWhite(startx, starty, endx, endy, killed);
        else
            return doTestsBlack(startx, starty, endx, endy, killed);
    }

    private boolean doTestsWhite(int startx, int starty, int endx, int endy, Piece killed) {

        if(starty-endy == 2 && Math.abs(startx - endx) == 0){ //first move 
            return starty == 6;
        }

        if (starty - endy > 1 || startx - endx > 1) // too large
            return false;
        else if (starty - endy < 0) // going backwards
            return false;
        else if (startx - endx > 0 && endy == starty) // going to side without taking
            return false;
        else if (Math.abs(startx - endx) == 1 && starty - endy == 1) // valid capture move
            return true;
        else if(killed == null)
            return true;
        
        return false;
    }

    private boolean doTestsBlack(int startx, int starty, int endx, int endy, Piece killed) {
        if(endy - starty == 2 && Math.abs(startx - endx) == 0){ 
            return starty == 1;
        }
        
        if (endy - starty > 1 || endx - startx > 1)
            return false;
        else if (endy - starty < 0 || endx < startx)
            return false;
        else if (endx - startx > 0 && endy == starty)
            return false;
        else if (Math.abs(endx - startx) == 1 && endy - starty == 1)
            return true;
        else if(killed == null)
            return true;
        
        return false;
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) { 
        int startx = start.getX();
        int starty = start.getY();
        int endy = end.getY();
        int endx = end.getX();
        int length = Math.abs(end.getY() - start.getY()); 
        Pair[] temparr = new Pair[length];

        if(startx != endx && Math.abs(endy - starty) == 1){ //capture
            return Arrays.asList(start, end);
        }

        for (int i = length; i > 0; i--) { //moving forward
            int y = Math.min(starty, endy);    
            temparr[i-1] = new Pair(startx, y + i);
        }
        
        return Arrays.asList(temparr);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitPawn(this);
    }

    @Override
    public String getReadablePiece() {
        return "Pawn";
    }
}
