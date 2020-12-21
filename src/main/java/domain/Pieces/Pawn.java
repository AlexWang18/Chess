package domain.Pieces;

import java.util.Arrays;
import java.util.List;

import domain.Logic.Color.*;
import domain.Pieces.Visitor.Visitor;
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
    public boolean validOrNah(Pair startXY, Pair endXY) { // if that move with valid coordinates is allowed by rules
        return validOrNah(startXY, endXY, null);

    }

    public boolean validOrNah(Pair startXY, Pair endXY, Piece killed) { 
        if(startXY.equals(endXY)) return false;

        int startx = startXY.getX();
        int starty = startXY.getY();

        int endx = endXY.getX();
        int endy = endXY.getY();
        boolean watch = false;
        

        if (this.getColor() == ColorType.White){
            watch = doTestsWhite(startx, starty, endx, endy, killed); 
            return watch;
        }    
        else{ 
            watch = doTestsBlack(startx, starty, endx, endy, killed); 
            return watch;
        }
    }

    private boolean doTestsWhite(int startx, int starty, int endx, int endy, Piece killed) {

        if(starty-endy == 2 && Math.abs(startx - endx) == 0){ //first move 
            return starty == 6;
        }

        if (starty - endy > 1 || Math.abs(startx - endx) > 1) // too large
            return false;
        else if (starty - endy < 0) // going backwards
            return false;
        else if (Math.abs(endx - startx) > 0 && endy == starty) // going to side without taking
            return false;
        else if (Math.abs(startx - endx) == 1 && starty - endy == 1 && killed != null) // valid capture move
            return true;
        else if(killed == null && startx == endx)
            return true;
        
        return false;
    }

    private boolean doTestsBlack(int startx, int starty, int endx, int endy, Piece killed) {
        if(endy - starty == 2 && Math.abs(startx - endx) == 0){ 
            return starty == 1;
        }
        
        if (endy - starty > 1 || Math.abs(endx - startx) > 1)
            return false;
        else if (endy - starty < 0)
            return false;
        else if (Math.abs(endx - startx) > 0 && endy == starty)
            return false;
        else if (Math.abs(endx - startx) == 1 && endy - starty == 1 && killed != null)
            return true;
        else if(killed == null && startx == endx)
            return true;
        
        return false;
    }

    @Override
    public List<Pair> getPiecePath(Pair startXY, Pair endXY) { 
        int startx = startXY.getX();
        int starty = startXY.getY();
        int endy = endXY.getY();
        int endx = endXY.getX();
        int length = Math.abs(endXY.getY() - startXY.getY()); 
        Pair[] temparr = new Pair[length];

        if(startx != endx && Math.abs(endy - starty) == 1){ //capture moves
            return Arrays.asList(new Pair[] {startXY, endXY});
        }

        for (int i = 0; i < length; i++) { //moving forward
            int y = Math.min(starty, endy);    
            temparr[i] = new Pair(startx, y + i);
        }
        
        return Arrays.asList(temparr);
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Pair startXY, Pair endXY) { //Since pawns validity depends on the captured square 
        //we need to have an overloaded accept method with proper arguments
        return accept(visitor, startXY, endXY, null);
    }
  
    public <T> T accept(Visitor<T> visitor, Pair startXY, Pair endXY, Piece pieceKilled) {
        return visitor.visitPawn(this, startXY, endXY, pieceKilled);
    }

    @Override
    public String getReadablePiece() {
        return "Pawn";
    }

}
