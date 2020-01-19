/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.raczynski.romantoarabicclient.network;

import java.net.*;
import java.io.*;

/**
 *
 * @author Jasiek
 */
public class TCPClient implements Closeable {

    /**
     * Socket representing client's connection.
     */
    private final Socket clientSocket;
    /**
     * Buffered input character stream from server.
     */
    private final BufferedReader inputFromServer;
    /**
     * Formatted output character stream to server.
     */
    private final PrintWriter outputToServer;

    /**
     * Constructor of tcp client. It creates socket, input and output to
     * communicate with server.
     *
     * @param port number of server's port.
     * @param address IP address of server.
     * @throws IOException when there is a problem with creating input, output
     * or socket object.
     */
    public TCPClient(Integer port, InetAddress address) throws IOException {
        clientSocket = new Socket(address, port);
        outputToServer = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                clientSocket.getOutputStream())), true);
        inputFromServer = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
    }

    /**
     * Sends commands to server.
     *
     * @param command which should be send to server.
     */
    public void sendCommandToServer(String command) {
        command = command.toUpperCase();
        outputToServer.println(command);
    }

    /**
     * Closes client's socket (if it is open).
     *
     * @throws IOException when there is a problem with closing socket.
     */
    @Override
    public void close() throws IOException {
        if (clientSocket != null) {
            clientSocket.close();
        }
    }

    /**
     * Reads messages from server.
     *
     * @return string with message received from server.
     * @throws IOException when there is a problem with reading input from
     * stream.
     */
    public String receiveMessageFromServer() throws IOException {
        String str;
        String message = "";
        while (true) {
            str = inputFromServer.readLine();

            if (str == null) {
                break;
            } else if (str.toUpperCase().equals("==START==")) {
            } else if (str.toUpperCase().equals("==END==")) {
                break;
            } else {
                message = message + str + "\n";
            }
        }
        str = message;
        return str;
    }
}
