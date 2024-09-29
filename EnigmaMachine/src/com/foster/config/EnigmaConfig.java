package com.foster.config;

import com.foster.Reflector;
import com.foster.Rotor;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Properties;


public class EnigmaConfig {

    public static Rotor Rotor1;
    public static char Rotor1Initial;
    public static Rotor Rotor2;
    public static char Rotor2Initial;
    public static Rotor Rotor3;
    public static char Rotor3Initial;

    public static Reflector SelectedReflector;



    public static void readProps(){
        //for windows, use '\\' instead of '/'
        File configFile = new File(System.getProperty("user.dir") + "/config.properties");
        //File configFile = new File(System.getProperty("user.dir") + "out/artifacts/Engima_jar/config.properties");


        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            Rotor1 = Rotor.findMatchingRotor(props.getProperty("Rotor_1"));
            Rotor2 = Rotor.findMatchingRotor(props.getProperty("Rotor_2"));
            Rotor3 = Rotor.findMatchingRotor(props.getProperty("Rotor_3"));

            SelectedReflector = Reflector.findMatchingReflector(props.getProperty("Reflector").toUpperCase().charAt(0));

            Rotor1Initial = props.getProperty("Rotor_1_Initial_Position").toUpperCase().charAt(0);
            Rotor2Initial = props.getProperty("Rotor_2_Initial_Position").toUpperCase().charAt(0);
            Rotor3Initial = props.getProperty("Rotor_3_Initial_Position").toUpperCase().charAt(0);
            if(!Character.isLetter(Rotor1Initial)
            || !Character.isLetter(Rotor2Initial)
            || !Character.isLetter(Rotor3Initial)){
                throw new RuntimeException("Initial rotor position must be set to letter A-Z");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateProps(){
        File configFile = new File("config.properties");
        try {
            Properties props = new Properties();
            props.setProperty("Rotor_1", "I");
            props.setProperty("Rotor_2", "II");
            props.setProperty("Rotor_3", "III");
            props.setProperty("Rotor_1_Initial_Position", "E");
            props.setProperty("Rotor_2_Initial_Position", "F");
            props.setProperty("Rotor_3_Initial_Position", "A");

            props.setProperty("Reflector", "A");
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Settings for the Enigma Machine");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkAndGenerate(){
        File configFile = new File("config.properties");
        if (configFile.exists() && !configFile.isDirectory()) {
            readProps();
        } else {
            generateProps();
            readProps();
        }
    }

}
