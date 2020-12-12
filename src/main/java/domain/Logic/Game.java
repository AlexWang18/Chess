package domain.Logic;

//Gui to display game todo
/*
implemenet checkmate detection to automatically end game
*/
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import domain.Logic.Color.ColorType;
/*

*/
import domain.Pieces.Pawn;
import domain.Pieces.Piece;
import domain.Pieces.PieceType;

public class Game {
    public boolean over = false;

    private Board board;

    private ColorType currentplayer;

    private List<Move> previousMoves;

    private Map<ColorType, List<Piece>> pieceskilled;

    private List<Piece> whiteCapturedBlack;

    private List<Piece> blackCapturedWhite;

    private static Game g = new Game();

    private Game() { // each game creates a new board, set of moves taken, and sets the player to
                     // white

        board = new Board();
        previousMoves = new ArrayList<>();
        currentplayer = ColorType.White;
        pieceskilled = new EnumMap<>(ColorType.class);
        whiteCapturedBlack = new ArrayList<>();
        blackCapturedWhite = new ArrayList<>();
    }

    public static Game getGame() { // Singleton, Only one game will occur at a time
        return g;
    }

    public void startGame() {
        printBoard();
    }

    public boolean notDone() {
        return !over;
    }
//control flow error with check adding move to list before returning false
    public boolean validMove(int startx, int starty, int endx, int endy) { // need checkmate detection, refactor ifs,
                                                                             // main logic funct for moving
        if (!inBounds(startx, starty) || !inBounds(endx, endy))
            return false;

        Square[][] temp = board.getBoard();
        Square start = temp[starty][startx];
        Square end = temp[endy][endx];
        if (start == end) {
            System.out.println("You should move at least 1 square");
            return false;
        }

        Pair startXY = start.getCoord();
        Pair endXY = end.getCoord();
        Piece startPiece = start.getPiece(); // get the pieces at those squares
        Piece killedPiece = end.getPiece();

        if (startPiece == null) {
            System.out.println("No piece exists at " + start.getCoord());
            return false;
        }
        boolean test = isOwnedPiece(startPiece);
        if (!test) {
            System.out.println("You do not own " + startPiece + " at " + start.getCoord() + " silly");
            return false;
        }

        test = isCheck() && startPiece.getType() != PieceType.KING;
        if(test){
            System.out.println("Cannot move another piece while in check!! Must move your king!");
            return false;
        }

        test = isFriendlyFire(startPiece, killedPiece);
        if (test) {
            System.out.println("Cannot take your own piece silly");
            return false;
        }
        test = isPawn(startPiece);
        boolean test2 = startPiece.validOrNah(start.getCoord(), end.getCoord());
        boolean test3 = checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY);

        // special case for pawns
        if (test) {
            Pawn pawn = (Pawn) startPiece;
            // first check if the Piece can make the move, than look for barriers, and then
            // go to add move which checks its further validity

            test = pawn.validOrNah(start.getCoord(), end.getCoord(), killedPiece)
                    && checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY);

