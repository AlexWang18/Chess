package domain.Logic;


import domain.Pieces.Piece;

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

    //refactor 
    public void move() {
        setPieces();
    }

    private void setPieces(){
        end.setPiece(piecemoved);
        start.setPiece(null); 
    }

    public boolean isCapture(){
        return piecekilled != null;
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

    @Override 
    public String toString(){
        if(piecekilled == null)
            return this.piecemoved.getColor()+" "+this.piecemoved.getReadablePiece() + ", " + this.getStartingPair() + " -> " + this.getEndingPair();

        return this.piecemoved.getColor()+" "+this.piecemoved.getReadablePiece() + ", " + this.getStartingPair() + " -> " + this.getEndingPair() +", captures " +piecekilled.getColor() + " " + piecekilled.getReadablePiece();
    }
}
