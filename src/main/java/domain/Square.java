package domain;

import domain.Pieces.Piece;

public class Square { // holds coordinates and current piece at that square if available, otherwise
                      // null
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

    public Piece getPiece() {
        return this.piece;
    }

    public Pair getCoord(){
        return this.coord;
    }

    public boolean hasPiece(){
        return this.piece != null;
    }

    @Override
    public String toString() {
        if (piece == null) {
            return "-";
        }
        return piece.toString();
    }

}
