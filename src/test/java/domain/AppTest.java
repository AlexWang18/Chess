package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import domain.Color.ColorType;

/**
 * Uses System Rules dependency
 */
public class AppTest
{
    private static Game g;
    private static UI input;
    
    /**
     * Rigorous Test :-)
     */
    @Rule
    //public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();
    @BeforeClass
    public static void setUp(){
        g = Game.getGame();
        
    }
    
    private TextFromStandardInputStream emptyStandardInputStream() {
        return null;
    }

    @Test
    public void shouldTakeUserInput(){
        
        InputStream in = new ByteArrayInputStream("h7, h5".getBytes());
        input = new UI(g, new Scanner(in));
        assertEquals("h7, h5", System.console() );
    }
    @Test
    public void getTurn(){
        assertEquals(ColorType.White, g.getTurn());
    }
    
  /*  @Test
    public void checkPieceList(){
        assertThat(g.getMoves().isEmpty());
    } */

    @Test
    public void testMoves(){
        assertFalse(g.executeMove(7, 7, 5, 7)); //jumping

        assertFalse(g.executeMove(1, 1, 3, 1)); //wrong turn

        assertTrue(g.executeMove(6,6, 4,6));
    }
    public void testGameOver(){
        assertTrue(g.notDone());
        g.over = true;
        assertFalse(g.notDone());
    }
}
