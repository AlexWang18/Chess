package domain.Pieces;

import java.util.List;

import domain.Logic.Pair;
import domain.Logic.Color.*;

public abstract class Piece { // better abstract than interface less overrlap
    private ColorType color;
    private PieceType type;

    public Piece(PieceType type, ColorType color) {
        this.type = type; //never being initalized, type is staying at null
        this.color = color;
    }
    public abstract <T> T accept(Visitor<T> visitor); //generic type

    public abstract String toString();
    
    public abstract String getReadablePiece();

    public abstract List<Pair> getPiecePath(Pair start, Pair end);

    public abstract boolean validOrNah(Pair start, Pair end); // checks if the movement coordinates are feasbile for that piece                                                     

    public ColorType getColor() {
        return this.color;
    }

    public void setColor(ColorType color) {
        this.color = color;
    }

    public PieceType getType() {
        return this.type;
    }
    
    public void setType(PieceType type) {
        this.type = type;
    }

    public int getValue(){
        return this.type.getValue();
    }
    // setdead
    //
}
