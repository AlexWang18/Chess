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
    need to add castling- check multiple squares for check, and if the king or rook has moved yet. if king also doesnt go thru a threatened sqiare
    add promotion for pawns

    bug with pawn capturing when in check as it is marked as a different type - solved
    bug with pawn - having a path to king causing any time black move get check, piece valid abstract method for pawn calling rook

*/
import domain.Pieces.Pawn;
import domain.Pieces.Piece;
import domain.Pieces.PieceType;
import domain.Pieces.Queen;

public class Game {
    public boolean over = false;

    private Board board;

    private ColorType currentplayer;

    private List<Move> previousMoves;

    private Map<ColorType, List<Piece>> pieceskilled;

    private List<Piece> whiteCapturedBlack;

    private List<Piece> blackCapturedWhite;

    private int current;

    private static Game g = new Game();

    private Game() { // each game creates a single board, set of moves taken, and sets the player to whiteb    

        board = new Board();
        previousMoves = new ArrayList<>();
        currentplayer = ColorType.White;
        pieceskilled = new EnumMap<>(ColorType.class);
        whiteCapturedBlack = new ArrayList<>();
        blackCapturedWhite = new ArrayList<>();
        current = 0;
    }

    public static Game getGame() { // Singleton, Only one game will occur at a time
        return g;
    }

    public void startGame() {
        printBoard();
    }

    public boolean isCheckMate() {
        return over;
    }

    public boolean tryMove(int startx, int starty, int endx, int endy) { // need checkmate detection, refactor ifs into
                                                                        
        if (!inBounds(startx, starty) || !inBounds(endx, endy))
            return false;

        Square[][] temp = board.getBoard();
        Square start = temp[starty][startx];
        Square end = temp[endy][endx];

        Piece startPiece = start.getPiece(); // get the pieces at those squares
        Piece killedPiece = end.getPiece();
        if(startPiece == null) return false;
        //if (!checkSoundMove(start, end, startPiece, killedPiece)) return false;

        boolean testPawn = isPawn(startPiece);
        // special case
        if (testPawn) {
            return checkPawnMove(start, end, startPiece, killedPiece);
        }

        if (checkValidCastling(start, end)) { // check pieces path with rook vertically
            castlePieces(start, end);
            return true;
        }

        else if (checkSoundMove(start, end, startPiece, killedPiece)) {
            return checkStandardMove(start, end, startPiece, killedPiece);
        }

        // promotion
        return false;
    }

    private boolean checkSoundMove(Square start, Square end, Piece startPiece, Piece killedPiece) {

        if (start.equals(end)) {
            Errors.displayNoMovement();
            return false;
        }

        if (startPiece == null) {
            System.out.println("No piece exists at " + start.getCoord());
            return false;
        }

        boolean test = isOwnedPiece(startPiece);

        if (!test) {
            System.out
                    .println("You do not own " + startPiece.getReadablePiece() + " at " + start.getCoord() + " silly");
            return false;
        }

        test = moveMakesCheck(start, end) && startPiece.getType() != PieceType.KING; // does the move cause the current player to get in check
         //buggy, checking                                                                            
        if (test) {
            System.out.println("Cannot move " + startPiece.getReadablePiece() + " at " + start.getCoord()
                    + " as you are in check");
            return false;
        } else if (moveMakesCheck(start, end)) { // not sure if best practice to have my checkout detection with this,
                                                 // could be just a bad king move
            over = true;
        }

        test = isFriendlyFire(startPiece, killedPiece);

        if (test) {
            System.out.println("Cannot take your own piece silly");
            return false;
        }

        return true;
    }

    private boolean moveMakesCheck(Square start, Square end) { // not sure about logic here, set the ending square piece to point to moved piece, remove the starting squares piece
        Piece temp = end.getPiece();
        end.setPiece(start.getPiece());
        start.killPiece();
        boolean causedCheck = false; 

        if (isCheck(end.getPiece().getColor())) { //if the players move would have caused a check -bugging out b
            causedCheck = true;
        }
        start.setPiece(end.getPiece()); // reset it back into place
        end.setPiece(temp);

        return causedCheck;
    }

