package dev.gustavoesposar.utils;

import java.util.regex.Pattern;

public class AutenticacaoEmail {

    private static final String EMAIL_REGEX = "^[^@]+@[^@]+\\.[^@]{2,}$";

    public void verificarEmailCorreto(String email) {
        if (!contemArroba(email)) {
            throw new IllegalArgumentException("O email deve conter um '@'.");
        }
        if (!temDoisGruposComDominio(email)) {
            throw new IllegalArgumentException("O email deve conter um domínio válido.");
        }
    }

    private boolean contemArroba(String email) {
        return email.contains("@");
    }

    private boolean temDoisGruposComDominio(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }
}