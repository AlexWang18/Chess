package domain.Logic;



import domain.Pieces.*;

public class Errors {

    private Errors(){
        throw new IllegalStateException("Silly goose");
    }

    public static void displayNoMovement(){
        System.out.println("You should move at least 1 square");
    }

    public static void pieceBreakRules(Square start, Piece piece){
        System.out.println("Illegal move at " + start.getCoord() + " " + piece.getType()
                        + " cannot make those manuevers"); 
    }
    public static void noSuchPieceExists(Square start){
        System.out.println("No piece exists at " + start.getCoord());
    }
    public static void moveException(){
        System.out.println("Move did not follow through, try again..");
    }   
    public static void pathIsBlocked(Piece startPiece, Piece blockingPiece, Pair atHere){
        StringBuilder sb = new StringBuilder(startPiece.getReadablePiece() + " cannot hop over the "
        + blockingPiece.getReadablePiece() + " at " + atHere);
        System.out.println(sb.toString());
    }
    public static void piecesBlockingCastle(){
        System.out.println("You need to move your other pieces out of the way before castling!");
    }
    public static void castleIsThreatened(){
        System.out.println("You cannot castle as your King will be threatend in the process at ");
    }

}
