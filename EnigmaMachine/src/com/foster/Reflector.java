package com.foster;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the reflector functionality.
 * In effect an extra rotor with some different defaults.
 */
public class Reflector extends Rotor {

    /**
     * Options of reflectors.
     */
    public static final Reflector A = new Reflector("EJMZALYXVBWFCRQUONTSPIKHGD", String.valueOf(Rotor.toChar(0)), "A");
    public static final Reflector B = new Reflector("YRUHQSLDPXNGOKMIEBFZCWVJAT", String.valueOf(Rotor.toChar(0)), "B");
    public static final Reflector C = new Reflector("FVPJIAOYEDRZXWGCTKUQSBNMHL", String.valueOf(Rotor.toChar(0)), "C");
    public static final List<Reflector> PossibleReflectors = new ArrayList<>(Arrays.asList(Reflector.A, Reflector.B, Reflector.C));

    private Reflector(final String output, final String currentNotch, final String name) {
        super(output, currentNotch, name);
    }

    @Override
    public void setPosition(int pos) {
        super.setPosition(pos);
    }

    /**
     * Map reflector to user's choice in the config/UI selection.
     *
     * @param r character given of reflector choice
     * @return reflector
     */
    public static Reflector findMatchingReflector(char r){
        return switch (r) {
            case 'A' -> Reflector.A;
            case 'B' -> Reflector.B;
            case 'C' -> Reflector.C;
            default ->
                    throw new RuntimeException("No Reflector exists matching: " + r + " Please use A-C when defining an initial reflector.");
        };
    }

}
