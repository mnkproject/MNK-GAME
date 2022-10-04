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

/**
 * Totally random software player.
 */
public class Player implements MNKPlayer {
    private Random rand;
    private int TIMEOUT;

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
    }

    // AlphaBeta algorithm
    public int AlphaBeta(MNKCell[] FC, MNKCell[] MC, boolean isMaximizingPlayer, int alpha, int beta) {
        if (FC.length == 0) {
            return 0;
        }

        if (isMaximizingPlayer) {
            int eval = Integer.MIN_VALUE;
            for (MNKCell cell : FC) {
                /*
                 * MNKCell[] newFC = new MNKCell[FC.length - 1];
                 * MNKCell[] newMC = new MNKCell[MC.length + 1];
                 * int i = 0;
                 * int j = 0;
                 * for (MNKCell c : FC) {
                 * if (c != cell) {
                 * newFC[i] = c;
                 * i++;
                 * }
                 * }
                 * for (MNKCell c : MC) {
                 * newMC[j] = c;
                 * j++;
                 * }
                 * newMC[j] = cell;
                 * int value = AlphaBeta(newFC, newMC, false, alpha, beta);
                 * bestValue = Math.max(bestValue, value);
                 * alpha = Math.max(alpha, bestValue);
                 * if (beta <= alpha) {
                 * break;
                 * }
                 */
                eval = Math.max(eval, AlphaBeta(FC, MC, false, alpha, beta));
                alpha = Math.max(eval, alpha);
                if (beta <= alpha) {
                    break;
                }
            }
            return eval;
        } else {
            int eval = Integer.MAX_VALUE;
            for (MNKCell cell : MC) {
                /*
                 * MNKCell[] newFC = new MNKCell[FC.length + 1];
                 * MNKCell[] newMC = new MNKCell[MC.length - 1];
                 * int i = 0;
                 * int j = 0;
                 * for (MNKCell c : FC) {
                 * newFC[i] = c;
                 * i++;
                 * }
                 * for (MNKCell c : MC) {
                 * if (c != cell) {
                 * newMC[j] = c;
                 * j++;
                 * }
                 * }
                 * newFC[i] = cell;
                 * int value = AlphaBeta(newFC, newMC, true, alpha, beta);
                 * bestValue = Math.min(bestValue, value);
                 * beta = Math.min(beta, bestValue);
                 * if (beta <= alpha) {
                 * break;
                 * }
                 */
                eval = Math.min(eval, AlphaBeta(FC, MC, true, alpha, beta));
                alpha = Math.min(eval, alpha);
                if (beta <= alpha) {
                    break;
                }
            }
            return eval;
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

        // Assign value to each cell
        HashMap<MNKCell, Integer> cellValues = new HashMap<MNKCell, Integer>();
        for (MNKCell cell : FC) {
            cellValues.put(cell, AlphaBeta(FC, MC, true, Integer.MIN_VALUE, Integer.MAX_VALUE));
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

        return bestCell;

        // return FC[rand.nextInt(FC.length)];

    }

    public String playerName() {
        return "AlphaBeta";
    }

}