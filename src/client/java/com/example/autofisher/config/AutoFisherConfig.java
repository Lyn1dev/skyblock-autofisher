package com.example.autofisher.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AutoFisherConfig {

    private static final Path CONFIG_FILE = Paths.get("autofisher.properties");
    private static final Path FIREVEIL_MESSAGES_FILE = Paths.get("src/client/resources/fireveil_messages.txt");

    public static boolean enabled = true; // New: Overall mod enable/disable 
    public static boolean enableRecast = true;
    public static boolean debugMode = true; // Default to true for debugging
    public static boolean fireveilEnabled = false; // Default to false
    public static int fireveilDelayToSlot2_base = 100;
    public static int fireveilDelayToSlot2_random = 80;
    public static int fireveilDelayToRightClick_base = 80;
    public static int fireveilDelayToRightClick_random = 70;
    public static int fireveilDelayToOriginalSlot_base = 90;
    public static int fireveilDelayToOriginalSlot_random = 80;

    public static void load() {
        if (!Files.exists(CONFIG_FILE)) {
            save(); // Create default config if it doesn't exist
        }

        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(CONFIG_FILE)) {
            properties.load(reader);
            enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true"));
            enableRecast = Boolean.parseBoolean(properties.getProperty("enableRecast", "true"));
            debugMode = Boolean.parseBoolean(properties.getProperty("debugMode", "true"));
            fireveilEnabled = Boolean.parseBoolean(properties.getProperty("fireveilEnabled", "false"));
            fireveilDelayToSlot2_base = Integer.parseInt(properties.getProperty("fireveilDelayToSlot2_base", "100"));
            fireveilDelayToSlot2_random = Integer.parseInt(properties.getProperty("fireveilDelayToSlot2_random", "80"));
            fireveilDelayToRightClick_base = Integer.parseInt(properties.getProperty("fireveilDelayToRightClick_base", "80"));
            fireveilDelayToRightClick_random = Integer.parseInt(properties.getProperty("fireveilDelayToRightClick_random", "70"));
            fireveilDelayToOriginalSlot_base = Integer.parseInt(properties.getProperty("fireveilDelayToOriginalSlot_base", "90"));
            fireveilDelayToOriginalSlot_random = Integer.parseInt(properties.getProperty("fireveilDelayToOriginalSlot_random", "80"));
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load AutoFisher config: " + e.getMessage());
            // Reset to default values on error
            enabled = true;
            enableRecast = true;
            debugMode = true;
            fireveilEnabled = false;
            save(); // Save defaults to overwrite corrupted file
        }

    }

    public static void save() {
        Properties properties = new Properties();
        properties.setProperty("enabled", String.valueOf(enabled));
        properties.setProperty("enableRecast", String.valueOf(enableRecast));
        properties.setProperty("debugMode", String.valueOf(debugMode));
        properties.setProperty("fireveilEnabled", String.valueOf(fireveilEnabled));
        properties.setProperty("fireveilDelayToSlot2_base", String.valueOf(fireveilDelayToSlot2_base));
        properties.setProperty("fireveilDelayToSlot2_random", String.valueOf(fireveilDelayToSlot2_random));
        properties.setProperty("fireveilDelayToRightClick_base", String.valueOf(fireveilDelayToRightClick_base));
        properties.setProperty("fireveilDelayToRightClick_random", String.valueOf(fireveilDelayToRightClick_random));
        properties.setProperty("fireveilDelayToOriginalSlot_base", String.valueOf(fireveilDelayToOriginalSlot_base));
        properties.setProperty("fireveilDelayToOriginalSlot_random", String.valueOf(fireveilDelayToOriginalSlot_random));

        try (var writer = Files.newBufferedWriter(CONFIG_FILE)) {
            properties.store(writer, "AutoFisher Configuration");
        } catch (IOException e) {
            System.err.println("Failed to save AutoFisher config: " + e.getMessage());
        }
    }
}
