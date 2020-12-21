package domain.Pieces.Visitor;

import domain.Logic.Pair;
import domain.Pieces.Bishop;
import domain.Pieces.King;
import domain.Pieces.Knight;
import domain.Pieces.Pawn;
import domain.Pieces.Piece;
import domain.Pieces.Queen;
import domain.Pieces.Rook;

public class ClassicRules implements Visitor<Boolean> { // could make everyone queens or move in a weird fashion where
    // shit doesnt count, could just make void,
    /*
     * could have different design strategies for behavior of pieces embedded in UI
     * class so they can choose classic or nah
     * 
     */
    @Override
    public Boolean invokeBehavior(Piece piece, Pair startXY, Pair endXY, Piece killedPiece) {
        if (piece instanceof Pawn) { 
            Pawn pawn = (Pawn) piece; //trust me im a pro
            return pawn.accept(this, startXY, endXY, killedPiece);
        }
        return piece.accept(this, startXY, endXY);
    }

    @Override
    public Boolean visitBishop(Bishop bishop, Pair startXY, Pair endXY) {
        return bishop.validOrNah(startXY, endXY);
    }

    @Override
    public Boolean visitPawn(Pawn pawn, Pair startXY, Pair endXY, Piece killedPiece) {
        return pawn.validOrNah(startXY, endXY, killedPiece);
    }

    @Override
    public Boolean visitKnight(Knight knight, Pair startXY, Pair endXY) {
        return knight.validOrNah(startXY, endXY);
    }

    @Override
    public Boolean visitKing(King king, Pair startXY, Pair endXY) {
        return king.validOrNah(startXY, endXY);
    }

    @Override
    public Boolean visitQueen(Queen queen, Pair startXY, Pair endXY) {
        return queen.validOrNah(startXY, endXY);
    }

    @Override
    public Boolean visitRook(Rook rook, Pair startXY, Pair endXY) {
        return rook.validOrNah(startXY, endXY);
    }

}
