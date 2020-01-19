/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.raczynski.romantoarabicclient.view;

import java.io.*;

/**
 * Class to manage the view of program
 * 
 * @author Jasiek
 * @version 1.0
 */
public class View {

    /**
     * buffered input character stream.
     */
    private final BufferedReader input;

    /**
     * Constructor of view class.
     */
    public View() {
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Reads messages from console.
     *
     * @return string with messages from console.
     */
    public String readMessageFromConsole() {
        String str = "";
        try {
            str = input.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return str;
    }

    /**
     * Prints message on the console.
     *
     * @param messages strings with messages which should be print on the
     * console.
     */
    public void writeMessagesOnTheConsole(String... messages) {
        for (String s : messages) {
            System.out.println(s);
        }
    }

    /**
     * Prints message with information that server started.
     */
    public void displayStartingMessage() {
        System.out.println("Server started!");
    }
}
