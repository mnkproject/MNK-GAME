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
public class Player implements MNKPlayer {
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
    public Player() {
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
    private int evaluateBoard(MNKCell[] FC, MNKCell[] MC) {
        // Check if the board is full
        if (FC.length == 0) {
            return 0;
        }

        // Check if the board is empty
        if (MC.length == 0) {
            return rand.nextInt(FC.length); // Randomly choose the first cell
            /*
             * FIXME:we can improve this by choosing the cell with the highest number of
             * possible
             * winning lines
             */
        }

        // Check if board has a winner and determine it
        if (FC.length == 1) {
            // there is a winner
            if (FC[0].state == MNKCellState.P1) {
                return Integer.MAX_VALUE; // P1 wins -> P2 loses //TODO: check if return is correct
            } else {
                return Integer.MIN_VALUE; // P2 wins -> P1 loses
            }
        }

        //Check rows
        /*int[] markedCells_row = new int[M];
        for(MNKCell cell: FC){
            //Check if there is at least one marked cell in the row
            
        }*/

        return 6969;

    }

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

    public int eval(MNKCell cell, MNKCell[] FC, MNKCell[] MC) {
        if (MC.length == 0) {
            return 0;
        }

        int evaluation = rand.nextInt(FC.length);

        return evaluation;

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

        // print FC
        /*
         * System.out.println("FC: ");
         * for (int i = 0; i < FC.length; i++) {
         * System.out.println(FC[i]);
         * }
         */

        // Check if it is the first move
        /*
         * if (MC.length == 0) {
         * // Place the first move in the center of the board
         * System.out.println("First move: " + FC.length / 2);
         * return FC[FC.length / 2];
         * }
         */

        // Order FC by FC[i].i
        // **TODO^^

        // Assign value to each cell
        /*
         * HashMap<MNKCell, Integer> cellValues = new HashMap<MNKCell, Integer>();
         * for (MNKCell cell : FC) {
         * // cellValues.put(cell, AlphaBeta(FC, MC, false, 10, Integer.MIN_VALUE,
         * // Integer.MAX_VALUE));
         * cellValues.put(cell, eval(cell, FC, MC));
         * }
         * 
         * // Select cell with highest value
         * MNKCell bestCell = null;
         * int bestValue = Integer.MIN_VALUE;
         * for (MNKCell cell : FC) {
         * if (cellValues.get(cell) > bestValue) {
         * bestCell = cell;
         * bestValue = cellValues.get(cell);
         * }
         * 
         * // print cell values
         * System.out.println("Cell: " + cell + " Value: " + cellValues.get(cell));
         * }
         */

        // FIXME: la board non è aggiornata con le mosse dei due giocatori

        // Update board with marked cells
        for (MNKCell cell : MC) {
            if (cell.state == MNKCellState.P1) {
                myBoard.B[cell.i][cell.j] = MNKCellState.P1;
            } else {
                myBoard.B[cell.i][cell.j] = MNKCellState.P2;
            }
        }

        System.out.println("\n\nM: " + myBoard.M + ", N: " + myBoard.N + ", K: " + myBoard.K);
        // print myBoard
        for (int i = 0; i < myBoard.M; i++) {
            for (int j = 0; j < myBoard.N; j++) {
                if (myBoard.B[i][j] == MNKCellState.FREE) {
                    System.out.print("0 ");
                } else if (myBoard.B[i][j] == MNKCellState.P1) {
                    System.out.print("1 ");
                } else {
                    System.out.print("2 ");
                }
            }
            System.out.println();
        }

        // if marked cells are equal to 0 then randomly choose a cell
        if (MC.length == 0) {
            return FC[rand.nextInt(FC.length)];
        }
        // else use minimax algorithm
        else {
            // create a hashmap to store the scores of each cell
            HashMap<MNKCell, Integer> scores = new HashMap<MNKCell, Integer>();
            // get the first cell
            MNKCell bestCell = FC[0];
            // get the first cell's score
            scores.put(bestCell, AlphaBeta(FC, MC, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 6, FC.length));
            // iterate through the rest of the cells
            for (MNKCell cell : FC) {
                /*
                 * // get the current score
                 * int currentScore = AlphaBeta(FC, MC, true, Integer.MAX_VALUE,
                 * Integer.MIN_VALUE, 6);
                 * // if the current score is greater than the previous score
                 * if (currentScore > scores.get(bestCell)) {
                 * // set the current cell as the best cell
                 * bestCell = cell;
                 * // update the score
                 * scores.replace(bestCell, currentScore);
                 * }
                 */
                scores.put(cell, AlphaBeta(FC, MC, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 6, FC.length));
            }

            // print scores
            for (MNKCell cell : FC) {
                System.out.println("Cell: " + cell + " Score: " + scores.get(cell));
            }

            // return the best cell
            return bestCell;

            // System.out.println("Selected cell: " + bestCell);
            // return bestCell;

            // return FC[rand.nextInt(FC.length)];

        }
    }

    public String playerName() {
        return "AlphaBeta";
    }

}