package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import domain.Logic.Game;
import domain.Logic.Color.ColorType;
import domain.UserInterface.*;

/**
 * Uses System Rules dependency
 */
public class AppTest {
    private static Game g;
    private static UI input;

    /**
     * Rigorous Test :-)
     */
    @Rule
    // public final TextFromStandardInputStream systemInMock =
    // emptyStandardInputStream();
    @BeforeClass
    public static void setUp() {
        g = Game.getGame();

    }

    private TextFromStandardInputStream emptyStandardInputStream() {
        return null;
    }

    @Test
    public void shouldTakeUserInput() throws FileNotFoundException {
        input = new UI(g, new FileReader(new File("text.txt")));
        String expectedOutput = "array";
        
    }
    @Test
    public void getTurn(){
        assertEquals(ColorType.White, g.getTurn());
    }
    
  /*  @Test
    public void checkPieceList(){
        assertThat(g.getMoves().isEmpty());
    } */

    
}
