/*
 * Copyright (C) 2018 Nick Vocaire
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cchat.client;

import java.io.*;
import java.net.*;

/**
 * Simple Client for for a chatBox
 * @author Nick Vocaire
 * Date Created: 11/17/17
 */
public class ChatClient extends Thread {
    public static BufferedReader in; //Buffered Reader for taking input from the inputstream of the Socket,
                                     //(The Socket is what connects to the server)
                                     
    public static String hostName; //The IP Address of the machine running the Server
    public static String clientName; //Name the user gives to the client
    public static int portNumber; //The port for the server
    public static int clientNumber; //The postion of the client in the array on the Server
    public static final int DEFAULT_PORT = 22333; //Default Port
    public static final String DEFAULT_HOSTNAME = "0.0.0.0"; //Default Host Name
    public static final String DEFAULT_CLIENTNAME = "Jeff"; //Default Host Name
    
    /**
     * Simple Constructor for making Threads
     */
    public ChatClient () {}
    
    /**
     * Main method that is invoked when the program is started
     * @param args arguments that can be given when the program is run in a console
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        System.out.print("\u000C"); //Clear Screen
        
        BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in)); //Get input from Person
        
        System.out.println("What is the IP? (hit enter for default: " + DEFAULT_HOSTNAME + " )"); //Ask Question
        hostName = stdIn.readLine(); //Get input
        if(hostName.equals("")) //If enter, set host name to deafult
            hostName = DEFAULT_HOSTNAME;
            
        System.out.println("What is the Port? (hit enter for default: " + DEFAULT_PORT + " )"); //Ask Question
        String temp = stdIn.readLine(); // Temp String for input
        if(!temp.equals("")) //If not enter, set port number to temp
            portNumber = Integer.parseInt(temp);
        else //If enter, set port number to deafult
            portNumber = DEFAULT_PORT;
            
        System.out.println("What do you want to be called? (hit enter for default: " + DEFAULT_CLIENTNAME + " )");
        temp = stdIn.readLine(); // Use emp String again for input
        if(!temp.equals("")) //If not enter, set client name to temp
            clientName = temp;
        else //If enter, set client name to deafult
            clientName = DEFAULT_CLIENTNAME;
        
        Socket echoSocket = new Socket(hostName, portNumber); //Create Socket for Client to connect to Server
        
        PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true); //Print Writer to send input over
                                                                               //Socket to the Server.

        in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream())); //Set Buffered Reader "in"
                                                                                     //to input from Socket
                                                                                     
        out.println(clientName); //Send the name of the client to the server
        
        clientNumber = Integer.parseInt(in.readLine());       //The first data the server sends to the client
        System.out.println("Client number: " + clientNumber); //is the position of the client in the server
        System.out.println("Client name: " + clientName);     //array of clients.                                                    
                                                                                    
        Thread print = new ChatClient(); //Create a Thread to run seperatly that is always looking to print
                                         //incomming data to screen (see run() method).
        print.start(); //Start Thread
        try { //Try statement to catch errors from loosing contact with server.
            String userInput; //String for user's input
            while(true){
                if ((userInput = stdIn.readLine()) != null) { //If the userInput has characters(Triggers on
                                                              //enter key).                                    
                    out.println(userInput); //Send the users message to server.
                }
            }
        } catch (UnknownHostException e) { //Catch a error
            System.err.println("Don't know about host " + hostName);
        } catch (IOException e) { //Catch a error
            System.err.println("Couldn't get I/O for the connection to " + hostName);
        } 
    }
    
    /**
     * The method that is invoked when a ChatClient thread is created and started
     */
    public void run() {
        try {
            while (true) {
                System.out.println(in.readLine()); //Waits for and input from server then prints
            }
        }
        catch (IOException i) { //Catch error
            System.err.println("ERROR: IOEXCEPTION");
        }
    }
}