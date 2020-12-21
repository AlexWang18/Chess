package domain.Pieces.Visitor;

import domain.Logic.Pair;
import domain.Pieces.Bishop;
import domain.Pieces.King;
import domain.Pieces.Knight;
import domain.Pieces.Pawn;
import domain.Pieces.Piece;
import domain.Pieces.Queen;
import domain.Pieces.Rook;

public class SillyRules implements Visitor<Boolean> {

    @Override
    public Boolean visitBishop(Bishop bishop, Pair startXY, Pair endXY) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean visitPawn(Pawn pawn, Pair startXY, Pair endXY, Piece killedPiece) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean visitKnight(Knight knight, Pair startXY, Pair endXY) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean visitKing(King king, Pair startXY, Pair endXY) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean visitQueen(Queen queen, Pair startXY, Pair endXY) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean visitRook(Rook rook, Pair startXY, Pair endXY) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean invokeBehavior(Piece piece, Pair startXY, Pair endXY, Piece killedPiece) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
