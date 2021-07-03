/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author JJThodberg
 */
public class BowlingMainTest {
    
    public BowlingMainTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
    
    @BeforeAll
    public static void setUpClass2() {
    }
    
    @AfterAll
    public static void tearDownClass2() {
    }
    
    @BeforeEach
    public void setUp2() {
    }
    
    @AfterEach
    public void tearDown2() {
    }


    /**
     * Test af result mod resultater fra web
     */
    @org.junit.jupiter.api.Test
    public void testWebTest() {
        System.out.println("webTest");
        BowlingMain instance = new BowlingMain();
        boolean result = true;
        for(int i =0;i<25;i++) {
            result = instance.webTest();
            if(!result) break;
        }
        assertEquals(result,true);
    }

    /**
     * Test af fuldt spil med maxscore
     */
    @org.junit.jupiter.api.Test
    public void testFuldtSpilMaxScore() {
        System.out.println("fuldtSpilMaxScore");
        int[][] result = {{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,10}};
        BowlingMain bowlingmain = new BowlingMain();
        int[] score = bowlingmain.bowlingScore(result);
        assertEquals(300, score[score.length - 1]);
    }

    /**
     * Test af fuldt spil med med spare til sidst
     */
    @org.junit.jupiter.api.Test
    public void testFuldtSpilSpareTilSidst() {
        System.out.println("FuldtSpilSpareTilSidst");
        int[][] result = {{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{5,5},{10,0}};
        BowlingMain bowlingmain = new BowlingMain();
        int[] score = bowlingmain.bowlingScore(result);
        assertEquals(275, score[score.length - 1]);
    }

    /**
     * Test af fuldt spil uden bonusslag
     */
    @org.junit.jupiter.api.Test
    public void testFuldtSpilUdenBonusSlag() {
        System.out.println("FuldtSpilUdenBonusSlag");
        int[][] result = {{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{4,5}};
        BowlingMain bowlingmain = new BowlingMain();
        int[] score = bowlingmain.bowlingScore(result);
        assertEquals(262, score[score.length - 1]);
    }
    
    int[][] result = {{10,0},{5,5},{4,5},{10,0},{2,6},{3,7},{10,0},{10,0},{10,0}};

    /**
     * Test af ikke fuldt spil med 3 strikes til sidst
     */
    @org.junit.jupiter.api.Test
    public void testIkkeFuldtSpilTreStrikesTilSidst() {
        System.out.println("IkkeFuldtSpilTreStrikesTilSidst");
        int[][] result = {{10,0},{5,5},{4,5},{10,0},{2,6},{3,7},{10,0},{10,0},{10,0}};
        BowlingMain bowlingmain = new BowlingMain();
        int[] score = bowlingmain.bowlingScore(result);
        assertEquals(149, score[score.length - 1]);
        assertEquals(139, score[score.length - 2]);
        assertEquals(119, score[score.length - 3]);
    }

    /**
     * Test af ikke fuldt spil med 3 spare til sidst
     */
    @org.junit.jupiter.api.Test
    public void testIkkeFuldtSpilTreSpareTilSidst() {
        System.out.println("IkkeFuldtSpilTreSpareTilSidst");
        int[][] result = {{10,0},{5,5},{4,5},{10,0},{2,6},{2,7},{3,7},{9,1},{8,2}};
        BowlingMain bowlingmain = new BowlingMain();
        int[] score = bowlingmain.bowlingScore(result);
        assertEquals(125, score[score.length - 1]);
        assertEquals(115, score[score.length - 2]);
        assertEquals(97, score[score.length - 3]);
    }

    /**
     * Test af ikke fuldt spil med spare efter strike
     */
    @org.junit.jupiter.api.Test
    public void testIkkeFuldtSpilSpareEfterStrike() {
        System.out.println("IkkeFuldtSpilTreSpareTilSidst");
        int[][] result = {{10,0},{5,5},{4,5},{10,0},{2,6}};
        BowlingMain bowlingmain = new BowlingMain();
        int[] score = bowlingmain.bowlingScore(result);
        assertEquals(20, score[0]);
        assertEquals(34, score[1]);
        assertEquals(43, score[2]);
    }
    
}
