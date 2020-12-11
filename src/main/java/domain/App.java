package domain;

import java.util.Scanner;

/**
 * Hello world!
 * to do add ui and input
 */
public class App 
{
    public static void main( String[] args )
    {
        Game game = new Game();

        UI ui = new UI(game, new Scanner(System.in));
        ui.showGreeting();
        
    }
}
