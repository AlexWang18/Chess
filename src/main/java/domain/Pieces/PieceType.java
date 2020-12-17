package domain.Pieces;

public enum PieceType {

    CAPTURED(0),
    PAWN(1), 
    BISHOP(3), 
    KNIGHT(3), 
    ROOK(5), 
    QUEEN(9), 
    KING(100);
    
    private int value;
    
    private PieceType(int value) {
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
