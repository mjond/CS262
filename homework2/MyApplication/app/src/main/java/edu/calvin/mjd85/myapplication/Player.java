package edu.calvin.mjd85.myapplication;

/**
 * Created by Mark Davis on 10/20/2016.
 */

/**
 * Player object contains data about monopoly players
 * Player class holds data about each player
 *
 * @author mjd85 Mark Davis
 * @version spring, 2017
 */
public class Player {

    private String playerText;

    public Player(String playerText){

        this.playerText = playerText;
    }

    public String getPlayerText() {
        return playerText;
    }
}
