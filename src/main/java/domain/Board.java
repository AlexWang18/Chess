package domain;

import java.util.Arrays;

import domain.Color.ColorType;
import domain.Pieces.Bishop;
import domain.Pieces.King;
import domain.Pieces.Knight;
import domain.Pieces.Pawn;
import domain.Pieces.Piece;
import domain.Pieces.Queen;
import domain.Pieces.Rook;

public class Board { //handles displaying the board and subsequent changes
    private static final int SIZE = 8;
    private Square[][] bd = new Square[SIZE][SIZE];

    public Board(){
        clearBoard();
        setWhitePieces();
        setBlackPieces();
    }
    public void clearBoard(){
        for (int rank = 0; rank < bd.length; rank++) {
            for (int file = 0; file < bd.length; file++) {
                bd[rank][file] = new Square(new Pair(file,rank)); //x,y notation for pair
            }
        }
    }
    public void setWhitePieces(){
        for (int file = 0; file < bd.length; file++) {
            bd[6][file].setPiece(new Pawn(ColorType.White));
        } 
        bd[7][2].setPiece(new Bishop(ColorType.White));
		bd[7][5].setPiece(new Bishop(ColorType.White));
		bd[7][1].setPiece(new Knight(ColorType.White));
		bd[7][6].setPiece(new Knight(ColorType.White));
		bd[7][0].setPiece(new Rook(ColorType.White));
		bd[7][7].setPiece(new Rook(ColorType.White));
		bd[7][3].setPiece(new Queen(ColorType.White));
		bd[7][4].setPiece(new King(ColorType.White));
    }
    public void setBlackPieces(){
        for (int file = 0; file < bd.length; file++) {
            bd[1][file].setPiece(new Pawn(ColorType.Black));
        }
        bd[0][2].setPiece(new Bishop(ColorType.Black)); 
        bd[0][5].setPiece(new Bishop(ColorType.Black));
        bd[0][1].setPiece(new Knight(ColorType.Black));
        bd[0][6].setPiece(new Knight(ColorType.Black));
        bd[0][0].setPiece(new Rook(ColorType.Black));
        bd[0][7].setPiece(new Rook(ColorType.Black));
        bd[0][3].setPiece(new Queen(ColorType.Black));
        bd[0][4].setPiece(new King(ColorType.Black));
    }
    public Square[][] getBoard(){
        return this.bd;
    }
    
    public void showBoard(){
        System.out.println("\tBlack");
        for (int i = 0; i < bd.length; i++) {
            System.out.println((i+1) +" " +Arrays.toString(bd[i]).replaceAll("\\[|]|,", "")); //print each bd string rep
        }
        System.out.println("  a b c d e f g h"); 
        System.out.println("\tWhite");
    }
}