package domain.Logic;

/*
CRUD CLI Chess game
*** TODO implemenet checkmate detection to automatically end game

enpassant logic bug

*/

import java.util.ArrayList;
import java.util.List;

import domain.Logic.Color.ColorType;
import domain.Pieces.King;
import domain.Pieces.Pawn;
import domain.Pieces.Piece;
import domain.Pieces.PieceType;
import domain.Pieces.Queen;

public class Game {

    private Board board;

    private ColorType currentplayer;

    private List<Move> previousMoves;

    private int current;

    private static Game g = new Game();

    public boolean over = false;

    private Game() {
        board = new Board();
        previousMoves = new ArrayList<>();
        currentplayer = ColorType.White;
        current = 0;
    }

    public static Game getGame() { // Implementation of Singleton Pattern, Only one game will occur at a time
        return g;
    }

    public void startGame() {
        printBoard();
    }

    public boolean isCheckMate() {
        return over;
    }

    // Starting point of each attemped move, returns true only if the move was valid and executed
    /*
    * param: numerical coordinates indicating the location of a move on the Board's 2D array
    *
    *
    */
    public boolean tryMove(int startx, int starty, int endx, int endy) {

        if (!inBounds(startx, starty) || !inBounds(endx, endy))
            return false;

        Square[][] bd = board.getBoard();
        Square start = bd[starty][startx];
        Square end = bd[endy][endx];

        Piece startPiece = start.getPiece();
        Piece killedPiece = end.getPiece();

        // to avoid null pointer
        if (startPiece == null) {
            Errors.noSuchPieceExists(start);
            return false;
        }
        //maybe check for check here

         // Special case for pawns, method handles enpasssant, promotion and regular moves
        if (isPawn(startPiece)) {
            return checkPawnMove(start, end, startPiece, killedPiece);
        }

        if (checkValidCastling(start, end)) {
            castlePieces(start, end);
            return true;
        }
        
        else if (checkSoundMove(start, end, startPiece, killedPiece)) {
            return checkStandardMove(start, end, startPiece, killedPiece);
        }

        return false;
    }

