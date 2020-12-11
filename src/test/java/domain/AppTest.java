package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import domain.Color.ColorType;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    private static Game g;
    /**
     * Rigorous Test :-)
     */
    @BeforeClass
    public static void setUp(){
        g = Game.getGame();
        g.startGame();
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
