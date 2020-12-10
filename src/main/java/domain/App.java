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
        Scanner sc = new Scanner(System.in);
        Game game = new Game();
        UI ui = new UI(game, sc);
        ui.showGreeting();


        sc.close();
    }
}
