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

    // Evaluate the board
    // TODO DA IMPLEMENTARE il fatto che devo vedere celle adiacenti. prima scelta è
    // al centro!
    private int evaluateBoard(MNKCell[] FC, MNKCell[] MC) {

        return 6969;

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

        MNKCell bestCell = null;

        /*
         * bestCell.i = rand.nextInt(M);
         * bestCell.j = rand.nextInt(N);
         * bestCell.state = first ? MNKCellState.P1 : MNKCellState.P2;
         */
        bestCell = FC[0];
        System.out.println(bestCell.i + " " + bestCell.j+", "+bestCell.state);

        return bestCell;
    }

    public String playerName() {
        return "Il mio player";
    }

    // region Algorithms for the game tree
    // AlphaBeta algorithm
    public int AlphaBeta(MNKCell[] FC, MNKCell[] MC, boolean isMaximizingPlayer, int alpha, int beta, int depth,
            int freeCells) {
        // System.out.println("AlphaBeta: depth = " + depth);
        if (depth == 0 || freeCells == 0) {
            // System.out.println("ORA ESCO\n");
            return evaluateBoard(FC, MC);// TODO evaluate(); DEVO FARE UNA FUNZIONE CHE VALUTA LA BOARD
        }

        if (isMaximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for (MNKCell cell : FC) {
                myBoard.setCellState(cell.i, cell.j, MNKCellState.P1); // mark cell as visited by P1
                int value = AlphaBeta(FC, MC, false, alpha, beta, depth - 1, freeCells - 1);
                myBoard.setCellState(cell.i, cell.j, MNKCellState.FREE); // unmark cell
                bestValue = Math.max(bestValue, value);
                if (beta <= alpha)
                    break;
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (MNKCell cell : FC) {
                myBoard.setCellState(cell.i, cell.j, MNKCellState.P2); // mark cell as visited by P2
                freeCells -= 1;
                int value = AlphaBeta(FC, MC, true, alpha, beta, depth - 1, freeCells - 1);
                myBoard.setCellState(cell.i, cell.j, MNKCellState.FREE); // unmark cell
                bestValue = Math.min(bestValue, value);
                if (beta <= alpha)
                    break;
            }
            return bestValue;
        }

    }

    // minimax algorithm
    public int miniMax(MNKCell[] FC, MNKCell[] MC, boolean isMaximizingPlayer, int depth) {

        /*
         * if (isMaximizingPlayer) {
         * System.out.println("\n\nMINIMAX - Massimizzo\n**********");
         * int bestScore = Integer.MIN_VALUE;
         * for (int i = 0; i < this.M; i++) {
         * for (int j = 0; j < this.N; j++) {
         * // check if the cell is empty
         * if (myBoard.B[i][j] == MNKCellState.FREE) {
         * myBoard.B[i][j] = MNKCellState.P1;// mark the cell as P1
         * System.out.println("MINIMAX - Marked cell (" + i + "," + j + ") as P1");
         * System.out.println("MINIMAX - Calling minimax with depth = " + (depth - 1));
         * int score = miniMax(FC, MC, false, depth - 1);
         * myBoard.B[i][j] = MNKCellState.FREE;// unmark the cell
         * bestScore = Math.max(score, bestScore);// get the best score
         * }
         * }
         * }
         * return bestScore;
         * } else {
         * System.out.println("\n\nMINIMAX - Minimizzo\n**********");
         * int bestScore = Integer.MAX_VALUE;
         * for (int i = 0; i < this.M; i++) {
         * for (int j = 0; j < this.N; j++) {
         * // check if the cell is empty
         * if (myBoard.B[i][j] == MNKCellState.FREE) {
         * myBoard.B[i][j] = MNKCellState.P2;// mark the cell as P2
         * System.out.println("MINIMAX - Marked cell (" + i + "," + j + ") as P2");
         * System.out.println("MINIMAX - Calling minimax with depth = " + (depth - 1));
         * int score = miniMax(FC, MC, true, depth - 1);
         * myBoard.B[i][j] = MNKCellState.FREE;// unmark the cell
         * bestScore = Math.min(score, bestScore);// get the best score
         * }
         * }
         * }
         * return bestScore;
         * }
         */
        return 69;
    }
}