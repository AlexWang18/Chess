package domain;

import java.util.InputMismatchException;
import java.util.Scanner;

import domain.Pieces.Visitor;

public class UI {
    private Game game;
    private Scanner sc;
    // private Visitor<> visitor;

    public UI(Game game, Scanner sc) {
        this.game = game;
        this.sc = sc;
    }

    public void showGreeting() {
        System.out.println("Welcome to chess! Let's begin");
        game.startGame();

        getMoves();
        gameIsOver();
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

    private void getMoves() {
        while (game.notDone()) {
            try {

                System.out.print(game.getTurn() + "'s turn, ");

                System.out.println("enter the square you wish to move from.");
                String input1 = sc.nextLine();

                if (input1.isBlank())
                    continue;

                Pair startingPair = getPair(input1.toLowerCase());
                if (checkIfNull(startingPair)) {
                    System.out.println("Invalid square to move to at " + startingPair.getReadablePair() + " retry!");
                    continue;
                }

                int startfile = startingPair.getX();
                int startrank = startingPair.getY();

                System.out.println("Enter the square you wish to move to.");
                String input2 = sc.nextLine();

                if (input2.isBlank())
                    continue;

                Pair endingPair = getPair(input2.toLowerCase());

                if (checkIfNull(endingPair)) {
                    System.out.println("Invalid square to move to at " + endingPair.getReadablePair() + " retry!");
                    continue;
                }

                int endfile = endingPair.getX();
                int endrank = endingPair.getY();

                if (!game.executeMove(startfile, startrank, endfile, endrank))
                    tellError();
                /*
                 * add method parameter to specify ruleset?
                 */
            } catch (InputMismatchException exception) {
                System.out.println("Error occured in scanning users input   ");
            }
        }
    }

    private void tellError() {
        System.out.println("Move did not follow through, try again..");
    }

    private boolean checkIfNull(Pair obj) {
        return (obj == null);
    }

    // method returns the pair for those cordinates from user inputted string
    private Pair getPair(String input) {
        Pair p = new Pair(input);
        if (p.isPairValid())
            return p;
        return p; // refactor
    }
}
