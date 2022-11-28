/*
 *  Copyright (C) 2021 Pietro Di Lena
 *  
 *  This file is part of the MNKGame v2.0 software developed for the
 *  students of the course "Algoritmi e Strutture di Dati" first 
 *  cycle degree/bachelor in Computer Science, University of Bologna
 *  A.Y. 2020-2021.
 *
 *  MNKGame is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This  is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this file.  If not, see <https://www.gnu.org/licenses/>.
 */

package mnkgame;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

/**
 * Totally random software player.
 */
public class MyPlayer implements MNKPlayer {
    private Random rand;
    private int TIMEOUT;
    private int M, N, K, minNM;
    private boolean first;
    private int timeout_in_secs;
    private Board myBoard;

    // Terminal colors
    public final String ANSI_RESET = "\u001B[0m";
    public final String ANSI_BLACK = "\u001B[30m";
    public final String ANSI_RED = "\u001B[31m";
    public final String ANSI_GREEN = "\u001B[32m";
    public final String ANSI_YELLOW = "\u001B[33m";
    public final String ANSI_BLUE = "\u001B[34m";
    public final String ANSI_PURPLE = "\u001B[35m";
    public final String ANSI_CYAN = "\u001B[36m";
    public final String ANSI_WHITE = "\u001B[37m";
    public final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // Board
    private class Board extends MNKBoard {
        public Board(int M, int N, int K) {
            super(M, N, K);
        }

        // get cell state at position (i,j)
        public MNKCellState getCellState(int i, int j) {
            return super.B[i][j];
        }

        // set cell state
        public void setCellState(int i, int j, MNKCellState state) {
            super.B[i][j] = state;
        }

        private void updateMarkedCells(MNKCell newMarkedCell) {
            myBoard.markCell(newMarkedCell.i, newMarkedCell.j);

        }

    }

    // tells if the marked cell is a winning move
    public boolean isWinningMove(MNKCell cell, MNKGameState winningPlayer) {
        MNKGameState state = myBoard.markCell(cell.i, cell.j);
        // System.out.println("Checking if " + cell + " is a winning move...");
        // System.out.println("State: " + state);
        // System.out.println("Size after marking: " + myBoard.getMarkedCells().length);
        myBoard.unmarkCell();
        // System.out.println("Size after unmarking: " +
        // myBoard.getMarkedCells().length);

        return winningPlayer == state;
    }

    /*
     * Default empty constructor
     */
    public MyPlayer() {
    }

    public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
        // New random seed for each game
        rand = new Random(System.currentTimeMillis());
        // Save the timeout for testing purposes
        TIMEOUT = timeout_in_secs;

        // Uncomment to chech the initialization timeout
        /*
         * try {
         * Thread.sleep(1000*2*TIMEOUT);
         * }
         * catch(Exception e) {
         * }
         */
        this.M = M;
        this.N = N;
        this.K = K;
        this.first = first;
        this.timeout_in_secs = timeout_in_secs;

