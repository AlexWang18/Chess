package domain.Logic;




import domain.Pieces.*;

/* 
* Container class for every type of move so far... should i include validation in here> and do castling, promotion checking here... 
* would be creating a lot of objs in memory that would be removed
*/
public class Move {

    public enum MoveType{
        STANDARD,
        CASTLE,
        EN_PASSANT,
        PAWN_CAPTURE,
        PROMOTION;
    }

    private Square start;
    private Square end;

    private Piece pieceMoved;
    private Piece pieceKilled;

    private MoveType type;


    public Move(Square start, Square end) {
        this.start = start;
        this.end = end;
    }

    public Move(Square start, Square end, Piece pieceMoved, Piece pieceKilled){
        this.start = start;
        this.end = end;
        this.pieceMoved = pieceMoved;
        this.pieceKilled = pieceKilled;
        move(0);
    }

    public Move(Square start, Square end, Pawn pPawn, Piece pieceKilled){
        this.start = start;
        this.end = end;
        this.pieceMoved = pPawn;
        this.pieceKilled = pieceKilled;
    }

    public void move(int code) {
        if (code == 0) {
            setStandardMove();
        } else if (code == 1) {
            setPawnCapture();
        } else if (code == 2) {
            setCastling();
        } else if (code == 3){
            setEnPassant();
        } else if (code == 4) {
            setPromotion();
        }
    
    }

    private void setStandardMove() {
        end.setPiece(this.pieceMoved);
        start.setPiece(null);
    }

    private void setPawnCapture(){

    }

    private void setCastling(){

    }

    private void setEnPassant(){

    }

    private void setPromotion(){

    }

    public boolean isCapture() {
        return pieceKilled != null;
    }

    public Piece getPieceMoved() {
        return this.pieceMoved;
    }

    public Piece getPieceKilled() {
        return this.pieceKilled;
    }

    public Pair getStartingPair() {
        return getStartingSquare().getCoord();
    }

    public Pair getEndingPair() {
        return getEndingSquare().getCoord();
    }

    public Square getStartingSquare() {
        return this.start;
    }

    public Square getEndingSquare() {
        return this.end;
    }

    @Override
    public String toString() {
        if (pieceKilled == null)
            return this.pieceMoved.getColor() + " " + this.pieceMoved.getReadablePiece() + ", " + this.getStartingPair()
                    + " -> " + this.getEndingPair();

        return this.pieceMoved.getColor() + " " + this.pieceMoved.getReadablePiece() + ", " + this.getStartingPair()
                + " -> " + this.getEndingPair() + ", captures " + pieceKilled.getColor() + " "
                + pieceKilled.getReadablePiece();
    }
}
