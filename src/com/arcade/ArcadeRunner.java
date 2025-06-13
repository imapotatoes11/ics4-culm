/*
 * ArcadeRunner.java
 *
 * Date: 06 12, 2025
 *
 * Copyright 2025 Kevin Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the license at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the license is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * license for the specific language governing permissions and limitations under
 * the license.
 */
package com.arcade;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.arcade.player.Player;

public class ArcadeRunner {
    public static void main(String[] args) {
        ArcadeManager arcadeManager = new ArcadeManager();
        Scanner sc = new Scanner(System.in);

        arcadeManager.loadFromFile();

        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("Welcome to Arcade!");
            System.out.println("Would you like to:");
            System.out.println("  1. Log in as a user");
            System.out.println("  2. Create an account");
            System.out.println("  3. Exit");
            System.out.print("Enter an option: ");
            int option = Integer.parseInt(String.valueOf(sc.nextLine().charAt(0)));
            if (option == 3)
                return;
            if (option == 2) {
                System.out.print("Enter username (must be unique): ");
                String username = sc.nextLine();
                // Check if username already exists
                if (arcadeManager.searchForPlayer(username.toLowerCase()) != null) {
                    System.out
                            .println("Error: Username already exists. Try logging in or select a different username.");
                } else {
                    // System.out.print("Enter password: ");
                    Console console = System.console();
                    String password = new String(console.readPassword("Enter Password: "));
                    // hash password
                    String hashedPassword = generateSHA256(password);
                    // Create a new player and add to the arcade manager
                    Player newPlayer = new Player(username, hashedPassword);
                    arcadeManager.addPlayer(newPlayer);
                    System.out.println("Account created successfully! You can now log in.");
                }
            }
            if (option == 1) {
                System.out.print("Enter username: ");
                String usernameInput = sc.nextLine();
                // System.out.print("Enter password: ");
                Console console = System.console();
                String passwordInput = new String(console.readPassword("Enter Password: "));
                // hash password
                String hashedPassword = generateSHA256(passwordInput);
                ArcadeManager.LoginStatus status = arcadeManager.tryLogin(usernameInput, hashedPassword);
                switch (status) {
                    case USERNAME_NOT_FOUND:
                        System.out.println("Error: Username not found. Try again or sign up as a new user.");
                        break;
                    case INCORRECT_PASSWORD:
                        System.out.println("Error: The password you entered is incorrect. Please try again.");
                        break;
                    case SUCCESS:
                        System.out.println("Login success!");
                        loggedIn = true;
                        break;
                }
            }
        }
    }

    /**
     * Generates a SHA-256 hash for a given string.
     *
     * @param input The string to be hashed.
     * @return The SHA-256 hash as a hexadecimal string. Returns null if the
     *         algorithm is not found.
     */
    public static String generateSHA256(String input) {
        try {
            // Get an instance of the MessageDigest for the SHA-256 algorithm.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hash computation.
            // We get the bytes of the string using UTF-8 encoding, which is standard.
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array into a hexadecimal string.
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            // This exception is thrown if the algorithm (e.g., "SHA-256") is not available.
            // It's highly unlikely for standard algorithms like SHA-256.
            System.err.println("SHA-256 algorithm not found!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to convert a byte array into a hexadecimal string
     * representation.
     *
     * @param hash The byte array to convert.
     * @return The hexadecimal string.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            // Convert each byte to a hex value.
            String hex = Integer.toHexString(0xff & hash[i]);
            // Prepend '0' if the hex value is only one character.
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
