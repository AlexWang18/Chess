package domain.Pieces.Visitor;

import domain.Logic.Pair;
import domain.Pieces.*;


//Every move is valid as long as they are possible 
public class NoRules implements Visitor<Boolean> {

    @Override
    public Boolean invokeBehavior(Piece piece, Pair startXY, Pair endXY, Piece killedPiece) {
        return piece.accept(this, startXY, endXY);
    }
    
    @Override
    public Boolean visitBishop(Bishop bishop, Pair startXY, Pair endXY) {
        return true;
    }

    @Override
    public Boolean visitPawn(Pawn pawn, Pair startXY, Pair endXY, Piece killedPiece) {
        return true;
    }

    @Override
    public Boolean visitKnight(Knight knight, Pair startXY, Pair endXY) {
        return true;
    }

    @Override
    public Boolean visitKing(King king, Pair startXY, Pair endXY) {
        return true;
    }

    @Override
    public Boolean visitQueen(Queen queen, Pair startXY, Pair endXY) {
        return true;
    }

    @Override
    public Boolean visitRook(Rook rook, Pair startXY, Pair endXY) {
        return true;
    }

}
