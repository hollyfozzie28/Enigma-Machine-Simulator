package com.foster;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class serves to map plugboard letters together.
 */
public enum PlugboardPairs {
    ;
    private static final HashMap<Character, Character> pairs = new HashMap<>();
    private static final HashMap<Character, Character> revPairs = new HashMap<>();
    static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();

    public static HashMap<Character, Character> getPairs() {
        return PlugboardPairs.pairs;
    }

    public static HashMap<Character, Character> getRevPairs() {
        return PlugboardPairs.revPairs;
    }

    /**
     * Building each pair, for forwards and backwards encryption.
     *
     * @param c1 first character in the pair
     * @param c2 second character in the pair
     */
    public static void BuildPairs(final char c1, final char c2) {
        if (PlugboardPairs.pairs.containsKey(c1) || PlugboardPairs.revPairs.containsKey(c2)) {
            throw new IllegalArgumentException("Character already in map");
        }
        PlugboardPairs.pairs.put(c1, c2);
        PlugboardPairs.revPairs.put(c2, c1);
    }

    /**
     * Remove pairs if user deletes one in the UI.
     *
     * @param key character pair to remove
     */
    public static void RemovePairs(final char key) {
        if (PlugboardPairs.pairs.containsKey(key)) {
            final char revKey = PlugboardPairs.pairs.get(key);
            PlugboardPairs.pairs.remove(key);
            PlugboardPairs.revPairs.remove(revKey);
        }
    }

    /**
     * Find the letters that have not yet been used in a plugboard pair.
     *
     * @return array of letters remaining after chosen plugboard pairs
     */
    public static ArrayList<Character> getRemainingAlphabetChars() {
        final ArrayList<Character> returnArr = new ArrayList<>();
        for (final char letter : PlugboardPairs.alphabet) {
            if (PlugboardPairs.pairs.containsKey(letter) || PlugboardPairs.revPairs.containsKey(letter)) continue;
            returnArr.add(letter);
        }
        return returnArr;
    }
}
