package domain.Pieces;



import domain.Logic.Color.ColorType;
import domain.Logic.Pair;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public static final PieceType TYPE = PieceType.QUEEN;

    public Queen(ColorType color) {
        super(TYPE, color);
    }

    @Override
    public String toString() {
        if(super.getColor() == ColorType.Black) return "q";

        return "Q";
    }

    @Override
    public boolean validOrNah(Pair start, Pair end) {
        if (start.equals(end))
            return false;

        int starty = start.getY();
        int startx = start.getX();
        int endx = end.getX();
        int endy = end.getY();

        if (Math.abs(endy - starty) == Math.abs(endx - startx)) { // for diagnals
            return true;
        }
        return startx - endx == 0 || starty - endy == 0;
        // for straights need 1 plane to stay the same.
    }

    @Override
    public List<Pair> getPiecePath(Pair start, Pair end) {
        int starty = start.getY();
        int startx = start.getX();
        int endx = end.getX();
        int endy = end.getY();
        int length = 0;
        List<Pair> path = new ArrayList<>();

        if (Math.abs(endy - starty) == Math.abs(endx - startx)) { // diagnals, use sig num 
            length = Math.abs(endy - starty);
            int signX = Integer.signum(endx - startx); // direction of the move, left or right
            int signY = Integer.signum(endy - starty);

            for (int i = 0; i < length; i++) {
                path.add(i, new Pair(startx + (i*signX), starty + (i * signY)));
            }

        } else {
            length = Math.abs(endx - startx) + Math.abs(endy - starty);
            for (int i = 0; i < length; i++) {
                if (startx == endx)
                    path.add(i, new Pair(startx, Math.min(starty, endy) + i)); /* vertical str8 */
                else
                    path.add(i, new Pair(Math.min(startx, endx) + i, starty)); /* horizontal str8 */
            }
        }

        return path;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitQueen(this);
    }

    @Override
    public String getReadablePiece() {
        return "Queen";
    }

}
