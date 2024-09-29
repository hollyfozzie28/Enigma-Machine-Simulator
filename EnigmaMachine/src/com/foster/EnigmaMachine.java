package com.foster;

import com.foster.config.EnigmaConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class serves as the entry point for the machine execution.
 */
public class EnigmaMachine {
    private final Rotor[] rotors; // array of all rotors in use the engima machine
    private final char[] notches; // buenos notches
    private Reflector reflector;

    /**
     * Settings of the machine according to selected criteria in config file.
     *
     * @param numRotors number of rotors used in the machine (3 for Enigma M3).
     */
    public EnigmaMachine(final int numRotors) {
        rotors = new Rotor[numRotors];
        reflector = EnigmaConfig.SelectedReflector;
        notches = new char[this.rotors.length];
        setRotorAtPosition(0, EnigmaConfig.Rotor1);
        setRotorAtPosition(1, EnigmaConfig.Rotor2);
        setRotorAtPosition(2, EnigmaConfig.Rotor3);
       for (int i = 0; i < this.rotors.length; i++) {
            this.notches[i] = this.rotors[i].getCurrentNotch().toCharArray()[0];
        }
    }

    /**
     * Default constructor with 3 rotors.
     */
    public EnigmaMachine() {
        this(3);
    }

    /**
     * Encrypts the given inChar.
     * The message is passed through the plugboard and mapped to a pair.
     * Then the message is passed forward through the rotors, the reflector, and then passed back through the rotors in
     * reverse order.
     * Then the end message should be passed through the plugboard once more for final output.
     *
     * @param inChar Input message.
     * @return result
     */
    public EnigmaResult encrypt(final char inChar) {
        int index = 0;
        String result = "";
        final EnigmaResult res = new EnigmaResult();
        final List<String> steps = new ArrayList<>();
        boolean isCaps = false;
        if (!Character.isLetter(inChar)) {
            result = result + String.valueOf(inChar);
            res.result = result;
            return res;
        }

        if (Character.isUpperCase(inChar)) {
            isCaps = true;
        }

        this.moveRotors(this.rotors, 0);

        steps.add("Input letter: " + inChar);
        final char c = Character.toUpperCase(this.MapPlugboardPairs(inChar));
        steps.add("Plugboard: " + c);
        index = Rotor.toIndex(c);
        index = this.rotors[0].convertForward(this.rotors[0], index);
        steps.add("Rotor 1: " + Rotor.toChar(index % 26));
        index = this.rotors[1].convertForward(this.rotors[1], index);
        steps.add("Rotor 2: " + Rotor.toChar(index % 26));
        index = this.rotors[2].convertForward(this.rotors[2], index);
        steps.add("Rotor 3: " + Rotor.toChar(index % 26));
        index = this.reflector.convertForward(this.reflector, index);
        steps.add("Reflector: " + Rotor.toChar(index % 26));
        index = this.rotors[2].convertBackward(this.rotors[2], index);
        steps.add("Rotor 3: " + Rotor.toChar(index % 26));
        index = this.rotors[1].convertBackward(this.rotors[1], index);
        steps.add("Rotor 2: " + Rotor.toChar(index % 26));
        index = this.rotors[0].convertBackward(this.rotors[0], index);
        steps.add("Rotor 1: " + Rotor.toChar(index % 26));

        if (0 > index) {
            index += 26;
        }

        char resultChar = Rotor.toChar(index % 26);
        resultChar = this.MapPlugboardPairs(resultChar);
        steps.add("Plugboard: " + resultChar);
        if (!isCaps) {
            resultChar = Character.toLowerCase(resultChar);
        }

        final String resultCharAsString = Character.toString(resultChar);
        steps.add("Encrypted letter: " + resultCharAsString);
        result = result + resultCharAsString;
        res.steps = steps;
        res.result = result;
        return res;
    }

    /**
     * If inChar is in one of the plugboard pairs, the character is mapped and switched with its set pair.
     *
     * @param inChar encrypted character
     * @return swapped character
     */
    public char MapPlugboardPairs(final char inChar) {
        boolean matched = false;
        char outChar = Character.toUpperCase(inChar);
        final Set<Character> fwdPairKeys = PlugboardPairs.getPairs().keySet();
        if (fwdPairKeys.contains(outChar)) {
            matched = true;
            outChar = PlugboardPairs.getPairs().get(outChar);
        }

        final Set<Character> revPairKeys = PlugboardPairs.getRevPairs().keySet();
        if (revPairKeys.contains(outChar) && !matched) {
            outChar = PlugboardPairs.getRevPairs().get(outChar);
        }

        return outChar;
    }

    /**
     * Get the index equivalent of the character.
     */
    public int getCharIndex(final int stringChar, final int rotorChar) {
        if (26 > stringChar + rotorChar) {
            return stringChar + rotorChar;
        } else {
            return stringChar + rotorChar - 26;
        }
    }

    /**
     * Allows the user to advance rotor at position j
     *
     * @param rotors rotors array
     * @param j  instance to advance
     */
    public void moveRotors(final Rotor[] rotors, final int j) {
        if (0 > j || j > rotors.length - 1)
            throw new RuntimeException("com.fozzie.Rotor cannot be advanced at position " + j + "Size is out of scope of array length: " + rotors.length);
        final Rotor r = rotors[j];
        r.advance();
        if (r.atNotchPosition() && rotors.length > j + 1) {
            this.moveRotors(rotors, j + 1);
        }
    }

    /**
     * Initialise rotor at custom position.
     *
     * @param pos   position to start rotor at
     * @param rotor rotor instance
     */
    public void setRotorAtPosition(final int pos, final Rotor rotor) {
        this.rotors[pos] = rotor;
    }

    public void setReflector(final Reflector reflector) {
        this.reflector = reflector;
    }

    public Rotor[] getRotors() {
        return this.rotors;
    }

    public char[] getNotches() {
        return this.notches;
    }

    /**
     * Returns the result of each step of the character encryption, to display in the UI console.
     */
    public class EnigmaResult {
        List<String> steps;
        String result;

        public EnigmaResult() {
            this.steps = new ArrayList<>();
            this.result = "";
        }

        public List<String> getSteps() {
            return steps;
        }

        public void setSteps(final List<String> steps) {
            this.steps = steps;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}


