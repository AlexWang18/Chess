package domain.Logic;

import java.io.IOException;

/*

CRUD CLI Chess game

could get rid of null checking and have a blank or empty piecetype
TODO refactor move checking into Move class, could have an abstract move class with promotion, castling, normal, capture pawn etc
*/

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import domain.Logic.Color.ColorType;
import domain.Pieces.*;
import domain.Pieces.Visitor.*;
import domain.UserInterface.UI;

public final class Game { //prohibit inheritance

    private Board board;

    private ColorType currentplayer;

    private List<Move> previousMoves;

    private int current;

    private static Game g = new Game();

    public boolean over = false;

    private Visitor<Boolean> visitor; //Should i have encapsulated this? Could have alternatively passed it so we make sure not null

    private Game() {
        board = new Board();
        previousMoves = new ArrayList<>();
        currentplayer = ColorType.White;
        current = 0;
    }

    public static Game getGame() { // Implementation of Singleton Pattern, Only one game will occur at a time   
        return g;
    }

    public boolean isGameOver() {
        return over;
    }

    public void setMode(Visitor<Boolean> pVisitor){
        this.visitor = pVisitor;
    }

    // Starting point of each attemped move, returns true only if the move was valid
    // and executed
    /*
     * param: numerical coordinates indicating the location of a move on the Board's
     * 2D array
     * TODO Need param of visitor - Instead of accessing directly the subs implemented class, we just accept the passed visitor.. 
     * I could put a lot of the logic inside concrete visitors like if the conditions are true invoke behav
     */
    public boolean tryMove(int startx, int starty, int endx, int endy) throws IOException {

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

        // Special case for pawns, method handles enpasssant, promotion and regular moves
        if (isPawn(startPiece)) {
            return checkPawnMove(start, end, startPiece, killedPiece);
        }

        // If they opt to castle, they enter the King and Rooks squares
        if (killedPiece != null && startPiece.getColor() == killedPiece.getColor() && checkValidCastling(start, end)) {

            castlePieces(start, end);
            return true;

        }

        else if (checkSoundMove(start, end, startPiece, killedPiece)) {
            return checkStandardMove(start, end, startPiece, killedPiece);
        }

        return false;
    }

    private boolean checkPawnMove(Square start, Square end, Piece pawn, Piece killedPiece) throws IOException {

        if (!checkSoundMove(start, end, pawn, killedPiece))
            return false;

        if (getPrevMove() != null && isEnPassant(getPrevMove(), start.getCoord(), end.getCoord(), pawn)) {
            executeEnPassant(start, end, pawn);
            return true;
        }
        Pawn pPawn = (Pawn) pawn;

        if (isValidPromotion(pPawn, start, end)) { 
            /*
             // how do i get the users response after reaching end of board, should i have a
            // two way relationship b/tw UI and Game or keep it has a unidirectional
            */
            executeMove(start, end, pawn, killedPiece);
            return promote(pawn.getColor(), end, start.getCoord());
        }
        
        boolean test = visitor.invokeBehavior(pawn ,start.getCoord(), end.getCoord(), killedPiece)
                        && checkPiecesPath(pawn.getPiecePath(start.getCoord(), end.getCoord()), start.getCoord(),
                        end.getCoord()).value;

        if (test) {
            executeMove(start, end, pawn, killedPiece); // passes
            return true;
        } else {
            Errors.pawnBreaksRules(start);
            return false;
        }

    }

