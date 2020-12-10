package domain;

import java.util.*; //Gui to display game todo
/*
Need to check for piecs ahead of starting piece, only knights can hop
implement path finding for rest of pieces
*/

import domain.Color.ColorType;
import domain.Pieces.*;
/*

*/

public class Game {
    public boolean over = false;
    private Board board;
    private ColorType currentplayer;
    private List<Move> previousMoves;
    private Map<ColorType, List<Piece>> pieceskilled;
    private List<Piece> whiteCapturedBlack;
    private List<Piece> blackCapturedWhite;

    public Game() { // each game creates a new board, set of moves taken, and sets the player to
                    // white

        board = new Board();
        previousMoves = new ArrayList<>();
        currentplayer = ColorType.White;
        pieceskilled = new EnumMap<>(ColorType.class);
        whiteCapturedBlack = new ArrayList<>();
        blackCapturedWhite = new ArrayList<>();
    }

    public void startGame() {
        printBoard();
    }

    public boolean notDone() {
        return !over;
    }

    public void executeMove(int startx, int starty, int endx, int endy) { // need checkmate detection, refactor ifs into
                                                                          // smaller functions, allowing friendlyfire

        Square[][] temp = board.getBoard();
        Square start = temp[starty][startx];
        Square end = temp[endy][endx];
        if (start == end) {
            System.out.println("You should move at least 1 square");
            return;
        }
        Pair startXY = start.getCoord();
        Pair endXY = end.getCoord();
        Piece startPiece = start.getPiece(); // get the pieces at those squares, throw error if not existing, NEED TO
        Piece killedPiece = end.getPiece();

        if (startPiece == null) {
            System.out.println("No piece exists at " + start.getCoord());
            return;
        }
        boolean check = isOwnedPiece(startPiece);
        if (!check) {
            System.out.println("You do not own" + startPiece + "at" + start.getCoord() + " silly");
            return;
        }
        check = isFriendlyFire(startPiece, killedPiece);
        if (check) {
            System.out.println("Cannot take your own piece silly");
            return;
        }
        check = isPawn(startPiece);
        // special case for pawns
        if (check) {
            Pawn pawn = (Pawn) startPiece;
            // first check if the Piece can make the move, than look for barriers, and then
            // go to add move which checks its further validity
            check = pawn.validOrNah(start.getCoord(), end.getCoord(), killedPiece)
                    && checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY);

            if (check) {
                addMove(start, end, pawn, killedPiece);
            } else {
                System.out.println("Illegal pawn manuever at " + start.getCoord().getReadablePair());
                return;
            }
        }
        boolean check2 = startPiece.validOrNah(start.getCoord(), end.getCoord())
                && startPiece.getColor() == currentplayer
                && checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY);
        if (check2) {
            addMove(start, end, startPiece, killedPiece);
        }

        else {
            System.out.println("Illegal move at " + start.getCoord().getReadablePair()); //need 2 specify
            return;
        }

        switchCurrentPlayer();
        printBoard();
        showScore();
    }

    private void addMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        if (killedPiece != null) { // if the subsequent move has captured a piece
            checkIfKing(killedPiece);
            previousMoves.add(new Move(start, end, startPiece, killedPiece));
            addKilledPiece(killedPiece);
        } else {
            previousMoves.add(new Move(start, end, startPiece)); // no capture was performed
        }
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

    private boolean checkPiecesPath(List<Pair> path, Pair startXY, Pair endXY) { // recieves list of coordinates for a
                                                                                 // moved piece and its ending position.
        // Returns false if there are pieces blocking it from moving

        Square[][] bd = board.getBoard();

        for (Pair pair : path) {

            if ((bd[pair.getY()][pair.getX()].hasPiece() && (!pair.equals(startXY)) && (!pair.equals(endXY)))) {
                System.out.println("Cannot hop over piece at " + pair);
                return false;
            }
        }
        return true;
    }

    private void addKilledPiece(Piece killedPiece) { // helper function to
        if (killedPiece.getColor() == ColorType.White) {
            blackCapturedWhite.add(killedPiece);
            pieceskilled.putIfAbsent(ColorType.White, blackCapturedWhite);
            return;
        }
        whiteCapturedBlack.add(killedPiece);
        pieceskilled.putIfAbsent(ColorType.Black, whiteCapturedBlack);
    }

    private void checkIfKing(Piece killed) {
        if (killed.getType() == PieceType.KING)
            over = true;
    }

    private void showScore() { // not printing
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
