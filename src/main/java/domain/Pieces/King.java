package domain.Pieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import domain.Logic.Pair;
import domain.Logic.Color.ColorType;
import domain.Pieces.Visitor.Visitor;


/* should i put possible Positions in here, return a list of possible pos based on the pair passed? */
public class King extends Piece {

    public static final PieceType TYPE = PieceType.KING;

    public King(ColorType color) {
        super(TYPE, color);
    }

    @Override
    public String toString() {
        if(this.getColor() == ColorType.Black) return "♔";

        return "♚";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        if(start.equals(end)) return false;

        int startx = start.getX();
        int starty = start.getY();
        int endx =  end.getX();
        int endy = end.getY();

        return Math.abs(endx - startx) <= 1 && Math.abs(endy - starty) <= 1;
    }
    
    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        
        Pair[] temp = {start, end}; 
        
        return Arrays.stream(temp).collect(Collectors.toList());
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Pair startXY, Pair endXY) {
        return visitor.visitKing(this, startXY, endXY);
    }

    @Override
    public String getReadablePiece() {
        return "King";
    }
}