    private boolean isEnPassant(Move prevMove, Pair start, Pair end, Piece pawn) {
        Piece opponentPiece = prevMove.getPieceMoved();

        if (checkEnPassantCond(prevMove, opponentPiece, pawn)) return false;

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

    private boolean checkEnPassantCond(Move prevMove, Piece opponentPiece, Piece pawn) { 
        //has to be a 2 square pawn move, 3 and 6 are the files on each side respectively
        int blackDoubleJumpPos = 3;
        int whiteDoubleJumpPos = 6;
        if(Math.abs(prevMove.getEndingPair().getY() - prevMove.getStartingPair().getY()) != 2) return false; 
        
        // fails if the taken piece is not a pawn, or at the proper squares 

        return !(!isPawn(opponentPiece) || prevMove.getEndingPair().getY() != blackDoubleJumpPos 
                || prevMove.getEndingPair().getY() != whiteDoubleJumpPos
                    || opponentPiece.getColor() == pawn.getColor());

    }

    private void executeEnPassant(Square start, Square end, Piece pawn){
            System.out.println("You just got enpassanted cuhhhhhhh at " + end.getCoord());

            Piece enPassantedOn = getPrevMove().getPieceMoved();
            getPrevMove().getEndingSquare().killPiece(); //sets it to null

            executeMove(start, end, pawn, enPassantedOn);
    }


    private boolean isValidPromotion(Pawn pawn, Square start, Square end) { 

        Pair startXY = start.getCoord();
        Pair endXY = end.getCoord();
        Piece killedPiece = end.getPiece(); //could be null

        if (startXY.getX() == endXY.getX() && end.hasPiece())
            return false; // cannot move straight with something still in the way

        if (pawn.validOrNah(startXY, endXY, killedPiece)) {
            return atEndOfBoard(pawn, endXY);
        }

        return false;
    }

    private boolean promote(ColorType color, Square end, Pair readableXY) throws IOException {

        String pieceChoice = UI.parsePieceChoice(readableXY.toString()); //idk abt this static method association and this implementation in general - g


        String knightRegex = "^(A\\.*|k(night)?)$"; //   '\\' need to use double backslash becasue \ is already used as an escape sequence in java
        String bishopRegex = "^(B\\.*|b(ishop)?)$";
        
        String rookRegex = "^(C\\.*|r(ook)?)$";
        String queenRegex = "^(D\\.*|q(ueen)?)$";

        //could use Switch on a boolean, but opted for ifs for readability
        
        if (pieceChoice.matches(bishopRegex)) {
            end.setPiece(new Bishop(color));
        } else if (pieceChoice.matches(knightRegex)) {
            end.setPiece(new Knight(color));
        } else if (pieceChoice.matches(rookRegex)) {
            end.setPiece(new Rook(color));
        } else if (pieceChoice.matches(queenRegex)) {
            end.setPiece(new Queen(color));
        } else {
            return false;
        }

        return true;
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
        boolean testValidMove =  visitor.invokeBehavior(startPiece ,start.getCoord(), end.getCoord(), killedPiece);

        //Message holds data about the legality of move
        Message moveData = checkPiecesPath(startPiece.getPiecePath(start.getCoord(), end.getCoord()),
                        start.getCoord(), end.getCoord());

        boolean testPath = moveData.value;

        if (testValidMove && testPath) {
            executeMove(start, end, startPiece, killedPiece);
        } else if (!testPath) {
            Errors.pathIsBlocked(moveData);
            return false;
        } else {
            Errors.pieceBreakRules(start, startPiece);
            return false;
        }

        return true;
    }

    private boolean checkSoundMove(Square start, Square end, Piece startPiece, Piece killedPiece) {

        if (start.equals(end)) { //anon class implementation
            return new Error(){
                @Override
                public boolean displayError() { //no movement
                    System.out.println("You should move at least 1 square");
                    return false;
                }
            }.displayError();
            
         /*   Consumer<String> display = str -> System.out.println(str); //
            display.accept("You should move at least 1 square"); */ //cant use bc does not return any value, only applies an operation on the argument
        }

        boolean test = isOwnedPiece(startPiece);

        if (!test) {
            return Errors.unOwnedPiece(start, startPiece); 
        }

        ImmutablePair<Pair, Boolean> testCheck = moveMakesCheck(start, end); 
                                                                                     
        if (Boolean.TRUE == testCheck.right) {

            //pass in the Kings position to see if mate, ends game if true
            if(isCheckMate(testCheck.left)){
                over = true;
                return false;
            }

            return Errors.isInCheck(startPiece, start);
        }

        test = isFriendlyFire(startPiece, killedPiece);

        if (test) {
            return Errors.friendlyFire(end, killedPiece);
        }

        return true;
    }

    //
    private boolean checkValidCastling(Square kingSQ, Square rookSQ) {
        if (!kingSQ.hasPiece() || !rookSQ.hasPiece()) {
            return false;
        }
        
        Piece king = kingSQ.getPiece();
        Piece rook = rookSQ.getPiece();

        if(king.getType() == PieceType.KING){
            assert rook.getType() == PieceType.ROOK;
        }

        else if(king.getType() == PieceType.ROOK){ //swap the references
            Piece temp = king;
            king = rook;
            rook = temp;
        }

        Pair kingXY = kingSQ.getCoord();
        Pair rookXY = rookSQ.getCoord();

        if (!rook.validOrNah(rookXY, kingXY)) { // did the rook do a valid horizontal cross?
            return false;
        }

        if (!checkPiecesPath(rook.getPiecePath(rookXY, kingXY), rookXY,
        kingXY).getBool()) {
            return Errors.piecesBlockingCastle();
        }

        if (hasPieceMoved(kingSQ) || hasPieceMoved(rookSQ)) { // must have stayed in starting pos
            return Errors.cannotCastleActivePieces();
            
        }

        if (kingIsThreatened(rookXY, kingXY)) { //cannot move through an enemy threatened square
            return false;
        }

        return king.getType() == PieceType.KING && rook.getType() == PieceType.ROOK;
    }

    private boolean kingIsThreatened(Pair rookXY, Pair kingXY) {
        int length = Math.abs(rookXY.getX() - kingXY.getX());
        
        // see if a square on the kings horiz move can be reached
        for (int i = 0; i < length; i++) {
            Pair currentXY = new Pair(kingXY.getX() + i, kingXY.getY());
            ImmutablePair<Pair,Boolean> ip = isPieceBeingAttkd(currentXY);

            if (Boolean.TRUE.equals(ip.right)){
                Errors.castleIsThreatened(ip.left);
                return true;
            }
                
        }
        return false;
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
    
        this.current += 2;
        switchCurrentPlayer();
    }

    private void executeMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        addMove(start, end, startPiece, killedPiece);
        switchCurrentPlayer();
        current++;
    }

