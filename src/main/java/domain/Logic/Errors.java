package domain.Logic;

import domain.Pieces.*;
public class Errors {

    private Errors(){
        throw new IllegalStateException("Silly goose")
    }

    public static void displayNoMovement(){
        System.out.println("You should move at least 1 square");
    }
    
    public static void pieceBreakRules(Square start, Piece piece){
        System.out.println("Illegal move at " + start.getCoord() + " " + piece.getType()
                        + " cannot make those manuevers"); 
    }

}
