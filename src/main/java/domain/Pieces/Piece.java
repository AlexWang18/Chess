package domain.Pieces;

import domain.Pair;

import domain.Color.ColorType;

public abstract class Piece { // better abstract than interface less overrlap
    private ColorType color;
    private PieceType type;

    public Piece(PieceType type, ColorType color) {
        this.type = type; //never being initalized, type is staying at null
        this.color = color;
    }

    public abstract String toString();

    public abstract boolean validOrNah(Pair start, Pair end); // checks if the movement coordinates are feasbile for that piece                                                     
    //public abstract boolean validOrNah(Pair start, Pair end, Piece killed); //pawns
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

    public void setDead() {
        this.type = PieceType.CAPTURED; // refac
    }
    public int getValue(){
        return this.type.getValue();
    }
    // setdead
    //
}