    private void addMove(Square start, Square end, Piece startPiece, Piece killedPiece) {
        previousMoves.add(new Move(start, end, startPiece, killedPiece)); // killedPiece could be null in any instance
    }
    
    //Scans the current move to see it implicates check.. 
    private ImmutablePair<Pair, Boolean> moveMakesCheck(Square start, Square end) {
        Piece temp = end.getPiece();
        end.setPiece(start.getPiece());
        start.killPiece();

        boolean causedCheck = false;

        Pair kingXY = getKingPos(board.getBoard()); /*returning null when King is the piece moving and attacking pieces cannot have a valid path to a null end pair*/
        
        ImmutablePair<Pair,Boolean> ip = isPieceBeingAttkd(kingXY);
        if (Boolean.TRUE.equals(ip.right)) {
            causedCheck = true;
        }

        start.setPiece(end.getPiece()); // reset it back into place
        end.setPiece(temp);

        return new ImmutablePair<Pair,Boolean>(kingXY, causedCheck);
    }

    private boolean isCheckMate(Pair currentKingXY) {
        
        int x = currentKingXY.getX(); 
        int y = currentKingXY.getY();  
        int GIRTH = Board.getSize();
        
        List<Pair> possibleKingPos = new ArrayList<>(8); 

        //Adding possible moves for the passed king
        
        if((y + 1) < GIRTH){ //upper level
            possibleKingPos.add(new Pair(x, y + 1));
            if(x - 1 >= 0){
                possibleKingPos.add(new Pair(x-1, y+1));
            }
            if(x + 1 < GIRTH){
                possibleKingPos.add(new Pair(x+1, y+1));
            }
        }

        if((y - 1) >= 0){ //lower
            possibleKingPos.add(new Pair(x,y-1));
            if(x - 1 >= 0){
                possibleKingPos.add(new Pair(x-1, y-1));
            }
            if(x + 1 < GIRTH){
                possibleKingPos.add(new Pair(x+1, y-1));
            }
        }

        if(x - 1 >= 0){ //same level
            possibleKingPos.add(new Pair(x-1,y));
        }
        if(x + 1 < GIRTH){
            possibleKingPos.add(new Pair(x+1,y));
        }

        //get rid of the moves that have pieces already occupying it, than filter the remaining if it is being attacked

        List<? extends Pair> validKingMoves = possibleKingPos.stream().filter(m->!board.getBoard()[m.getY()][m.getX()].hasPiece()).
            filter(p -> !isPieceBeingAttkd(p).right).collect(Collectors.toList());

        //if list is empty than there is no valid place for the king to go
        return validKingMoves.isEmpty();
    }

