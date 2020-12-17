package domain.UserInterface;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import domain.Logic.Game;

/**
 * driver code
 */
public class App {

    public static void main(String... strings) throws IOException
    {

        Game game = Game.getGame();
        
        UI ui = new UI(game, new BufferedReader(new InputStreamReader(System.in)));
        ui.showGreeting(); 
        ui.getMoves();
        
    }
}