        this.myBoard = new Board(M, N, K);
    }

    private void printCell(MNKCell cell) {
        System.out.println("Cell: (" + cell.i + ", " + cell.j + ", " + cell.state + ")");
    }

    private void printCell(MNKCell cell, int value) {
        // System.out.println("Cell: (" + cell.i + ", " + cell.j + ", " + cell.state +
        // "), value: " + value);

        // let's simulate the board
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (i == cell.i && j == cell.j) {
                    System.out.print("[" + value + "]");
                } else {
                    if (myBoard.getCellState(i, j) == MNKCellState.FREE) {
                        System.out.print("[ ]");
                    } else if (myBoard.getCellState(i, j) == MNKCellState.P1) {
                        System.out.print("[X]");
                    } else {
                        System.out.print("[O]");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printBoardValues(HashMap<MNKCell, Integer> boardValues, MNKCell[] MC) {

        // print board with values
        // store values in a matrix
        int[][] values = new int[M][N];
        for (MNKCell cell : boardValues.keySet()) {
            values[cell.i][cell.j] = boardValues.get(cell);
        }

        // let's simulate the board
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                boolean markedCell = false;

                // print marked cells
                for (MNKCell cell : MC) {
                    if (cell.i == i && cell.j == j) {
                        markedCell = true;
                        if (cell.state == MNKCellState.P1) {
                            System.out.print("[ " + ANSI_RED + "X" + ANSI_RESET + " ]");
                        } else {
                            System.out.print("[ " + ANSI_BLUE + "O" + ANSI_RESET + " ]");
                        }
                    }
                }

                // print free cells
                // System.out.print("[" + values[i][j] + "]");
                // justify the values
                if (!markedCell) {
                    if (values[i][j] < 10) {
                        System.out.print("[ " + values[i][j] + " ]");
                    } else if (values[i][j] < 100) {
                        System.out.print("[ " + values[i][j] + "]");
                    } else {
                        System.out.print("[" + values[i][j] + "]");
                    }
                }

            }
            System.out.println();
        }
        System.out.println();

    }

    // Evaluate the board
    private HashMap<MNKCell, Integer> evaluateBoard(MNKCell[] FC, MNKCell[] MC) {
        final int MAX_VALUE = 100;
        final int MIN_VALUE = 0;
        // cell values are in this range (int)[0, 100].
        // 0 means that the cell is not interesting at all
        // 100 means that the cell is a winning move or stops the opponent from winning
        // values in between are used to assign a value to its cell

        /*
         * // print cells
         * for (MNKCell cell : MC) {
         * printCell(cell);
         * }
         * for (MNKCell cell : FC) {
         * printCell(cell);
         * }
         */

        HashMap<MNKCell, Integer> freeCellValues = new HashMap<MNKCell, Integer>();

        // initialize freeCellValues with FC to 0
        for (MNKCell cell : FC) {
            freeCellValues.put(cell, 0);
        }

        /*
         * System.out.println("************** CELLE APPENA INIZIALIZZATE **************"
         * );
         * freeCellValues.forEach((cell, value) -> {
         * printCell(cell, value);
         * });
         */

        // printBoardValues(freeCellValues, MC);

        /*
         * System.out.println("_____________DEBUG_____________");
         * for (MNKCell cell : MC) {
         * printCell(cell);
         * }
         */

        /*
         * CHECK FIRST MOVE ************************************************
         */
        // check if it is first move in the game
        if (MC.length == 0) {
            // if it is first move, choose a cell in the middle of the board
            int i = (int) Math.floor(M / 2);
            int j = (int) Math.floor(N / 2);
            MNKCell bestCell = new MNKCell(i, j, MNKCellState.FREE); // we know it is free because this is the very
                                                                     // first move
            freeCellValues.put(bestCell, MAX_VALUE - 1);// MAX_VALUE-1 because it is not the winning move
        }

        /*
         * CHECK WINNING MOVES ************************************************
         */
        // Check if there are cells that are winning, or draw moves
        // If there are, assign them the maximum value
        for (MNKCell cell : freeCellValues.keySet()) {
            // FIXME: a volte non trova la cella vincente (per ora solo riscontrato per l'opponent, ma controlla bene)

            // check my winning moves
            if (isWinningMove(cell, MNKGameState.WINP1)) {// check if it is a winning move for P1 (X), my player
                System.out.println("************** CELLA VINCENTE P1 **************");
                printCell(cell, MAX_VALUE);
                freeCellValues.put(cell, MAX_VALUE); // assign the maximum value to the winning move

                printBoardValues(freeCellValues, MC);
                return freeCellValues; // return the board values, because we found a winning move
            }

            // check opponent winning moves (we need to stop them)
            if (isWinningMove(cell, MNKGameState.WINP2)) {// check if it is a winning move for P2 (O), the opponent
                System.out.println("************** CELLA VINCENTE PER L'AVVERSARIO P2 **************");
                printCell(cell, MAX_VALUE);
                freeCellValues.put(cell, MAX_VALUE); // assign the maximum value to the winning move

                printBoardValues(freeCellValues, MC);
                return freeCellValues; // return the board values, because we found a winning move for the opponent
            }
        }

        /*
         * MANHATTAN ************************************************
         */
        // now we need to assign a value to each free cell
        // we will use the Manhattan distance to the closest cell of the opponent
        // the closer the cell is, the higher the value
        // Manhattan formula is d = |x1-x2| + |y1-y2|

        /*
         * for (MNKCell freeCell : freeCellValues.keySet()) {
         * for (MNKCell markedCell : MC) {
         * int d = Math.abs(freeCell.i - markedCell.i) + Math.abs(freeCell.j -
         * markedCell.j);
         * int value = MAX_VALUE / (d + 10);
         * freeCellValues.put(freeCell, value);
         * }
         * }
         * 
         * System.out.println("************** MANHATTAN **************");
         */

        System.out.println("************** CELLE VALORE FINALE **************");
        printBoardValues(freeCellValues, MC);

        return freeCellValues;
    }

    /**
     * Selects the best cell in <code>FC</code>
     */
    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
        // Uncomment to check the move timeout
        /*
         * try {
         * Thread.sleep(1000*2*TIMEOUT);
         * }
         * catch(Exception e) {
         * }
         */

        // first of all we need to update OUR board with the new move
        // get new move, from MNKBoard.java we know that the new move is in the MC tail
        // (the last element)
        if (MC.length > 0) { // we do that only if it is not the first move
            myBoard.updateMarkedCells(MC[MC.length - 1]);
        }

        HashMap<MNKCell, Integer> evaluatedCells = evaluateBoard(FC, MC);

        MNKCell bestCell = null;

        // search for the best cell in evaluatedCells
        for (MNKCell cell : evaluatedCells.keySet()) {
            if (bestCell == null) { // first iteration
                bestCell = cell;
            } else {
                if (evaluatedCells.get(cell) > evaluatedCells.get(bestCell)) {// check if new cell has a higher value
                    bestCell = cell;
                }
            }
        }

        System.out.println("Best cell: " + bestCell.i + " " + bestCell.j + ", " + bestCell.state + "\n\n");

        // before returning the best cell, we need to update our board
        myBoard.updateMarkedCells(bestCell);

        // return the best cell
        return bestCell;
    }

    public String playerName() {
        return "Il mio player";
    }

}