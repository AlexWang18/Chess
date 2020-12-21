package domain.Pieces.Visitor;

import domain.Logic.Pair;
import domain.Logic.Square;
import domain.Pieces.*;

public interface Visitor<T> {
    // visitor interface used to add functionality to Piece subclasses easily, can
    // return any generic type T concrete implementations are all gonna be Bool tho.. 
    //should prob nest to reduce file clutter

    public T visitBishop(Bishop bishop, Pair startXY, Pair endXY);

    public T visitPawn(Pawn pawn, Pair startXY, Pair endXY, Piece killedPiece);

    public T visitKnight(Knight knight, Pair startXY, Pair endXY);

    public T visitKing(King king, Pair startXY, Pair endXY);

    public T visitQueen(Queen queen, Pair startXY, Pair endXY);

    public T visitRook(Rook rook, Pair startXY, Pair endXY);
}

