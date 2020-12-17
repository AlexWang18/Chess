package domain.Logic;



import java.util.Objects;

import domain.Pieces.Piece;
import domain.Pieces.PieceType;

public class Square { // holds coordinates and current piece at that square if available, otherwise Piece will be null
               
    private Pair coord;
    
    private Piece piece;

    public Square(Pair coord) {
        this(coord, null);
    }

    public Square(Pair coord, Piece piece) {
        this.coord = coord;
        this.piece = piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void killPiece(){
        this.piece = null;
       // this.piece.setType(PieceType.CAPTURED);
    }

    public Piece getPiece() {
        return this.piece;
    }

    public Pair getCoord(){
        return this.coord;
    }

    public boolean hasPiece(){
        return piece != null;
    }

    @Override
    public String toString() {
        if(piece == null) {
            return "-";
        }
        return piece.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Square)) {
            return false;
        }
        Square othersquare = (Square) o;
        return this.getCoord() == othersquare.getCoord() && this.getPiece() == othersquare.getPiece();
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord,piece); //utility method creates hash from fields
    }
    
}
