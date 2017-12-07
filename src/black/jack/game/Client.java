package black.jack.game;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *This Client Class is a none GUI version of the client that uses the command line options to 
 * deal, stay, and show hand when playing black jack. There is also an Option to quit the application
 * when the user is done with the game.
 * @author Hennok
 * @version 1.0
 */
public class Client{
    
    /**
     *Attempts to make a connection to the Server and once connection is made a
     * menu is displayed to the user with three option: Deal, Stay, Hand, and 
     * Quit.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        Scanner scan=new Scanner(System.in);
        List<Card> hand =new ArrayList<>();
        int handIndex=0;
        
        try{
            Socket sock = new Socket("localhost",40090);
            
            ObjectOutputStream outToServer=new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream inFromServer=new ObjectInputStream(sock.getInputStream());
            
            System.out.println("Hennok Tadesse");
            System.out.println("Commands For Black Jack");
            System.out.println("--------------------------------");
            System.out.println("deal to Recive a card from Server");
            System.out.println("stay to stay with current card(s)");
            System.out.println("hand to show your current card(s)");
            System.out.println("quit to close program");
            System.out.println("--------------------------------");
            String cmd;
            
            while(true){
                System.out.print("Enter a command: ");
                cmd = scan.next();
               /**
                * Receives card from the server and if the card is an ace it will 
                * ask the user if the card value should be 1 or 11.
                */
                if(cmd.matches("deal")){
                    outToServer.writeObject(cmd);
                    Card c=(Card) inFromServer.readObject();
                    hand.add(c);
                    if(c.rank.equals("Ace")){
                        System.out.print("Should Aces be 1 or 11?: ");
                        hand.get(handIndex).setValue(scan.nextInt());
                    }
                    System.out.println(c.ToString());
                    handIndex++;
                }
                
                /**
                * Receives the sum of all the cards in the users hand from the server and 
                * clear the users hand.
                */
                if(cmd.matches("stay")){
                    outToServer.writeObject(cmd);
                    outToServer.reset();
                    outToServer.writeObject(hand);
                    int sum = (int)inFromServer.readObject();
                    System.out.println("Total Value: " + sum);
                    sum=0;
                    hand.clear();
                }
                
                /**
                * Will display the users hand with all the cards received from the server
                */
                if(cmd.matches("hand")){
                    System.out.println("------------------");
                    System.out.println("Cards in hand:");
                    for (Card hand1 : hand) {
                        System.out.println(hand1.ToString());
                    }
                    System.out.println("------------------");
                }
                
                /**
                 * Quits program and closes Server
                 */
                if (cmd.matches("quit")){
                    break;
                }
                
                outToServer.flush();
            }
        }
        catch(IOException | ClassNotFoundException e){
        }
    }
    
}