    private Pair getKingPos(Square[][] bd) {

        for (int rank = 0; rank < bd.length; rank++) {
            for (int file = 0; file < bd[rank].length; file++) {
                Piece temp = bd[rank][file].getPiece();
                if (temp != null && temp.getType() == PieceType.KING && isOwnedPiece(temp))  {
                    return bd[rank][file].getCoord();
                }
            }
        }
        return bd[0][0].getCoord(); //avoid null pointer for now
    }
    
    /*
    param will usually be the Kings position on the board
    */
    private ImmutablePair<Pair,Boolean> isPieceBeingAttkd(Pair endXY) { //return a tuple to store the pair where error message should point
        Square[][] bd = board.getBoard();
        for (int rank = 0; rank < bd.length; rank++) {
            for (int file = 0; file < bd.length; file++) {

                Square sq = bd[rank][file];
                Piece pieceHere = sq.getPiece();

                if (sq.hasPiece()
                    && pieceHasValidPath(pieceHere, sq.getCoord(), endXY)) {
                        return new ImmutablePair<Pair,Boolean>(endXY,true);}
                
            }
        }
        return new ImmutablePair<Pair,Boolean>(endXY, false);
    }

    /*
    
     */
    private boolean pieceHasValidPath(Piece startPiece, Pair startXY, Pair endXY) { 

        //Special case for pawns because its moves validity depends if there is a vacancy at end square
        if(startPiece instanceof Pawn){
            Pawn pPawn = (Pawn) startPiece;
            return !isOwnedPiece(startPiece) && pPawn.validOrNah(startXY, endXY, new King(currentplayer)) 
                    && checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY).value;
        }

        //We make sure its not a friendly piece attacking first
        //Then check if it is a feasible path within the pieces ruleset
        //Finally see if the path has pieces blocking it
                    
        return !isOwnedPiece(startPiece) && startPiece.validOrNah(startXY, endXY)
                && checkPiecesPath(startPiece.getPiecePath(startXY, endXY), startXY, endXY).value;

    }

    private Message checkPiecesPath(List<Pair> path, Pair startXY, Pair endXY) { //checking discovered checks is causing error message to be raised even if done implicitly
        Square[][] bd = board.getBoard();
        Piece startPiece = bd[startXY.getY()][startXY.getX()].getPiece();
        Message flag = new Message(startPiece, null, null, true);
        
        //iterate thru list of pair values taken by Pieces path to end goal
        for (Pair pair : path) {
            if (squareIsOccupied(bd, pair, startXY, endXY)) {

                flag = new Message(startPiece, bd[pair.getY()][pair.getX()].getPiece(), pair, false);
                
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

    private boolean isOwnedPiece(Piece thisPiece) {
        return thisPiece.getColor() == this.currentplayer;
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

    public Move getPrevMove() {
        if (previousMoves.isEmpty())
            return null;
        return previousMoves.get(current - 1);
    }

    private boolean isPawn(Piece startPiece) {
        return startPiece.getType() == PieceType.PAWN;
    }

    public ColorType getTurn() {
        return this.currentplayer;
    }

    public Square[][] getBoard(){
        return board.getBoard();
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

}