    private boolean checkPawnMove(Square start, Square end, Piece pawn, Piece killedPiece) {

        if (getPrevMove() != null && isEnPassant(getPrevMove(), start.getCoord(), end.getCoord(), pawn)) {
            //check sound move
            System.out.println("You just got enpassanted cuhhhhhhh at " + end.getCoord());
            executeMove(start, end, pawn, killedPiece);
            return true;
        }

        if (isValidPromotion(pawn, start, end)) { // checks if it is promotion move
            /*
            TODO 
            could offer better signifers bc just executing move says a queen made promotion 
            */
            executeMove(start, end, pawn, killedPiece);
            end.setPiece(new Queen(pawn.getColor()));
            return true;
        }

        Pawn pPawn = (Pawn) pawn;
        boolean test = pPawn.validOrNah(start.getCoord(), end.getCoord(), killedPiece) && checkPiecesPath(
                pawn.getPiecePath(start.getCoord(), end.getCoord()), start.getCoord(), end.getCoord());

        if (test) {
            if (!checkSoundMove(start, end, pawn, killedPiece))
                return false;
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

        if (opponentPiece.getColor() == ColorType.Black && prevMove.getEndingPair().getY() == start.getY()) { //white captues black enpassant
            return end.getY() + 1 == prevMove.getEndingPair().getY() 
                    && end.getX() == prevMove.getEndingPair().getX();
        }

        if (opponentPiece.getColor() == ColorType.White && prevMove.getEndingPair().getY() == start.getY()) { // black
            return end.getY() - 1 == prevMove.getEndingPair().getY() 
                    && end.getX() == prevMove.getEndingPair().getX();
        }

        return false;
    }

    private boolean checkEnPassantCond(Move prevMove, Piece opponentPiece, Piece pawn) { // fails if the taken piece is not a pawn, or at proper squares 
        //sound logic --- TODO


        ///
        return !(!isPawn(opponentPiece) || prevMove.getEndingPair().getY() != 3 || prevMove.getEndingPair().getY() != 6
                || opponentPiece.getColor() == pawn.getColor());

    }

    private boolean isValidPromotion(Piece pawn, Square start, Square end) { //need 2 check for soundness
        if(checkSoundMove(start, end, pawn, null)) return false;

        Pair startXY = start.getCoord();
        Pair endXY = end.getCoord();
        if (startXY.getX() == endXY.getX() && end.hasPiece())
            return false; // cannot move straight with something in the way

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

    private boolean checkStandardMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        boolean testValidMove = !isPawn(startPiece) 
                                        && startPiece.validOrNah(start.getCoord(), end.getCoord());

        boolean testPath = checkPiecesPath(startPiece.getPiecePath(start.getCoord(), end.getCoord()),
                start.getCoord(), end.getCoord());

        if (testValidMove && testPath) {
            executeMove(start, end, startPiece, killedPiece);
        } else if (!testPath) {
            // raise cannot hop over piece at xy
            return false;
        } else {
            Errors.pieceBreakRules(start, startPiece);
            return false;
        }

        return true;
    }

    private boolean checkSoundMove(Square start, Square end, Piece startPiece, Piece killedPiece) {

        if (start.equals(end)) {
            Errors.displayNoMovement();
            return false;
        }

        boolean test = isOwnedPiece(startPiece);

        if (!test) {
            System.out
                    .println("You do not own " + startPiece.getReadablePiece() + " at " + start.getCoord() + " silly");
            return false;
        }

        test = moveMakesCheck(start, end); //AHHHHHHHHH BUGGGINGGGGGGGGGGGGG
                                                                                     
        if (test) {
            System.out.println(startPiece + "made check"+start.getCoord());
            System.out.println("Cannot move " + startPiece.getReadablePiece() + " at " + start.getCoord()
                    + " as you are in check");

            return false;
        }

        test = isFriendlyFire(startPiece, killedPiece);

        if (test) {
            System.out.println("Cannot take your own piece silly");
            return false;
        }

        return true;
    }

    private boolean checkValidCastling(Square kingSQ, Square rookSQ) {
        if (!kingSQ.hasPiece() || !rookSQ.hasPiece()) {
            return false;
        }

        Piece king = kingSQ.getPiece();
        Piece rook = rookSQ.getPiece();

        if (!rook.validOrNah(rookSQ.getCoord(), kingSQ.getCoord())) { // did the rook do a valid horizontal cross?
            return false;
        }

        if (!checkPiecesPath(rook.getPiecePath(rookSQ.getCoord(), kingSQ.getCoord()), rookSQ.getCoord(),
                kingSQ.getCoord())) {
            Errors.piecesBlockingCastle();
            return false;
        }

        if (hasPieceMoved(kingSQ) || hasPieceMoved(rookSQ)) { // must have stayed in starting pos
            Errors.cannotCastleActivePieces();
            return false;
        }

        if (kingIsThreatened(rookSQ.getCoord(), kingSQ.getCoord())) {
            return false;
        }

        return king.getType() == PieceType.KING && rook.getType() == PieceType.ROOK;
    }

    // check if the square matches any squares in the move list already
    private boolean hasPieceMoved(Square sq) {

        for (Move moves : previousMoves) {
            if (moves.getStartingPair().equals(sq.getCoord()) || moves.getEndingPair().equals(sq.getCoord())) {
                return true;
            }
        }
        return false;
    }

    private void castlePieces(Square kingSQ, Square rookSQ) {
        Piece king = kingSQ.getPiece();
        Piece rook = rookSQ.getPiece();
        if (rookSQ.getCoord().getX() == 0 && rookSQ.getCoord().getY() == 0) { // queen side black
            addMove(kingSQ, board.getBoard()[0][2], king, null);
            addMove(rookSQ, board.getBoard()[0][3], rook, null);
        }
        if (rookSQ.getCoord().getX() == 7 && rookSQ.getCoord().getY() == 0) { // short black
            addMove(kingSQ, board.getBoard()[0][6], king, null);
            addMove(rookSQ, board.getBoard()[0][5], rook, null);
        }
        if (rookSQ.getCoord().getX() == 0 && rookSQ.getCoord().getY() == 7) { // queen side white
            addMove(kingSQ, board.getBoard()[7][2], king, null);
            addMove(rookSQ, board.getBoard()[7][3], rook, null);
        }
        if (rookSQ.getCoord().getX() == 7 && rookSQ.getCoord().getY() == 7) { // sort white
            addMove(kingSQ, board.getBoard()[7][6], king, null);
            addMove(rookSQ, board.getBoard()[7][5], rook, null);
        }
        System.out.println(king.getColor() + " castled!");
        /*
        execute move twice instead of adding two moves for each case
        */
        this.current += 2;
        switchCurrentPlayer();
    }

    //
    private boolean kingIsThreatened(Pair rookXY, Pair kingXY) {
        int length = Math.abs(rookXY.getX() - kingXY.getX());
        int minpos = Math.min(rookXY.getX(), kingXY.getX()); // start at leftmost pos

        // see if a square on the kings horiz move can be reached
        for (int i = 0; i < length; i++) {
            Pair currentXY = new Pair(minpos + i, kingXY.getY());
            if (isPieceBeingAttkd(currentXY))
                return true;
        }
        return false;
    }

    private void executeMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        addMove(start, end, startPiece, killedPiece);
        switchCurrentPlayer();
        current++;
    }

