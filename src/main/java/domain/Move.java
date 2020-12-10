package domain;


import domain.Pieces.Piece;
import domain.Pieces.PieceType;

public class Move { // sets target square to the moving piece and sets the piece previously at that square dead.. need check for color if 
                    
    private Square start;
    private Square end;
    private Piece piecemoved;
    private Piece piecekilled;

    public Move(Square start, Square end, Piece piecemoved, Piece piecekilled) {
        this.start = start;
        this.end = end;
        this.piecemoved = piecemoved;
        this.piecekilled = piecekilled;
        move();
    }
    public Move(Square start, Square end, Piece piecemoved){
        this(start,end,piecemoved,null);
    }

    public Piece getPieceMoved() {
        return this.piecemoved;
    }   

    public void move() {

        if (piecekilled != null) {
                end.setPiece(piecemoved);
                start.setPiece(null); 
                System.out.println(piecekilled.getColor()+"'s " + piecekilled.getType().name() + " has been captured by "+piecemoved.getColor()+"'s " + piecemoved.getType().name());
                if(piecekilled.getType() != PieceType.KING)
                    piecekilled.setDead(); 
        }
        else{ //there was no captured piece
            end.setPiece(piecemoved);
            start.setPiece(null);
        }
    }
}
