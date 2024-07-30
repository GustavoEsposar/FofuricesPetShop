package dev.gustavoesposar.utils;

import java.util.regex.Pattern;

public class AutenticacaoEmail {

    private static final String EMAIL_REGEX = "^[^@]+@[^@]+\\.[^@]{2,}$";

    public static String verificarEmailCorreto(String email) {
        if (!contemArroba(email)) {
            throw new IllegalArgumentException("O email deve conter um '@'.");
        }
        if (!temDoisGruposComDominio(email)) {
            throw new IllegalArgumentException("O email deve conter um domínio válido.");
        }
        return email;
    }

    private static boolean contemArroba(String email) {
        return email.contains("@");
    }

    private static boolean temDoisGruposComDominio(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }
}