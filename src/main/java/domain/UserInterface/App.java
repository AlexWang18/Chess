package domain.UserInterface;

import java.util.Scanner;

import domain.Logic.Game;

/**
 * Hello world!
 * to do add ui and input
 */
public class App 
{
    public static void main(String...strings)
    {
        Game game = Game.getGame();
        
        UI ui = new UI(game, new Scanner(System.in));
        ui.showGreeting();
        
    }
}
