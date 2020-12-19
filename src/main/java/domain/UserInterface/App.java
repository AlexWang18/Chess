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
        
        
        Runnable ui = new UI(Game.getGame(), new BufferedReader(new InputStreamReader(System.in)));
        Thread t1 = new Thread(ui, "Chess");

        long startTime = System.nanoTime();

        t1.start();
        while(t1.isAlive()); //to avoid concurrency issues

        long endTime = System.nanoTime();

        long durationNano = endTime - startTime;
        long durationSec = TimeUnit.NANOSECONDS.toSeconds(durationNano);
        
        System.out.println("Game lasted " + durationSec + " seconds");
    }
}