    private boolean isCheck(ColorType testColor) { // iterate through pieces to find king and its location then iterate
                                                   // through opposite colors pieces to see if threatens king
        Pair kingXY = null;
        
        kingXY = getKingPos(board.getBoard()); // Find current players Kings position on the board
        
        // delegate looping to another func
        return loopThruPieces(board.getBoard(), kingXY);
    }

    private boolean isValidPromotion(Piece pawn, Square start, Square end) {
        Pair startXY = start.getCoord();
        Pair endXY = end.getCoord();
        if (end.hasPiece() && startXY.getX() == endXY.getX())
            return false; // cannot move straight

        if (pawn.validOrNah(startXY, endXY)) {
            return atEndOfBoard(pawn, endXY);
        }
        return false;
    }

    private boolean atEndOfBoard(Piece pawn, Pair endXY) {
        if (pawn.getColor() == ColorType.White && endXY.getY() == 0) {
            return true;
        } else if (pawn.getColor() == ColorType.Black && endXY.getY() == 7) {
            return true;
        }
        return false;
    }

    private boolean checkValidCastling(Square kingSQ, Square rookSQ) {
        if (!kingSQ.hasPiece() || !rookSQ.hasPiece()) {
            return false;
        }
        if (hasPieceMoved(kingSQ) || hasPieceMoved(rookSQ)) { // must have stayed in starting pos
            return false;
        }

        Piece king = kingSQ.getPiece();
        Piece rook = rookSQ.getPiece();

        if (!rook.validOrNah(rookSQ.getCoord(), kingSQ.getCoord())) { // did the rook do horizontal cross
            return false;
        }

        if (!checkPiecesPath(rook.getPiecePath(rookSQ.getCoord(), kingSQ.getCoord()), rookSQ.getCoord(),
                kingSQ.getCoord())) { // stuff in the way still
            System.out.println("You need to move your other pieces out of the way before castling!");
            return false;
        }

        // for the length of the kings move check each square if threatened

        return king.getType() == PieceType.KING && rook.getType() == PieceType.ROOK; // can castle, currently returning
                                                                                     // true
        // return castlingCond(kingSQ.getCoord(), rookSQ.getCoord());

    }

    private void castlePieces(Square kingSQ, Square rookSQ) {
        Piece king = kingSQ.getPiece();
        Piece rook = rookSQ.getPiece();
        if (rookSQ.getCoord().getX() == 0 && rookSQ.getCoord().getY() == 0) { // queen side black
            addMove(kingSQ, board.getBoard()[0][2], king, null);
            addMove(rookSQ, board.getBoard()[0][3], rook, null);
        }
        if (rookSQ.getCoord().getX() == 7 && rookSQ.getCoord().getY() == 0) { // short castle black
            addMove(kingSQ, board.getBoard()[0][6], king, null);
            addMove(rookSQ, board.getBoard()[0][5], rook, null);
        }
        if (rookSQ.getCoord().getX() == 0 && rookSQ.getCoord().getY() == 7) { //
            addMove(kingSQ, board.getBoard()[7][2], king, null);
            addMove(rookSQ, board.getBoard()[7][3], rook, null);
        }
        if (rookSQ.getCoord().getX() == 7 && rookSQ.getCoord().getY() == 7) {
            addMove(kingSQ, board.getBoard()[7][6], king, null);
            addMove(rookSQ, board.getBoard()[7][5], rook, null);
        }
        System.out.println(king.getColor() + " castled!");
        this.current += 2;
        switchCurrentPlayer();
    }

