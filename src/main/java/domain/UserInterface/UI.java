package domain.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.InputMismatchException;

import domain.Logic.Errors;
import domain.Logic.Game;
import domain.Logic.Pair;
import domain.Pieces.Visitor;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class UI implements Runnable {

    private Game game;

    private static final String ENDCASE = "^(?i)(exit|stop)+$"; //embedded flag ? specifies case insensitive, + one or more times

    private Reader read;

    private static BufferedReader br;
    // private Visitor<> visitor;

    // we accept a implementation of the Reader interface as a parameter to
    // also pass different forms of readers for testing, e.g. fileReader

    public UI(Game g, Reader r) throws IOException {
        this.game = g;
        this.read = r;
    }

    @Override
    public void run() {
        showGreeting();

        if (this.read instanceof BufferedReader) { // testing if we are doing user input
            br = (BufferedReader) read;
            try {
                getMoves();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            System.err.println("testing");
    }

    public void showGreeting() {
        System.out.println("Welcome to CL Chess! \nLowercase letters is black, and uppercase is white. \nLet's begin");
        game.printBoard();
    }

    private void gameIsOver() {
        System.out.println("GG!");
    }

    private boolean isInputValid(String input) {

        return !(input.isBlank() || (input.length() > 2));

    }

    public boolean userEndCase(String... inputs) {
        for (String str : inputs) {
            if (str.matches(ENDCASE))
                return true;
        }
        return false;
    }

    // Prompting for user input until game is over
    public void getMoves() throws IOException {
        String input1 = "";
        String input2 = "";

        while (!game.isGameOver() && !userEndCase(input1, input2)) { // not working
            try {
                System.out.print(game.getTurn() + " to move, ");

                System.out.println("enter the square you wish to move from.");
                input1 = br.readLine();

                if (!isInputValid(input1))
                    continue;

                Pair startingPair = getPair(input1.toLowerCase());

                if (!checkPair(startingPair)) {
                    System.out.println("Invalid square to move to at " + startingPair + " retry!");
                    continue;
                }

                int startfile = startingPair.getX();
                int startrank = startingPair.getY();

                System.out.println("Enter the square you wish to move to.");
                input2 = br.readLine();

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
                else {
                    assert true; // move went thru,
                    System.out.println(game.getPrevMove());
                }

            } catch (InputMismatchException exception) {
                System.out.println("Error occured in scanning users input   ");
            }

            finally { // will print the board again after each attempt
                game.printBoard();
            }
        }
        game.getMoves();
        gameIsOver();
    }

    public static String parsePieceChoice(String xy) throws IOException {
        System.out.println("You can promote your pawn at " + xy + "Enter your selection");
        String pChoice = "";
        try {
            pChoice = br.readLine();
        }
        catch(InputMismatchException e){
            Errors.inputError();
            parsePieceChoice(xy); //try again
        }
        
        return pChoice.toLowerCase();
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