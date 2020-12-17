package domain.Pieces;

public interface Visitor<T> { //visitor interface used to add functionality to Piece subclasses easily, can return any generic type T
    
    public T visitBishop(Bishop bishop); 
    public T visitPawn(Pawn pawn);
    public T visitKnight(Knight knight);
    public T visitKing(King king);
    public T visitQueen(Queen queen);
    public T visitRook(Rook rook);
}
