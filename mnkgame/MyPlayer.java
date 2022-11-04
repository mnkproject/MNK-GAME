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
        //System.out.println("Cell: (" + cell.i + ", " + cell.j + ", " + cell.state + "), value: " + value);

        //let's simulate the board
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (i == cell.i && j == cell.j) {
                    System.out.print("[" + value + "]");
                } else {
                    System.out.print("[" + myBoard.getCellState(i, j) + "]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Evaluate the board
    private HashMap<MNKCell, Integer> evaluateBoard(MNKCell[] FC, MNKCell[] MC) {
        // cell values are in this range (int)[0, 100].
        // 0 means that the cell is not interesting at all
        // 100 means that the cell is a winning move
        // values in between are used to assign a value to its cell

        // print cells
        for (MNKCell cell : MC) {
            // printCell(cell);
        }
        for (MNKCell cell : FC) {
            // printCell(cell);
        }

        HashMap<MNKCell, Integer> freeCellValues = new HashMap<MNKCell, Integer>();

        // initialize freeCellValues with FC to 0
        for (MNKCell cell : FC) {
            freeCellValues.put(cell, 0);
        }

        System.out.println("************** CELLE APPENA INIZIALIZZATE **************");
        freeCellValues.forEach((cell, value) -> {
            printCell(cell, value);
        });

        // check if it is first move in the game
        if (MC.length == 0) {
            // if it is first move, choose a cell in the middle of the board
            int i = (int) Math.floor(M / 2);
            int j = (int) Math.floor(N / 2);
            MNKCell bestCell = new MNKCell(i, j, MNKCellState.FREE); // we know it is free because this is the very
                                                                     // first move
            freeCellValues.put(bestCell, 99);
        }

        // now we need to assign a value to each free cell
        // we will use the Manhattan distance to the closest cell of the opponent
        // the closer the cell is, the higher the value
        // Manhattan formula is d = |x1-x2| + |y1-y2|
        for (MNKCell freeCell : freeCellValues.keySet()) {
            for (MNKCell markedCell : MC) {
                int d = Math.abs(freeCell.i - markedCell.i) + Math.abs(freeCell.j - markedCell.j);
                freeCellValues.put(freeCell, d);
            }
        }

        System.out.println("************** MANHATTAN **************");
        freeCellValues.forEach((cell, value) -> {
            printCell(cell, value);
        });

        return freeCellValues;
    }

    /**
     * Selects the best cell in <code>FC</code>
     */
    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
        // Uncomment to chech the move timeout
        /*
         * try {
         * Thread.sleep(1000*2*TIMEOUT);
         * }
         * catch(Exception e) {
         * }
         */

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

        return bestCell;
    }

    public String playerName() {
        return "Il mio player";
    }

}