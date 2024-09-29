package com.foster.test;

import com.foster.EnigmaMachine;
import com.foster.PlugboardPairs;
import com.foster.Rotor;
import com.foster.config.EnigmaConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class tests the backend logic of the enigma machine
 * THE CONFIG MUST BE LEFT AT DEFAULT VALUES FOR THIS TESTING
 *
 * Regenerate config or set the following values:
 * Rotor_3=III
 * Rotor_2=II
 * Rotor_1=I
 * Reflector=A
 * Rotor_1_Initial_Position=E
 * Rotor_2_Initial_Position=F
 * Rotor_3_Initial_Position=A
 */
public class EnigmaBackend {


    @Test
    public void testEncrypt(){
        EnigmaConfig.checkAndGenerate();
        EnigmaMachine enigmaMachine = new EnigmaMachine(3);
        Assertions.assertEquals("q", enigmaMachine.encrypt('e').getResult());
    }

    @Test
    public void testEncryptNewRotors(){
        EnigmaConfig.checkAndGenerate();
        EnigmaMachine enigmaMachine = new EnigmaMachine(3);
        enigmaMachine.setRotorAtPosition(2, Rotor.V);
        Assertions.assertEquals("x", enigmaMachine.encrypt('e').getResult());
    }

    @Test
    public void testPlugboardPair(){
        EnigmaConfig.checkAndGenerate();
        EnigmaMachine enigmaMachine = new EnigmaMachine(3);
        enigmaMachine.setRotorAtPosition(2, Rotor.V);
        PlugboardPairs.BuildPairs('X', 'B');
        Assertions.assertEquals("b", enigmaMachine.encrypt('e').getResult());
    }

    @Test
    public void testNotch(){
        EnigmaConfig.checkAndGenerate();
        EnigmaMachine enigmaMachine = new EnigmaMachine(3);
        enigmaMachine.getRotors()[0].setToChar('Q');
        int pos = enigmaMachine.getRotors()[1].getCurrentPosition();
        enigmaMachine.moveRotors(enigmaMachine.getRotors(), 0);
        int newPos = enigmaMachine.getRotors()[1].getCurrentPosition();
        Assertions.assertNotEquals(pos, newPos);
    }

    @Test
    public void testEncryptAndDecrypt(){
        EnigmaConfig.checkAndGenerate();
        String testString = "The Quick Brown Fox Jumps Over The Lazy Dog.";
        StringBuilder sbEncrypt = new StringBuilder();
        StringBuilder sbDecrypt = new StringBuilder();
        EnigmaMachine enigmaMachine = new EnigmaMachine(3);
        enigmaMachine.getRotors()[0].setToChar(EnigmaConfig.Rotor1Initial);
        enigmaMachine.getRotors()[1].setToChar(EnigmaConfig.Rotor2Initial);
        enigmaMachine.getRotors()[2].setToChar(EnigmaConfig.Rotor3Initial);
        for (char c : testString.toCharArray()) {
            sbEncrypt.append(enigmaMachine.encrypt(c).getResult());
        }

        enigmaMachine.getRotors()[0].setToChar(EnigmaConfig.Rotor1Initial);
        enigmaMachine.getRotors()[1].setToChar(EnigmaConfig.Rotor2Initial);
        enigmaMachine.getRotors()[2].setToChar(EnigmaConfig.Rotor3Initial);
        for (char c : sbEncrypt.toString().toCharArray()) {
            sbDecrypt.append(enigmaMachine.encrypt(c).getResult());
        }
        Assertions.assertEquals(testString, sbDecrypt.toString());
    }

    @Test
    void toIndex() {
        Assertions.assertAll(() -> Assertions.assertEquals(0, Rotor.toIndex('A')),
                () -> Assertions.assertEquals(1, Rotor.toIndex('B')),
                () -> Assertions.assertEquals(2, Rotor.toIndex('C')),
                () -> Assertions.assertEquals(3, Rotor.toIndex('D')),
                () -> Assertions.assertEquals(4, Rotor.toIndex('E')),
                () -> Assertions.assertEquals(5, Rotor.toIndex('F')),
                () -> Assertions.assertEquals(6, Rotor.toIndex('G')),
                () -> Assertions.assertEquals(7, Rotor.toIndex('H')),
                () -> Assertions.assertEquals(8, Rotor.toIndex('I')));
    }

    @Test
    void toChar() {
        Assertions.assertAll(() -> Assertions.assertEquals('A', Rotor.toChar(0)),
                () -> Assertions.assertEquals('B', Rotor.toChar(1)),
                () -> Assertions.assertEquals('C', Rotor.toChar(2)),
                () -> Assertions.assertEquals('D', Rotor.toChar(3)),
                () -> Assertions.assertEquals('E', Rotor.toChar(4)),
                () -> Assertions.assertEquals('F', Rotor.toChar(5)),
                () -> Assertions.assertEquals('G', Rotor.toChar(6)),
                () -> Assertions.assertEquals('H', Rotor.toChar(7)),
                () -> Assertions.assertEquals('I', Rotor.toChar(8)));
    }

    @Test
    void setPosition() {
        EnigmaConfig.checkAndGenerate();
        EnigmaMachine enigmaMachine = new EnigmaMachine(3);

        enigmaMachine.getRotors()[0].setPosition(23);
        Assertions.assertEquals(23, enigmaMachine.getRotors()[0].getCurrentPosition());
    }

    @Test
    void convertForward() {
        EnigmaConfig.checkAndGenerate();
        EnigmaMachine enigmaMachine = new EnigmaMachine(3);
        Assertions.assertEquals('J', Rotor.toChar(enigmaMachine.getRotors()[0].convertForward(enigmaMachine.getRotors()[0], 25))); // this should get J from output of rotor I which is ASCII index 9
    }

    @Test
    void setToChar() {
        EnigmaConfig.checkAndGenerate();
        EnigmaMachine enigmaMachine = new EnigmaMachine(3);

        enigmaMachine.getRotors()[0].setToChar('J');
        Assertions.assertEquals(25, enigmaMachine.getRotors()[0].getCurrentPosition());
    }

}
