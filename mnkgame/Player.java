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
    public int AlphaBeta(MNKCell[] FC, MNKCell[] MC, boolean isMaximizingPlayer, int depth, int alpha, int beta) {
        // If the game is over or the depth is 0, return the heuristic value
        if (depth == 0 || FC.length == 0) {
            // return Heuristic(FC, MC);
            return 0;
        }

        int bestValue = 0;

        // If it is the maximizing player
        if (isMaximizingPlayer) {
            // The best value is set to the minimum value
            bestValue = Integer.MIN_VALUE;
            // For each possible move
            for (MNKCell cell : FC) {
                // We make the move
                MNKCell[] newFC = new MNKCell[FC.length - 1];
                int i = 0;
                for (MNKCell c : FC) {
                    if (c != cell) {
                        newFC[i] = c;
                        i++;
                    }
                }
                MNKCell[] newMC = new MNKCell[MC.length + 1];
                i = 0;
                for (MNKCell c : MC) {
                    newMC[i] = c;
                    i++;
                }
                newMC[i] = cell;
                // We call AlphaBeta on the new state
                int value = AlphaBeta(newFC, newMC, false, depth - 1, alpha, beta);
                // We update the best value
                bestValue = Math.max(bestValue, value);
                // We update alpha
                alpha = Math.max(alpha, bestValue);
                // If beta is less than alpha, we prune the rest of the tree
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            // The best value is set to the minimum value
            bestValue = Integer.MIN_VALUE;
            // For each possible move
            for (MNKCell cell : FC) {
                // We make the move
                MNKCell[] newFC = new MNKCell[FC.length - 1];
                int i = 0;
                for (MNKCell c : FC) {
                    if (c != cell) {
                        newFC[i] = c;
                        i++;
                    }
                }
                MNKCell[] newMC = new MNKCell[MC.length + 1];
                i = 0;
                for (MNKCell c : MC) {
                    newMC[i] = c;
                    i++;
                }
                newMC[i] = cell;
                // We call AlphaBeta on the new state
                int value = AlphaBeta(newFC, newMC, true, depth - 1, alpha, beta);
                // We update the best value
                bestValue = Math.min(bestValue, value);
                // We update alpha
                alpha = Math.min(alpha, bestValue);
                // If beta is less than alpha, we prune the rest of the tree
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return bestValue;

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
            cellValues.put(cell, AlphaBeta(FC, MC, true, 10, Integer.MIN_VALUE, Integer.MAX_VALUE));
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