            if (test) {
                ;//pass
            } else {
                System.out.println("Illegal pawn manuever at " + start.getCoord().getReadablePair());
                return false;
            }
        }
        else if (test2 && test3) {
            ;
        } else if (!test3) {
                System.out.println("Reg piece Cannot hop over pieces");
                return false;
        } else {
                System.out.println("Illegal move at " + start.getCoord().getReadablePair() + " " + startPiece.getType()
                        + " cannot make those manuevers"); // need 2 specify
                return false;
        }

        addMove(start, end, startPiece, killedPiece);
        switchCurrentPlayer();
        printBoard();
        showScore();
        return true;
    }

    private void addMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        if (killedPiece != null) { // if the subsequent move has captured a piece

            checkIfKing(killedPiece); // refactor into better method
            addKilledPiece(killedPiece);
        }
        previousMoves.add(new Move(start, end, startPiece, killedPiece)); //killedPiece could be null, 
        /*
        adding two moves for some reason
        */
    }

    private boolean isCheckMate(){
        return false;
    }

    private boolean isPawn(Piece startPiece) {
        return startPiece.getType() == PieceType.PAWN;
    }

    private boolean isOwnedPiece(Piece startPiece) {
        return startPiece.getColor() == this.currentplayer;
    }

    private boolean isFriendlyFire(Piece startPiece, Piece killedPiece) {
        if (killedPiece == null)
            return false;
        return startPiece.getColor() == killedPiece.getColor();
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    private boolean checkPiecesPath(List<Pair> path, Pair startXY, Pair endXY) { // recieves list of coordinates for a
                                                                                 // moved piece and its ending position.

        Square[][] bd = board.getBoard();

        for (Pair pair : path) {

            if ((bd[pair.getY()][pair.getX()].hasPiece() && (!pair.equals(startXY)) && (!pair.equals(endXY)))) {
                System.out.println("Cannot hop over piece at " + pair);
                return false;
            } // Returns false if there are pieces blocking it from moving
        }
        return true;
    }

    private boolean isCheck() { // iterate through pieces to find king and its location then iterate through 
                                // oppositite colors pieces to see if they have a valid path from present location to King,

        Pair kingXY = null;
        if (currentplayer == ColorType.White) {
            kingXY = getKingPos(board.getBoard(), 7, 0); // start from bottom row to find whites king, might just do 0,0
        } else {
            kingXY = getKingPos(board.getBoard(), 0, 0);
        }
        // call loop depending on color and check if that piece has
        return loopThruPieces(board.getBoard(), kingXY);
    }

    private boolean loopThruPieces(Square[][] bd, Pair kingXY) { // might have to loop through squares cuz Pieces
        for (int i = 0; i < bd.length; i++) {
            for (int j = 0; j < bd.length; j++) {
                Square sq = bd[i][j];
                if (sq.hasPiece()) {
                    Piece pieceHere = sq.getPiece();
                    if (!isOwnedPiece(pieceHere) && pieceHere.validOrNah(sq.getCoord(), kingXY)) { // attackers
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean loopThruPieces(Square[][] bd, Pair kingXY, int i, int j) { // might have to loop through squares cuz Pieces
        if(bd[i][j].hasPiece() && !isOwnedPiece(bd[i][j].getPiece()) 
                && bd[i][j].getPiece().validOrNah(bd[i][j].getCoord(), kingXY)) 
                    return true;
        
        if(j < bd[i].length -1){
            return loopThruPieces(bd, kingXY, i, j+1);
        }
        else if(i < bd.length - 1){
            return loopThruPieces(bd, kingXY, i+1, 0);
        }
        else{
            return loopThruPieces(bd, kingXY, i-2, j-2);
        }
        
    }

    private Pair getKingPos(Square[][] bd, int rank, int file) { // recursive function to find where king is, better performance than nested fors?

        Piece temp = bd[rank][file].getPiece();
        if (temp != null && temp.getColor() == currentplayer && temp.getType() == PieceType.KING) {
            System.out.println("found it "+ bd[rank][file].getCoord());
            return bd[rank][file].getCoord();
        } else {
            if (file < bd[rank].length - 1) { // still has some bugs stackoverflowing in some instances
                return getKingPos(bd, rank, file + 1);
            } else if (rank < bd.length - 1) {
                return getKingPos(bd, rank + 1, 0);
            } else {
                return getKingPos(bd, rank-2, file-2);
            }
        }

    }

    private void addKilledPiece(Piece killedPiece) { //
        if (killedPiece.getColor() == ColorType.White) {
            blackCapturedWhite.add(killedPiece);
            pieceskilled.putIfAbsent(ColorType.White, blackCapturedWhite);
            return;
        }
        whiteCapturedBlack.add(killedPiece);
        pieceskilled.putIfAbsent(ColorType.Black, whiteCapturedBlack);
    }

    public List<Move> getMoves() { //previousMoves is storing twice
        previousMoves.stream().distinct().forEach(move -> {
            System.out.println(move);
        }); 
        return this.previousMoves;
    }

    private void checkIfKing(Piece killed) {
        if (killed.getType() == PieceType.KING)
            over = true;
    }

    private void showScore() { /*
                                * to do ---- has logic errors
                                */
        for (List<Piece> p : pieceskilled.values()) {
            int score = 39 - p.stream().mapToInt(Piece::getValue).sum();
            System.out.println("'s score: " + score);
        }
    }

    public ColorType getTurn() {
        return this.currentplayer;
    }

    private void printBoard() {
        System.out.println("Current Board:");
        board.showBoard();
    }

    private void switchCurrentPlayer() {
        if (currentplayer.equals(ColorType.White)) {
            currentplayer = ColorType.Black;
        } else {
            currentplayer = ColorType.White;
        }
    }
}
