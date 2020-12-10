package domain;

//single responsibility principle 
import java.util.Scanner;

public class UI {
    private Game game;
    private Scanner sc;
    public UI(Game game, Scanner sc) {
        this.game = game;
        this.sc = sc;
    }

    public void showGreeting() {
        System.out.println("Welcome to chess! Let's begin");
        game.startGame();
        getMoves();
        gameOver();
    }

    private void gameOver() {
        System.out.println("GG!");
    }

    private void getMoves() {
        
        while (game.notDone()) {
            System.out.print(game.getTurn()+"'s turn, ");
            System.out.println("enter the square you wish to move from.");
            String input1 = sc.nextLine();
            Pair startingPair = getPair(input1.toLowerCase());
            if(checkIfNull(startingPair)){
                System.out.println("Invalid square to move! Retry!");
                continue;
            }
            int startfile = startingPair.getX(); //2
            int startrank = startingPair.getY(); //6
            System.out.println("Enter the square you wish to move to.");
            String input2 = sc.nextLine();
            Pair endingPair = getPair(input2.toLowerCase());
            if(checkIfNull(endingPair)){
                System.out.println("Invalid square to move! Retry!");
                continue;
            }
            int endfile = endingPair.getX();  
            int endrank = endingPair.getY();

            game.executeMove(startfile, startrank, endfile, endrank); //x,y x,y
        }
    }

    private boolean checkIfNull(Pair obj){
        return (obj == null);
    }
    //method returns the pair for those cord from string
    private Pair getPair(String input){ 
        Pair p = new Pair(input);
        if(p.isPairValid()) return p;
        return p; //refactor
    }
}
