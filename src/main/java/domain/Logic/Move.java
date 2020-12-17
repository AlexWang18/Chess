package domain.Logic;


import domain.Pieces.Piece;
import domain.Pieces.PieceType;

//

public class Move { 
                    
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

    public Piece getPieceKilled(){
        return this.piecekilled;
    }

    public Pair getStartingPair(){
        return getStartingSquare().getCoord();
    }

    public Pair getEndingPair(){
        return getEndingSquare().getCoord();
    }
    
    public Square getStartingSquare(){
        return this.start;
    }
    
    public Square getEndingSquare(){
        return this.end;
    }

    public void move() {

        if(piecekilled != null) {
                setPieces();
                System.out.println(piecekilled.getColor()+"'s " + piecekilled.getType().name() + " has been captured by "+piecemoved.getColor()+"'s " + piecemoved.getType().name());
           //     if(piecekilled.getType() != PieceType.KING)
           //         piecekilled.setType(PieceType.CAPTURED); //changing this memory boxes type effectively should i do this could have repruccssions l8r
        }
        else{ 
            setPieces();
        }
    }

    private void setPieces(){
        end.setPiece(piecemoved);
        start.setPiece(null); 
    }

    public boolean isCapture(){
        return piecekilled != null;
    }

    @Override 
    public String toString(){
        if(piecekilled == null)
            return this.piecemoved.getColor()+" "+this.piecemoved.getReadablePiece() + ", " + this.getStartingPair() + " -> " + this.getEndingPair();

        return this.piecemoved.getColor()+" "+this.piecemoved.getReadablePiece() + ", " + this.getStartingPair() + " -> " + this.getEndingPair() +", captures " +piecekilled.getColor() + " " + piecekilled.getReadablePiece();
    }
}
