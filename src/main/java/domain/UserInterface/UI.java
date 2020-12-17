package domain.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import domain.Logic.Errors;
import domain.Logic.Game;
import domain.Logic.Pair;
import domain.Pieces.Visitor;

public class UI {
    private Game game;
    // private Scanner sc;
    private BufferedReader sc;
    // private Visitor<> visitor;

    public UI(Game game, BufferedReader in) {
        this.game = game;
        this.sc = in;
    }

    public void showGreeting() {
        System.out.println("Welcome to chess! Let's begin");
        game.startGame();

    }

    private void gameIsOver() {
        System.out.println("GG!");
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
    private boolean isInputValid(String input) {
        return !(input.isBlank() || input.length() < 2);
    }

    public void getMoves() throws IOException {
        while (!game.isCheckMate()) {
            try {
                System.out.print(game.getTurn() + "'s turn, ");

                System.out.println("enter the square you wish to move from.");
                String input1 = sc.readLine();

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
                String input2 = sc.readLine();

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
