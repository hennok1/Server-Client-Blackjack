package black.jack.game;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Hennok
 * @version 1.0
 */
public class Server {
    
    /**
     *The Server Class will create a ServerSocket and will wait for a connection from a client and 
     * if an issue happens when creating the ServerSocket, an exception will be thrown .
     * If a connection is made, an input and output stream from the socket will be made
     * to allow communication between the Server and Client.
     * 
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        System.out.println("Waiting for Connection...");
        
        try{
            Deck deck = new Deck();
            List<Card> hand =new ArrayList<>();
            int sum=0;
            
            ServerSocket socket;
            socket = new ServerSocket(40090);
            Socket sock = socket.accept( );
            System.out.println("Connection Recived From Client!");
            
            ObjectOutputStream outToClient=new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream inFromClient=new ObjectInputStream(sock.getInputStream());
            
            String cmd;
            while(true){
                cmd = (String)inFromClient.readObject();//Read a string from client
                
                /**
                 * If deal is received from client, the server will
                 * send the client a card from the deck.
                 */
                if(cmd.matches("deal")){
                    System.out.println("Sending a new card");
                    outToClient.writeObject(deck.cardList.get(Deck.index));
                    Deck.index++;
                    System.out.println(deck.cardList.get(Deck.index).getClass());
                    outToClient.flush();
                }
                
                /**
                 * If stay is received from client, the server will calculate the 
                 * sum of all the cards in the clients hand and send the sum value
                 * of the hand.
                 */
                if(cmd.matches("stay")){
                    hand = (List<Card>)inFromClient.readObject();
                    System.out.println("Reciving hand from Client");
                    for (int i =0; i < hand.size();i++) {
                        sum = sum + hand.get(i).getValue();
                    }
                    System.out.println("Total Value: "+sum);
                    outToClient.writeObject(sum);
                    hand.clear();
                    sum=0;
                }
                
                if(Deck.index==52){
                    System.out.println("Error! no more cards, restart server");
                    break;  
                }
                
            }
            
            socket.close();
        }
        catch(IOException e){
        }
        
    }
    
}
