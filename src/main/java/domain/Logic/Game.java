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
    need to add enpassant and castling
    Check detection bug bc recycling piece path method and current players move has not fully rendered so it is still temp blocking even though it will not be and cause a check  
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

    private int current;

    private static Game g = new Game();

    private Game() { // each game creates a new board, set of moves taken, and sets the player to white   

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

    public boolean notDone() {
        return !over;
    }

    public boolean validMove(int startx, int starty, int endx, int endy) { // need checkmate detection, refactor ifs,
                                                                             // main logic funct for moving,. split into if functions
        if (!inBounds(startx, starty) || !inBounds(endx, endy))
            return false;

        Square[][] temp = board.getBoard();
        Square start = temp[starty][startx];
        Square end = temp[endy][endx];
        if (start.equals(end)) {
            Errors.displayNoMovement();
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

        test = moveMakesCheck(start, end); //does the move cause the current player to get in check

        if(test){
            System.out.println("Cannot move " + startPiece.getReadablePiece() + "at" + startXY.getReadablePair() + " as you are in check");
            return false;
        }

        test = isFriendlyFire(startPiece, killedPiece);
        if (test) {
            System.out.println("Cannot take your own piece silly");
            return false;
        }

        boolean testPawn = isPawn(startPiece);
        // special case for pawns add enpassant in valid or nah implementation, not sure actually
        if (testPawn) {
            Pawn pawn = (Pawn) startPiece;

            // first check if the Piece can make the move, than look for barriers, and then
            // go to add move which checks its further validity

            testPawn = pawn.validOrNah(start.getCoord(), end.getCoord(), killedPiece)
                    && checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY);

            if (testPawn) {
                wasValidMove(start, end, startPiece, killedPiece); //passes
                return true;
            } else {
                System.out.println("Illegal pawn manuever at " + start.getCoord().getReadablePair());
                return false;
            }
        }

        boolean testPieceValidMove = !isPawn(startPiece) && startPiece.validOrNah(start.getCoord(), end.getCoord());
        boolean testPiecePath = checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY);

        if (testPieceValidMove && testPiecePath) { //for pieces other than pawns
            wasValidMove(start, end, startPiece, killedPiece);
        } 
        else if (!testPiecePath) {
                return false;
        } else {
                System.out.println("Illegal move at " + start.getCoord().getReadablePair() + " " + startPiece.getType()
                        + " cannot make those manuevers"); // need 2 specify
                return false;
        }

        return true;
    }

    private boolean moveMakesCheck(Square start, Square end){ //not sure about logic here
        Piece temp = end.getPiece();
        end.setPiece(start.getPiece());
        start.killPiece();

        if(isCheck(end.getPiece().getColor())){
            start.setPiece(end.getPiece()); //reset it 
            end.setPiece(temp);
            return true;
        }else{
            start.setPiece(end.getPiece()); //move is all gravy
            end.setPiece(temp);
        }
        return false;
    }
    private void wasValidMove(Square start, Square end, Piece startPiece, Piece killedPiece){
        addMove(start, end, startPiece, killedPiece);
        switchCurrentPlayer();
        current++; 
        //printBoard();
        //showScore();
    }

    private void addMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        if (killedPiece != null) { // if the subsequent move has captured a piece

            checkIfKing(killedPiece); // refactor into better method
            addKilledPiece(killedPiece);
        }
        previousMoves.add(new Move(start, end, startPiece, killedPiece)); //killedPiece could be null, 
    }

    private boolean isCheckMate(){
        if(!isCheck(currentplayer)) return false;
        Square[][] bd = board.getBoard();
        List<Pair> possibleKingPos = new ArrayList<>(8);
        Pair currentKingPos = getKingPos(board.getBoard(), 0, 0);
        int x = currentKingPos.getX();
        int y = currentKingPos.getY();
        //if not at end or edge of the board 8 possible moves
        for(int rank = 0; rank < 3; rank++){ //right
            if(!bd[x+1][y+rank].hasPiece())
                possibleKingPos.add(new Pair(x+1, y+rank));
            
        }
        for(int rank = 0; rank < 3; rank++){ //left
            if(!bd[x-1][y+rank].hasPiece())
                possibleKingPos.add(new Pair(x-1, y+rank));
        }
        for(int rank = 0; rank < 2; rank++){ //left
            if(!bd[x][y+rank].hasPiece())
                possibleKingPos.add(new Pair(x-1, y+rank));
        }
        return false;
        //get current King moves, 
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
        Piece startPiece = bd[startXY.getY()][startXY.getX()].getPiece();

        for (Pair pair : path) {

            if ((bd[pair.getY()][pair.getX()].hasPiece() && (!pair.equals(startXY)) && (!pair.equals(endXY)))) { //ignore the start and end squares, only care if obstructions in btw
                Piece pieceAtThisPos = bd[pair.getY()][pair.getX()].getPiece();
                //System.out.println(startPiece.getReadablePiece() + " cannot hop over the " + pieceAtThisPos.getReadablePiece()+" at " + pair);
                return false;
            } // Returns false if there are pieces blocking it from moving
        }
        return true;
    }

    private boolean isCheck(ColorType testColor) { // iterate through pieces to find king and its location then iterate through 
                                // oppositite colors pieces to see if they have a valid path from present location to King,

        Pair kingXY = null;
        if (testColor == ColorType.White) { 
            kingXY = getKingPos(board.getBoard(), 7, 0); // Find current players Kings position on the board
        } else {
            kingXY = getKingPos(board.getBoard(), 0, 0);
        }
        // delegate looping to another func
        return loopThruPieces(board.getBoard(), kingXY);
    }

    private boolean loopThruPieces(Square[][] bd, Pair kingXY) { //Will return true if the current iterated piece can attack the king without things in the way
        for (int i = 0; i < bd.length; i++) {
            for (int j = 0; j < bd.length; j++) {

                Square sq = bd[i][j];
                if (sq.hasPiece()) {
                    Piece pieceHere = sq.getPiece();
                    Pair startXY = sq.getCoord();
                    if(pieceHasPathToKing(pieceHere, startXY, kingXY)) return true;
                    else ;
                }
            }
        }
        return false; //we iterated through the whole board and no square has a piece that is putting current player in check
    }
    private boolean pieceHasPathToKing(Piece startPiece, Pair startXY, Pair kingXY){
        return !isOwnedPiece(startPiece) && startPiece.validOrNah(startXY, kingXY) 
                && checkPiecesPath(startPiece.getPiecePath(startXY, kingXY),startXY, kingXY); //because the pawn hasnt moved yet when we loop 
                //through to see the path it is blocked by intermediary movement
                //is it valid path 
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

    private void undoMove(){
        Move previous = getPrevMove();
        if(previous.getStartingPair() != previous.getEndingPair()){
            addMove(previous.getEndingSquare(), previous.getStartingSquare(), previous.getPieceMoved(), null);
            if(previous.isCapture()){
                board.setPiece(previous.getEndingSquare(),previous.getPieceKilled());
            }
        }
        previousMoves.remove(getPrevMove());
        switchCurrentPlayer();
    }

    private Move getPrevMove(){
        return previousMoves.get(current-1);
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