    private void executeMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        addMove(start, end, startPiece, killedPiece);
        switchCurrentPlayer();
        current++;
        // showScore();
    }

    private void addMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        if (killedPiece != null) { // if the subsequent move has captured a piece
            checkIfKing(killedPiece); // refactor both into better and more useful methods
            addKilledPiece(killedPiece);
        }
        previousMoves.add(new Move(start, end, startPiece, killedPiece)); // killedPiece could be null,
    }

    private boolean hasPieceMoved(Square sq) { // check if the square has been utilized for a move

        for (Move moves : previousMoves) {
            if (moves.getStartingPair().equals(sq.getCoord()) || moves.getEndingPair().equals(sq.getCoord())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPawnMove(Square start, Square end, Piece pawn, Piece killedPiece) { //handles all pawn shenaningans
        if(!checkSoundMove(start, end, pawn, killedPiece)) return false;

        if (getPrevMove() != null && isEnPassant(getPrevMove(), start.getCoord(), end.getCoord(), pawn)) {  //truthy 
            System.out.println("You just got enpassanted cuhhhhhhh at " + end.getCoord());
            executeMove(start, end, pawn, killedPiece);
            return true;
        }

        if (isValidPromotion(pawn, start, end)) { // checks if it is promotion move, could offer better signifers bc just executing move says a queen made promotion 
            pawn = new Queen(pawn.getColor()); 
            executeMove(start, end, pawn, null);
            return true;
        }

        Pawn pPawn = (Pawn) pawn;
        boolean test = pPawn.validOrNah(start.getCoord(), end.getCoord(), killedPiece) && checkPiecesPath(
                pawn.getPiecePath(start.getCoord(), end.getCoord()), start.getCoord(), end.getCoord());

        if (test) {
            executeMove(start, end, pawn, killedPiece); // passes
            return true;
        } else {
            System.out.println("Illegal pawn manuever at " + start.getCoord());
            return false;
        }
    }

    private boolean isEnPassant(Move prevMove, Pair start, Pair end, Piece pawn) {
        Piece opponentPiece = prevMove.getPieceMoved();

        if (checkEnPassantCond(prevMove, opponentPiece, pawn))
            return false;

        if (opponentPiece.getColor() == ColorType.Black && prevMove.getEndingPair().getY() == start.getY()) { // white captures black en passant                                                                           
            return end.getY() + 1 == prevMove.getEndingPair().getY() && end.getX() == prevMove.getEndingPair().getX();
        }

        if (opponentPiece.getColor() == ColorType.White && prevMove.getEndingPair().getY() == start.getY()) { // black captures white                                                            
            return end.getY() - 1 == prevMove.getEndingPair().getY() && end.getX() == prevMove.getEndingPair().getX();
        }

        return false;
    }

    private boolean checkEnPassantCond(Move prevMove, Piece opponentPiece, Piece pawn) { // sees if it satisfies basic prereqs                                                                                  

        return !(!isPawn(opponentPiece) || prevMove.getEndingPair().getY() != 3
                || prevMove.getEndingPair().getY() != 6 || opponentPiece.getColor() == pawn.getColor());

    }

    private boolean isPawn(Piece startPiece) {
        return startPiece.getType() == PieceType.PAWN;
    }

    private boolean checkStandardMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        boolean testPieceValidMove = !isPawn(startPiece) && startPiece.validOrNah(start.getCoord(), end.getCoord());

        boolean testPiecePath = checkPiecesPath(startPiece.getPiecePath(start.getCoord(), end.getCoord()),
                start.getCoord(), end.getCoord());

        if (testPieceValidMove && testPiecePath) { // for pieces other than pawns
            executeMove(start, end, startPiece, killedPiece);
        } else if (!testPiecePath) {
            // raise cannot hop over piece at xy
            return false;
        } else {
            Errors.pieceBreakRules(start, startPiece);
            return false;
        }

        return true;
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

    private boolean checkPiecesPath(List<Pair> path, Pair startXY, Pair endXY) { // recieves list of coordinates for a  moved piece and its ending position.

        Square[][] bd = board.getBoard();
        Piece startPiece = bd[startXY.getY()][startXY.getX()].getPiece();

        for (Pair pair : path) {

            if ((bd[pair.getY()][pair.getX()].hasPiece() && (!pair.equals(startXY)) && (!pair.equals(endXY)))) { //ignore start and end squares
                Piece pieceAtThisPos = bd[pair.getY()][pair.getX()].getPiece();
                System.out.println(startPiece.getReadablePiece() + " cannot hop over the "
                        + pieceAtThisPos.getReadablePiece() + " at " + pair);
                return false;
            } // Returns false if there are pieces blocking it from moving
        }
        return true;
    }

    private boolean loopThruPieces(Square[][] bd, Pair kingXY) { // Will return true if the current iterated piece can attack the king without things in the way
        for (int i = 0; i < bd.length; i++) {
            for (int j = 0; j < bd.length; j++) {

                Square sq = bd[i][j];
                if (sq.hasPiece()) {
                    Piece pieceHere = sq.getPiece();
                    Pair startXY = sq.getCoord();
                    if (pieceHasPathToKing(pieceHere, startXY, kingXY))
                        return true;
                    else
                        ;
                }
            }
        }
        return false; // we iterated through the whole board and no square has a piece that is putting
                      // current player in check
    }

    private boolean pieceHasPathToKing(Piece startPiece, Pair startXY, Pair kingXY) {
        return !isOwnedPiece(startPiece) && startPiece.validOrNah(startXY, kingXY)
                && checkPiecesPath(startPiece.getPiecePath(startXY, kingXY), startXY, kingXY); // because the pawn hasnt moved yet when we loop
                                                                                               
        // through to see the path it is blocked by intermediary movement
        // is it valid path
    }

    private Pair getKingPos(Square[][] bd) { // recursive function to find where king is, better performance than nested fors?

        for (int rank = 0; rank < bd.length; rank++) {
            for (int file = 0; file < bd[rank].length; file++) {
                Piece temp = bd[rank][file].getPiece();
                if (temp != null && temp.getColor() == currentplayer && temp.getType() == PieceType.KING) {
                    return bd[rank][file].getCoord();
                }
            }
        }
        return null;
    }

    private Pair getKingPos(Square[][] bd, int rank, int file) { // recursive function to find where king is, better performance than nested fors?

        Piece temp = bd[rank][file].getPiece();
        if (temp != null && temp.getColor() == currentplayer && temp.getType() == PieceType.KING) {
            return bd[rank][file].getCoord();
        } else {
            if (file < bd[rank].length - 1) { 
                return getKingPos(bd, rank, file + 1);
            } else if (rank < bd.length - 1) {
                return getKingPos(bd, rank + 1, 0); //stack overflow error still here
            } else {
                return getKingPos(bd, 0, 0);
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

    public List<Move> getMoves() { // previousMoves is storing twice
        previousMoves.stream().distinct().forEach(move -> {
            System.out.println(move);
        });
        return this.previousMoves;
    }

    private void undoMove() {
        Move previous = getPrevMove();

        if (previous == null)
            return;

        if (previous.getStartingPair() != previous.getEndingPair()) {
            addMove(previous.getEndingSquare(), previous.getStartingSquare(), previous.getPieceMoved(), null);
            if (previous.isCapture()) {
                board.setPiece(previous.getEndingSquare(), previous.getPieceKilled());
            }
        }
        previousMoves.remove(getPrevMove());
        switchCurrentPlayer();
    }

    public Move getPrevMove() {
        if (previousMoves.isEmpty())
            return null;
        return previousMoves.get(current - 1);
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

    public void printBoard() {
        //System.out.println("\t Black");
        board.showBoard();
        //bSystem.out.println("\t White");
    }

    private void switchCurrentPlayer() {
        if (currentplayer.equals(ColorType.White)) {
            currentplayer = ColorType.Black;
        } else {
            currentplayer = ColorType.White;
        }
    }

    /*
     * private boolean isCheckMate(){ if(!isCheck(currentplayer)) return false;
     * Square[][] bd = board.getBoard(); List<Pair> possibleKingPos = new
     * ArrayList<>(8); Pair currentKingPos = getKingPos(board.getBoard(), 0, 0); int
     * x = currentKingPos.getX(); int y = currentKingPos.getY(); //if not at end or
     * edge of the board 8 possible moves for(int rank = 0; rank < 3; rank++){
     * //right if(!bd[x+1][y+rank].hasPiece()) possibleKingPos.add(new Pair(x+1,
     * y+rank));
     * 
     * } for(int rank = 0; rank < 3; rank++){ //left if(!bd[x-1][y+rank].hasPiece())
     * possibleKingPos.add(new Pair(x-1, y+rank)); } for(int rank = 0; rank < 2;
     * rank++){ //left if(!bd[x][y+rank].hasPiece()) possibleKingPos.add(new
     * Pair(x-1, y+rank)); } return false; //get current King moves, }
     */

}
