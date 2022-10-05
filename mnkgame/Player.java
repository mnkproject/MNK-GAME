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
    private int M, N, K;
    private boolean first;
    private int timeout_in_secs;

    /**
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
    }

    // AlphaBeta algorithm
    public int AlphaBeta(MNKCell[] FC, MNKCell[] MC, boolean isMaximizingPlayer, int depth, int alpha, int beta) {
        int eval = 0;
        // if marked cells are equal to 0 then randomly choose a cell

        return eval;
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
        /*  if (MC.length == 0) {
            // Place the first move in the center of the board
            System.out.println("First move: " + FC.length / 2);
            return FC[FC.length / 2];
        }*/

        //Order FC by FC[i].i
        //**TODO^^

        // Assign value to each cell
        HashMap<MNKCell, Integer> cellValues = new HashMap<MNKCell, Integer>();
        for (MNKCell cell : FC) {
            // cellValues.put(cell, AlphaBeta(FC, MC, false, 10, Integer.MIN_VALUE,
            // Integer.MAX_VALUE));
            cellValues.put(cell, eval(cell, FC, MC));
        }

        // Select cell with highest value
        MNKCell bestCell = null;
        int bestValue = Integer.MIN_VALUE;
        for (MNKCell cell : FC) {
            if (cellValues.get(cell) > bestValue) {
                bestCell = cell;
                bestValue = cellValues.get(cell);
            }

            // print cell values
             System.out.println("Cell: " + cell + " Value: " + cellValues.get(cell));
        }

        System.out.println("Selected cell: " + bestCell);
        return bestCell;

        // return FC[rand.nextInt(FC.length)];

    }

    public String playerName() {
        return "AlphaBeta";
    }

}