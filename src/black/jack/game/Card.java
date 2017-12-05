package black.jack.game;

import java.io.Serializable;

/**
 *The Card Object Class has three properties that define the Card Object:
 * rank (Ace, Jack, Queen, King, etc.), and suit (Spades, Hearts, Diamonds, and Clubs).
 * @author Hennok
 * @version 1.0
 */
public class Card implements Serializable{
    String suit,rank;
    private int value;  
 
    /**
     *Setting the Card's suit and rank when creating a card.
     * @param r
     * @param s
     */
    public Card(String r,String s){
        suit=s;
        rank=r;
    }
 
    /**
     * Sets the integer value of playing Card.
     * @param val
     */
    public void setValue(int val){
        value=val;
    }
 
    /**
     *Returns the Integer value of playing Card.
     * @return Integer value of a playing Card.
     */
    public int getValue(){
        return value;
    } 
 
    /**
     * String representation of the playing Card.
     * @return String representation of the playing Card.
     */
    public String ToString(){
        return rank+" of "+suit;
    }
}

