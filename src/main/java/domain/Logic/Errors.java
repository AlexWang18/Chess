package domain.Logic;



import domain.Pieces.*;

class Message{  //would not work as an inner class as i cant make an instance of the Errors util class
    protected Piece startPiece;
    protected Piece blockPiece;
    protected Pair whereXY;
    protected boolean value;

    public Message(Piece startPiece, Piece blockPiece, Pair whereXY, boolean value){
        this.startPiece = startPiece;
        this.blockPiece = blockPiece;
        this.whereXY = whereXY;
        this.value = value;
    }
    public boolean getBool(){
        return this.value;
    }
}

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
    
    public static void inputError(){
        System.out.println("An error has occured with the input.. try again");
    }

    public static void pathIsBlocked(Message m){
        Piece startPiece = m.startPiece;
        Piece blockingPiece = m.blockPiece;
        Pair atHere = m.whereXY;
        StringBuilder sb = new StringBuilder(startPiece.getReadablePiece() + " cannot hop over the "
        + blockingPiece.getReadablePiece() + " at " + atHere);
        System.out.println(sb.toString());
    }

    public static void piecesBlockingCastle(){
        System.out.println("You need to move your other pieces out of the way before castling!");
    }

    public static void castleIsThreatened(Pair xy){
        System.out.println("You cannot castle as your King will be threatend in the process at " + xy);
    }

    public static void cannotCastleActivePieces(){
        System.out.println("You have moved your king or rook already... cannot castle");
    }
}
