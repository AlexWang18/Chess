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

interface Error{
    abstract boolean displayError();
}

//could make many nested classes inside Errors that implement Error so they have uniform method name. issues with instance abstract method cant be accessed staticly and different param

public class Errors {
    private Errors(){
        throw new IllegalStateException("Silly goose");
    }
    //Unsound move exceptions ->> if called will always return false

    public static boolean displayNoMovement(){
        System.out.println("You should move at least 1 square");
        return false;
    }

    public static boolean pieceBreakRules(Square start, Piece piece){
        System.out.println("Illegal move at " + start.getCoord() + " " + piece.getType()
                        + " cannot make those manuevers"); 
        return false;
    }

    public static boolean pawnBreaksRules(Square start){
        System.out.println("Illegal pawn manuever at " + start.getCoord());
        return false;
    }

    public static boolean noSuchPieceExists(Square start){
        System.out.println("No piece exists at " + start.getCoord());
        return false;
    }

    public static boolean unOwnedPiece(Square start, Piece startPiece){
        System.out.println("You do not own " + startPiece.getReadablePiece() + " at " + start.getCoord() + " silly");
        return false;
    }

    public static boolean friendlyFire(Square end, Piece endPiece){
        System.out.println("You cannot capture your own "+ endPiece.getReadablePiece() + " at " + end.getCoord());
        return false;
    }

    public static boolean isInCheck(Piece pieceMoved, Square start){
        System.out.println(pieceMoved.getColor() + " cannot move " + pieceMoved.getReadablePiece() + " at " + start.getCoord()
                    + " as they are in check");
        return false;
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
        System.out.println("You cannot castle as your King will be threatened in the process at " + xy);
    }

    public static void cannotCastleActivePieces(){
        System.out.println("You have moved your king or rook already... cannot castle");
    }
}
