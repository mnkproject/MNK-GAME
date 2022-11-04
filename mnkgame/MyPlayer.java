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

    // Evaluate the board
    private void evaluateBoard(MNKCell[] FC, MNKCell[] MC) {
        // print cells
        for (MNKCell cell : MC) {
            printCell(cell);
        }
        for (MNKCell cell : FC) {
            printCell(cell);
        }
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

        evaluateBoard(FC, MC);

        MNKCell bestCell = null;
        bestCell = FC[0];
        System.out.println("Best cell: " + bestCell.i + " " + bestCell.j + ", " + bestCell.state+"\n\n");

        return bestCell;
    }

    public String playerName() {
        return "Il mio player";
    }

}