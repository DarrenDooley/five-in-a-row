package com.genesys.darrendooley;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    private Grid grid; // Don't create an instance here

    @BeforeEach
    public void setUp() {
        grid = new Grid();
    }

    @AfterEach
    public void tearDown() {
        grid = null;
    }

    @Test
    public void testGetStateDuringGame() {
        //Grid grid = new Grid();
        String gridString = "[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]\n";
        grid.addPlayer("john");
        String turnString = grid.getTurn("john");
        String expected = gridString + turnString;
        assertFalse(grid.isGameOver());
        assertEquals(expected, grid.getState("john"));
    }

    @Test
    public void testGetStateIfGameWon() {
        //Grid grid = new Grid();
        String gridString = "[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[x][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[x][o][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[x][o][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[x][o][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[x][o][ ][ ][ ][ ][ ][ ][ ]\n";
        grid.addPlayer("john");
        grid.addPlayer("jane");
        for (int i = 0; i < 5; i++) {
            grid.dropDisc("john", 1);
            grid.dropDisc("jane", 2);
        }
        String winString = "\n'john' has won the game!";
        String expected = gridString + winString;
        assertTrue(grid.isGameOver());
        assertEquals(expected, grid.getState("john"));
    }

    @Test
    public void testGetStateIfPlayerLeaves() {
        //Grid grid = new Grid();
        String gridString = "[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]"
                + "\n[ ][ ][ ][ ][ ][ ][ ][ ][ ]\n";
        grid.addPlayer("john");
        grid.addPlayer("jane");
        grid.removePlayer("jane");
        String playerLeftString = "\nThe other player has left the game. You win!";
        String expected = gridString + playerLeftString;
        assertEquals(expected, grid.getState("john"));
    }

    @Test
    void testHasAnyPlayers() {
        //Grid grid = new Grid();
        grid.addPlayer("john");
        grid.addPlayer("jane");
        assertTrue(grid.hasAnyPlayers());
    }

    @Test
    void testDoesPlayerExistIfTrue() {
        //Grid grid = new Grid();
        grid.addPlayer("john");
        assertTrue(grid.doesPlayerExist("john"));
    }

    @Test
    void testDoesPlayerExistIfFalse() {
        //Grid grid = new Grid();
        grid.addPlayer("john");
        grid.removePlayer("john");
        assertFalse(grid.doesPlayerExist("john"));
    }

    @Test
    void testHasTwoPlayersIfTrue() {
        //Grid grid = new Grid();
        grid.addPlayer("john");
        grid.addPlayer("jane");
        assertTrue(grid.hasTwoPlayers());
    }

    @Test
    void testHasTwoPlayersIfFalse() {
        //Grid grid = new Grid();
        grid.addPlayer("john");
        assertFalse(grid.hasTwoPlayers());
    }

}