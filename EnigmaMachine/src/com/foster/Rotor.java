package com.foster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the functionality of the rotors.
 */
public class Rotor {

    public static int NUM_LETTERS = 26;
    public static int ASCII_A_NUM = 65;

    /**
     * Choice of rotors.
     */
    public static Rotor I = new Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "Q", "I");
    public static Rotor II = new Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE", "E", "II");
    public static Rotor III = new Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO", "V", "III");
    public static Rotor IV = new Rotor("ESOVPZJAYQUIRHXLNFTGKDCMWB", "J", "IV");
    public static Rotor V = new Rotor("VZBRGITYUPSDNHLXAWMJQOFECK", "Z", "V");
    public static List<Rotor> PossibleRotors = new ArrayList<>(Arrays.asList(Rotor.I, Rotor.II, Rotor.III, Rotor.IV, Rotor.V));
    private String output;
    private String currentNotch;
    private String reverseOutput;
    private int currentPosition;
    private String name = "NONE";
    // if the number is greater than 26
    //original array, index total - 26

    public Rotor(final String output, String currentNotch, String name) {
        this(output, currentNotch);
        this.name = name;
    }


    public Rotor(final String output, String currentNotch) {
        this.output = output;
        this.currentNotch = currentNotch;

        char[] reversed = new char[Rotor.NUM_LETTERS];

        for (int i = 0; i < output.length(); i++) {
            /*
             * The index access is based on whatever letter we're at, -65 so a will be 0, B = 1
             * Whatever position the current letter is in, we're adding the "ith" letter in the alphabet, to that position
             */
            reversed[output.charAt(i) - Rotor.ASCII_A_NUM] = (char) (i + Rotor.ASCII_A_NUM);

        }
        StringBuilder stringBuilder = new StringBuilder();
        for (final char c : reversed) {
            stringBuilder.append(c);
        }

        this.reverseOutput = stringBuilder.toString();
    }

    public static int toIndex(final char c) {
        return c - Rotor.ASCII_A_NUM;
    }

    //jeff R B
    //brian G M
    //Brian from M to N triggers Jeff to rotate

    /**
     * Map index number to equivalent character.
     *
     * @param index 0-26
     * @return character
     */
    public static char toChar(final int index) { // index = 0 - 26
        return (char) (index + Rotor.ASCII_A_NUM);
    }

    /**
     * Set the position of the rotor.
     *
     * @param pos current rotor position
     */
    public void setPosition(int pos) {
        if (NUM_LETTERS < pos) {
            pos = pos % Rotor.NUM_LETTERS;
        }
        this.currentPosition = pos;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    /**
     * Converting character forwards through the rotor.
     *
     * @param r rotor
     * @param index current index position
     * @return new resulting index
     */
    public int convertForward(final Rotor r, int index) {
        //index will always be in range 0-26
        int curIndex = Rotor.toIndex(r.output.charAt((index + this.currentPosition) % Rotor.NUM_LETTERS));
        curIndex = curIndex - this.currentPosition;
        while (0 > curIndex) {
            curIndex += Rotor.NUM_LETTERS;
        }
        return curIndex;

    }

    /**
     *Converting character backwards through the rotor.
     *
     * @param r rotor
     * @param index current index position
     * @return new resulting index
     */
    public int convertBackward(final Rotor r, int index) {
        //index will always be in range 0-26
        int i = 0;
        for (; i < r.output.length(); i += 1) {
            char a = toChar((index + this.currentPosition) % Rotor.NUM_LETTERS);
            if (r.output.charAt(i) == a) {
                break;
            }
        }
        int result = i - this.currentPosition;
        if (0 > result) {
            result += Rotor.NUM_LETTERS;
        }
        if (NUM_LETTERS < result) {
            result = result % Rotor.NUM_LETTERS;
        }

        return result;

    }

    /**
     * Check if we are at the rotor notch position.
     *
     * @return true if the current position we are at is the same as the rotor's notch
     */
    public boolean atNotchPosition() {
        return this.currentNotch.toCharArray()[0] == output.charAt(this.currentPosition - 1);
    }

    /**
     * Advance the position by 1.
     */
    public void advance() {
        this.currentPosition += 1;
        if (NUM_LETTERS < currentPosition) {
            this.currentPosition = this.currentPosition % Rotor.NUM_LETTERS;
        }
    }

    /**
     * Character position.
     *
     * @param inChar current character
     */
    public void setToChar( char inChar) {
        for (int i = 0; i < output.length(); i++) {
            if (output.charAt(i) == inChar) {
                setPosition(i);
            }
        }
    }

    /**
     * Map reflector to user's choice in the config/UI selection.
     *
     * @param rot string given of rotor choice
     * @return rotor
     */
    public static Rotor findMatchingRotor(String rot){
        return switch (rot) {
            case "I" -> Rotor.I;
            case "II" -> Rotor.II;
            case "III" -> Rotor.III;
            case "IV" -> Rotor.IV;
            case "V" -> Rotor.V;
            default -> throw new RuntimeException("No Rotor matching input string: " + rot);
        };
    }


    public String getReverseOutput() {
        return this.reverseOutput;
    }

    public String getOutput() {
        return this.output;
    }

    public String getCurrentNotch() {
        return this.currentNotch;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

