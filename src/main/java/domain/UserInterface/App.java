package domain.UserInterface;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import domain.Logic.Game;

/**
 * driver code
 */
public class App {

    public static void main(String... strings) throws IOException
    {
        long startTime = System.nanoTime();

        Game game = Game.getGame();
        
        UI ui = new UI(game, new BufferedReader(new InputStreamReader(System.in)));
        ui.showGreeting();
        ui.initGame();
        //ui.getMoves();

        long endTime = System.nanoTime();

        long durationNano = endTime - startTime;
        long durationSec = TimeUnit.NANOSECONDS.toSeconds(durationNano);
        
        System.out.println("Game lasted " + durationSec + " seconds");
    }
}
