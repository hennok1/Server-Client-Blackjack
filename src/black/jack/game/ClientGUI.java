package black.jack.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This Client class uses a GUI with one JTextField indicating where is client is connecting to, one JTextArea
 * to show the user the results of the option they have selected, and three buttons with the options deal (pick up one/another card),
 * show (show the cards in the users hand), and stay(show total value of hand and reset value).
 * @author Hennok
 * @version 1.0
 */
public class ClientGUI extends JFrame{
    
    JTextField textfield1 = new JTextField(10);
    JTextArea textfield2 = new JTextArea();
    
    JButton dealButton = new JButton("Deal");
    JButton showButton = new JButton("Show");
    JButton stayButton = new JButton("Stay");
    
    Socket sock;
    ObjectOutputStream outToServer;
    ObjectInputStream inFromServer;
    Scanner ip=new Scanner(System.in);
    List<Card> hand =new ArrayList<>();
    int handIndex=0;
    
    /**
     * First attempts to make a connection to the Server.
     * Once connection is made, the method adds the panels that use the 
     * JTextField, JTextArea, and three buttons to the GUI.
     * @throws IOException
     */
    public ClientGUI() throws IOException{
        this.sock = new Socket("localhost",40090); // This is used to connect to the server using an IP and the port number
        this.outToServer = new ObjectOutputStream(sock.getOutputStream());
        this.inFromServer = new ObjectInputStream(sock.getInputStream());
            InetAddress inetAddress = sock.getInetAddress();
            //Panel 1
            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            //Add contents to the panel1
            panel1.add(new JLabel("<html>Message From:  "+inetAddress.getHostAddress()+":40090<br>Black Jack:</html>"));
        
            //Panel 2
            JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            //Add contents to panel2
            textfield2.setPreferredSize(new Dimension(250,150));
            panel2.add(textfield2);
        
            //Panel3
            JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            //Add contents to panel3
            panel3.add(dealButton);
            panel3.add(showButton);
            panel3.add(stayButton);
        
            add(panel1, BorderLayout.NORTH);
            add(panel2, BorderLayout.CENTER);
            add(panel3, BorderLayout.SOUTH);
        
            dealButton.addActionListener(new deal());
            showButton.addActionListener(new show());
            stayButton.addActionListener(new stay());
        
        
    }
    
    /**
     * Receives card from the server and if the card is an ace it will ask the user if 
     * the card value should be 1 or 11.
     */
    class deal implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                    outToServer.writeObject("deal");
                    Card c=(Card) inFromServer.readObject();
                    hand.add(c);
                    if(c.rank.equals("Ace")){
                        int value = Integer.parseInt(JOptionPane.showInputDialog("Should Aces be 1 or 11?:"));
                        hand.get(handIndex).setValue(value);
                    }
                    textfield2.setText(c.ToString());
                    System.out.println(c.ToString());
            }
            catch(IOException | ClassNotFoundException x){
                
            }
        }
        
    }
    
    /**
     * Will display the users hand with all the cards received from the server
     */
    class show implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println("Cards in hand:");
            textfield2.setText("Cards in hand:\n");
            textfield2.append("------------------");
            for (Card hand1 : hand) {
                System.out.println(hand1.ToString());
                
                textfield2.append("\n"+hand1.ToString());
            }
            System.out.println("------------------");
                
        }
    }
    
    /**
     * Receives the sum of all the cards in the users hand from the server and 
     * clear the users hand.
     */
    class stay implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
            try{
                outToServer.writeObject("stay");
                outToServer.writeObject(hand);
                int sum =(int)inFromServer.readObject();
                System.out.println("Total Value: " + sum);
                textfield2.setText("Total Value: " + sum);
                hand.clear();
            }
        
            catch (IOException | ClassNotFoundException ex) {
            }
        }
    }
    
    public static void main(String[] args) throws IOException{
        JFrame frame1 = new ClientGUI();
        frame1.setTitle("BlackJack Client"); //Title of the frame
        frame1.setSize(600,400);// frame size
        frame1.setLocationRelativeTo(null);//center the frame
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
    }
    
}
