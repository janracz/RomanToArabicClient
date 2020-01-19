/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.raczynski.romantoarabicclient.controler;

import pl.polsl.raczynski.romantoarabicclient.view.View;
import pl.polsl.raczynski.romantoarabicclient.network.TCPClient;

import java.io.*;
import java.net.*;
import java.util.Properties;

/**
 * Class that controls the flow of program
 * 
 * @author Jasiek
 * @version 1.0
 */
public class RomanToArabicClientControler {

    /**
     * Properties containing server's IP address and port.
     */
    private Properties properties;
    /**
     * View object. It is used to print messages on console and read messages
     * from console.
     */
    private View view;
    /**
     * TCP connection. It is used to communicate with server.
     */
    private TCPClient client;

    /**
     * Constructor of controller. Loads properties from file or if there are not
     * any available properties, it uses default values. It also creates view
     * object.
     */
    public RomanToArabicClientControler() {
        view = new View();
        view.displayStartingMessage();

        boolean success = true;

        view.writeMessagesOnTheConsole("Loading RomanToArabicClient.properties file.");
        properties = new Properties();

        try (FileInputStream in = new FileInputStream("RomanToArabicClient.properties")) {
            properties.load(in);
        } catch (FileNotFoundException e) {
            view.writeMessagesOnTheConsole("File RomanToArabicClient.properties can not be found!.");
            success = false;
        } catch (IOException e) {
            view.writeMessagesOnTheConsole("Properties can not be loaded!.");
            success = false;
        }
        if (properties.getProperty("serverAddress") == null || properties.getProperty("port") == null) {
            view.writeMessagesOnTheConsole("Properties can not be loaded!.");
            success = false;
        }
        try {
            Integer.parseInt(properties.getProperty("port"));
        } catch (NumberFormatException e) {
            view.writeMessagesOnTheConsole("Wrong port in properties!");
            success = false;
        }
        try {
            InetAddress.getByName(properties.getProperty("serverAddress"));
        } catch (UnknownHostException e) {
            view.writeMessagesOnTheConsole("Wrong serverAddress in properties!");
            success = false;
        }

        if (success == false) {
            view.writeMessagesOnTheConsole("Default properties will be loaded.");
            properties = new Properties();
            properties.setProperty("port", String.valueOf(4728));
            properties.setProperty("serverAddress", "localhost");
        }
        try (FileOutputStream out = new FileOutputStream("RomanToArabicClient.properties")) {
            properties.store(out, "--Configuration--");
        } catch (IOException e) {
            view.writeMessagesOnTheConsole("Properties can not be saved!");
        }
    }

    /**
     * Tries to connect with server.
     *
     * @return true if successfully connected with server.
     */
    private boolean connectToServer() {
        view.writeMessagesOnTheConsole("Connecting to server...");
        try {
            client = new TCPClient(Integer.parseInt(properties.getProperty("port")), InetAddress.getByName(properties.getProperty("serverAddress")));
        } catch (IOException e) {
            view.writeMessagesOnTheConsole("Connecting to server failed.");
        }
        if (client == null) {
            view.writeMessagesOnTheConsole("Client couldn't be connected to server!");
            return false;
        } else {
            view.writeMessagesOnTheConsole("Client is connected to server.");
            try {
                view.writeMessagesOnTheConsole(client.receiveMessageFromServer());
            } catch (IOException e) {
                view.writeMessagesOnTheConsole("Receiving welcome message from server failed.");
            }
            return true;
        }
    }

    /**
     * Allows to reconnect with server.
     */
    private boolean reconnect() {
        while (connectToServer() == false) {
            view.writeMessagesOnTheConsole("If You want to connect to server again write Y, if not write N.");
            while (true) {
                if (view.readMessageFromConsole().toUpperCase().equals("Y")) {
                    break;
                }
                if (view.readMessageFromConsole().toUpperCase().equals("N")) {
                    view.writeMessagesOnTheConsole("Closing client.");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tries to connect with server. Listens for commands and makes apropriate
     * steps. If parameters were passed: displays help if 'h' was given; solves
     * knight tour problem if starting field's co-ordinates were given.
     *
     */
    //public void run(String[] args) {
    public void run() {
        boolean connectionMade = reconnect();
        
        client.sendCommandToServer("HELLO");
            try {
                view.writeMessagesOnTheConsole(client.receiveMessageFromServer());
            } catch (IOException e) {
                view.writeMessagesOnTheConsole("Issue while receiving connection with server!");
            }
        
        while (connectionMade){
            view.writeMessagesOnTheConsole("Enter your roman number to convert: ");
            String numberToConvert = view.readMessageFromConsole();
            

            client.sendCommandToServer("SET " + numberToConvert);
            try {
                view.writeMessagesOnTheConsole(client.receiveMessageFromServer());
            } catch (IOException e) {
                view.writeMessagesOnTheConsole("Issue while receiving connection with server!");
            }
        
            client.sendCommandToServer("RUN");
            try {
                view.writeMessagesOnTheConsole(client.receiveMessageFromServer());
            } catch (IOException e) {
                view.writeMessagesOnTheConsole("Issue while receiving connection with server!");
            }
        
            client.sendCommandToServer("GET");
            try {
                view.writeMessagesOnTheConsole(client.receiveMessageFromServer());
            } catch (IOException e) {
                view.writeMessagesOnTheConsole("Issue while receiving connection with server!");
            }
            
            String quitProgram = "";
            boolean repeat = true;
            
            while (repeat) {
                view.writeMessagesOnTheConsole("Do you want to quit? Y/N: ");
                quitProgram = view.readMessageFromConsole();
                if (quitProgram.equals("Y")) {
                    repeat = false;
                    connectionMade = false;
                }
                else if (quitProgram.equals("N")) {
                    repeat = false;
                }
                else {
                    view.writeMessagesOnTheConsole("UNKNOWN ANWSER!");
                }
            }
        }
        
        client.sendCommandToServer("QUIT");
        try {
            view.writeMessagesOnTheConsole(client.receiveMessageFromServer());
        } catch (IOException e) {
            view.writeMessagesOnTheConsole("Issue while receiving connection with server!");
        } 


    }
}
