package domain.Pieces;

public class ClassicRules implements Visitor<Void> { //could make everyone queens or move in a weird fashion where shit doesnt count, could just make void, 
    /*
    could have different design strategies for behavior of pieces embedded in UI class so they can choose classic or nah 
    i think i effed up should have the current methods of Piece put in a class visitor than another concrete visiitor for another mode
    i have redundancy and should 
    */

    public void changeBehavior(Piece piece){
        piece.accept(this);
    }

    @Override
    public Void visitBishop(Bishop bishop) { //would have piece behjavior here i.e. returning true or false based on ruleset
        return null;
    }

    @Override
    public Void visitPawn(Pawn pawn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitKnight(Knight knight) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitKing(King king) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitQueen(Queen queen) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitRook(Rook rook) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
