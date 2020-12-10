package domain;

import java.util.*; //Gui to display game todo
/*
Need to check for piecs ahead of starting piece, only knights can hop
implement path finding for rest of pieces
*/

import domain.Color.ColorType;
import domain.Pieces.*;

public class Game { // manages the game and its state can be active, stalemeate, ended, or
                    // currentplayers victory
    public boolean over = false;
    private Board board;
    private ColorType currentplayer;
    private List<Move> moves;
    private Map<ColorType, List<Piece>> pieceskilled;
    private List<Piece> whitepieces;
    private List<Piece> blackpieces;

    public Game() { // each game creates a new board, set of moves taken, and sets the player to //
                    // move as white
        board = new Board();
        moves = new ArrayList<>();
        currentplayer = ColorType.White;
        pieceskilled = new EnumMap<>(ColorType.class);
        whitepieces = new ArrayList<>();
        blackpieces = new ArrayList<>();
    }

    public void startGame() {
        printBoard();
    }

    public boolean notDone() {
        return !over;
    }

    public void executeMove(int startx, int starty, int endx, int endy) { // need checkmate detection, refactor ifs

        Square[][] temp = board.getBoard();
        Square start = temp[starty][startx];
        Square end = temp[endy][endx];
        Pair startXY = start.getCoord();
        Pair endXY = end.getCoord();
        Piece startPiece = start.getPiece(); // get the pieces at those squares, throw error if not existing, NEED TO
                                             // ADD NULL PROTECTION
        if (startPiece == null) {
            System.out.println("No piece at " + start.getCoord());
            return;
        }
        Piece killedPiece = end.getPiece();
        if (startPiece.getType() == PieceType.PAWN && startPiece.getColor() == currentplayer) { 
            Pawn pawn = (Pawn) startPiece;
            if (pawn.validOrNah(start.getCoord(), end.getCoord(), killedPiece) 
                    && checkPath(startPiece.getPiecePath(startXY, endXY),endXY)) {
                        addMove(start, end, pawn, killedPiece);
            }
            else{
                System.out.println("Illegal pawn manuever at " + start.getCoord().getReadablePair());
                return;
            }
        }

        else if (startPiece.validOrNah(start.getCoord(), end.getCoord()) 
                && startPiece.getColor() == currentplayer 
                    && checkPath(startPiece.getPiecePath(startXY, endXY), endXY))
                        addMove(start, end, startPiece, killedPiece);

        else {
            System.out.println("Illegal move at " + start.getCoord().getReadablePair()); // could specify the error here wrong piece or cant move that way                               
            return;
        }
        switchCurrentPlayer();
        printBoard();
        showScore();
    }

    private boolean checkPath(List<Pair> path,Pair endXY){ //makes sure their isnt pieces in the way, BUGGING with bishops allowing for skips still...
        Square[][] bd = board.getBoard();
          
        for (Pair pair : path) { //had to swap y and x and put failsafe 
            if ((bd[pair.getY()][pair.getX()].hasPiece()
                    && !pair.equals(endXY))) { 
				        return false;
			}
		}
		return true;
    }
    private void addMove(Square start, Square end, Piece startPiece, Piece killedPiece) { //not moving pawns and displaying it
        // if the move is possible and if is their turn

        if (killedPiece != null && startPiece.getColor() != killedPiece.getColor()) {
            checkIfKing(killedPiece);
            moves.add(new Move(start, end, startPiece, killedPiece));
            addKilledPiece(killedPiece);
        } else {
            moves.add(new Move(start, end, startPiece, killedPiece)); // no capture performed
        }
    }

    private void addKilledPiece(Piece killedpiece) {
        if (killedpiece.getColor() == ColorType.White) {
            pieceskilled.putIfAbsent(ColorType.White, whitepieces);
            whitepieces.add(killedpiece);
            return;
        }
        blackpieces.add(killedpiece);
        pieceskilled.putIfAbsent(ColorType.Black, blackpieces);
    }

    private void checkIfKing(Piece killed) {
        if (killed.getType() == PieceType.KING)
            over = true;
    }

    private void showScore() { // not printing
        for (List<Piece> p : pieceskilled.values()) {
            int score = 39 - p.stream().mapToInt(i -> i.getValue()).sum();
            System.out.println(p.get(0).getColor() + "'s score: " + score); 
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
