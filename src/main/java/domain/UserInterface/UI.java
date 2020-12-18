package domain.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import java.util.InputMismatchException;

import domain.Logic.Errors;
import domain.Logic.Game;
import domain.Logic.Pair;
import domain.Pieces.Visitor;

public class UI {

    private Game game;
    
    private Reader r;
    // private Visitor<> visitor;

    public UI(Game game, Reader r) throws IOException {
        this.game = game;
        this.r = r;
        showGreeting();
        initGame();
        if(r instanceof BufferedReader){
            BufferedReader br = (BufferedReader) r;
            getMoves(br);
        }
    }

    public void showGreeting() {
        System.out.println("Welcome to chess! \nLowercase letters is black, and uppercase is white. \nLet's begin");
    }

    public void initGame(){
        game.startGame();
    }

    private void gameIsOver() {
        System.out.println("GG!");
    }

    private boolean isInputValid(String input) {
        return !(input.isBlank() || input.length() < 2);
    }

    //Prompting for user input until game is over
    public void getMoves(BufferedReader br) throws IOException {
        while (!game.isCheckMate()) {
            try {
                System.out.print(game.getTurn() + " to move, ");

                System.out.println("enter the square you wish to move from.");
                String input1 = br.readLine();

                if(!isInputValid(input1))
                    continue;

                Pair startingPair = getPair(input1.toLowerCase());

                if(!checkPair(startingPair)){
                    System.out.println("Invalid square to move to at " + startingPair + " retry!");
                    continue;
                }

                int startfile = startingPair.getX();
                int startrank = startingPair.getY();

                System.out.println("Enter the square you wish to move to.");
                String input2 = br.readLine();

                if (!isInputValid(input2))
                    continue;

                Pair endingPair = getPair(input2.toLowerCase());

                if (!checkPair(endingPair)) {
                    System.out.println("Invalid square to move to at " + endingPair + " retry!");
                    continue;
                }

                int endfile = endingPair.getX();
                int endrank = endingPair.getY();

                if (!game.tryMove(startfile, startrank, endfile, endrank))
                    Errors.moveException();
                else{
                    assert true; //move went thru, 
                    System.out.println(game.getPrevMove());
                }

            } catch (InputMismatchException exception) {
                System.out.println("Error occured in scanning users input   ");
            }
            finally{
                game.printBoard();
            }
        }
        game.getMoves();
        gameIsOver();
    }

    private boolean checkPair(Pair p) {
        return p.isPairValid();
    }

    private Pair getPair(String input){
        return new Pair(input);
    }
}
 /*
     * private void setMode(){
     * System.out.println("What style of rules do you want to play?");
     * System.out.println("Classic, Silly, or None"); String input = sc.nextLine();
     * if(input.equals("Classic")){ visitor = new ClassicRules(); } else
     * if(input.equals("Silly")){ visitor = new Object(); //would i encapsulate the
     * pieces in Move, than accepting the created visitor } }
     */
    /*
     * add method parameter to specify ruleset?
     */