    private void addMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        if (killedPiece != null) { // if the subsequent move has captured a piece
            checkIfKing(killedPiece); // refactor both into better and more useful methods
        }
        previousMoves.add(new Move(start, end, startPiece, killedPiece)); // killedPiece could be null,
    }
    
    //Scans the current move to see it implicates check
    private boolean moveMakesCheck(Square start, Square end) {
        Piece temp = end.getPiece();
        end.setPiece(start.getPiece());
        start.killPiece();

        boolean causedCheck = false;

        Pair kingXY = getKingPos(board.getBoard());

        if (isPieceBeingAttkd(kingXY)) {
            causedCheck = true;
            System.out.println(end.getPiece().getColor() + " is in check");
        }

        start.setPiece(end.getPiece()); // reset it back into place
        end.setPiece(temp);

        return causedCheck;
    }

    private Pair getKingPos(Square[][] bd) {

        for (int rank = 0; rank < bd.length; rank++) {
            for (int file = 0; file < bd[rank].length; file++) {
                Piece temp = bd[rank][file].getPiece();
                if (temp != null && isOwnedPiece(temp) && temp.getType() == PieceType.KING) {
                    return bd[rank][file].getCoord();
                }
            }
        }
        return null;
    }
    
    //logi error 
    /*
    param will usually be the Kings position on the board
    */
    private boolean isPieceBeingAttkd(Pair endXY) { 
        Square[][] bd = board.getBoard();
        for (int rank = 0; rank < bd.length; rank++) {
            for (int file = 0; file < bd.length; file++) {

                Square sq = bd[rank][file];
                Piece pieceHere = sq.getPiece();
                if (sq.hasPiece()
                    && pieceHasValidPath(pieceHere, sq.getCoord(), endXY)) 
                        return true;
                
            }
        }
        return false;
    }

    /*
     * will return false, when it is an owned piece, the attacking move is invalid,
     * and when there are obstructions in the then validated path
     */
    private boolean pieceHasValidPath(Piece startPiece, Pair startXY, Pair endXY) { 

        //Special case for pawns because its moves validity depends if there is a vacancy at end square
        if(startPiece instanceof Pawn){
            Pawn pPawn = (Pawn) startPiece;
            return !isOwnedPiece(startPiece) && pPawn.validOrNah(startXY, endXY, new King(currentplayer)) 
                    && checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY);
        }
        //We make sure its not a friendly piece attacking first
        //Then check if it is a feasible path within the pieces ruleset
        //Finally see if the path has pieces blocking it
                    
        return !isOwnedPiece(startPiece) && startPiece.validOrNah(startXY, endXY)
                && checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY);

    }

    private boolean checkPiecesPath(List<Pair> path, Pair startXY, Pair endXY) { //checking discovered checks is causing error message to be raised even if done implicitly
        Square[][] bd = board.getBoard();
        Piece startPiece = bd[startXY.getY()][startXY.getX()].getPiece();
        boolean flag = true;

        //iterate thru list of pair values taken by Pieces path to end goal
        for (Pair pair : path) {
            if (squareIsOccupied(bd, pair, startXY, endXY)) {
                flag = false;
                Errors.pathIsBlocked(startPiece, bd[pair.getY()][pair.getX()].getPiece(), pair);
                break;
            }
        }

        return flag;
    }

    private boolean squareIsOccupied(Square[][] bd, Pair pair, Pair startXY, Pair endXY) {
        // Ignore the start and ending position of the path - irrelevant to the 
        //validity of the path

        return bd[pair.getY()][pair.getX()].hasPiece() && 
                (!pair.equals(startXY)) && (!pair.equals(endXY));

    }

    private boolean isOwnedPiece(Piece thispiece) {
        return thispiece.getColor() == this.currentplayer;
    }

    private boolean isFriendlyFire(Piece startPiece, Piece killedPiece) {
        if (killedPiece == null)
            return false;
        return startPiece.getColor() == killedPiece.getColor();
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    private void switchCurrentPlayer() {
        if (currentplayer.equals(ColorType.White)) {
            currentplayer = ColorType.Black;
        } else {
            currentplayer = ColorType.White;
        }
    }

    public List<Move> getMoves() { 
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

    private boolean isPawn(Piece startPiece) {
        return startPiece.getType() == PieceType.PAWN;
    }

    public ColorType getTurn() {
        return this.currentplayer;
    }

    public void printBoard() {
        // System.out.println("\t Black");
        board.showBoard();
        // bSystem.out.println("\t White");
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
    /*
     * private void showScore() { for (List<Piece> p : pieceskilled.values()) { int
     * score = 39 - p.stream().mapToInt(Piece::getValue).sum();
     * System.out.println("'s score: " + score); } } to do ---- has logic errors
     */

}
