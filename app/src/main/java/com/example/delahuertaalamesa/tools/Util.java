package com.example.delahuertaalamesa.tools;

import java.util.Objects;

/**
 * Class used for exception handling
 */
public class Util {
    public static String PrintEx(Exception e) {
        Throwable cause = e.getCause();
        StringBuilder exit = new StringBuilder();
        while (cause != null) {
            exit.append(cause).append(System.lineSeparator());
            cause = cause.getCause();
        }
        if (exit.toString().equalsIgnoreCase(""))
            exit = new StringBuilder(Objects.requireNonNull(e.getMessage()));
        return exit.toString();
    }
}
