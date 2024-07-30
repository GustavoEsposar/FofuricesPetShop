package dev.gustavoesposar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class AutenticacaoSenha {
    private static final String CARACTERES_ESPECIAIS_REGEX = "[!@#$%^&*]";
    private static final String NUMEROS_REGEX = "[0-9]";
    private static final String LETRAS_MAIUSCULAS_REGEX = "[A-Z]";
    private static final String LETRAS_MINUSCULAS_REGEX = "[a-z]";

    // Método para hash de senha
    public static String gerarHash(String plainTextPassword) {
        return BCrypt.withDefaults().hashToString(12, plainTextPassword.toCharArray());
    }

    // Método para verificar a senha
    public static boolean autenticarSenha(String plainTextPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainTextPassword.toCharArray(), hashedPassword);
        return result.verified;
    }

    public static void ehSenhaSegura(String s) {
        if (!contemCaracterEspecial(s)) {
            throw new IllegalArgumentException("A senha deve conter pelo menos um caractere especial.");
        }
        if (!contemNumero(s)) {
            throw new IllegalArgumentException("A senha deve conter pelo menos um número.");
        }
        if (!contemLetraMaiuscula(s)) {
            throw new IllegalArgumentException("A senha deve conter pelo menos uma letra maiúscula.");
        }
        if (!contemLetraMinuscula(s)) {
            throw new IllegalArgumentException("A senha deve conter pelo menos uma letra minúscula.");
        }
        if (!comprimentoSuficiente(s)) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        }
    }

    private static boolean contemCaracterEspecial(String s) {
        return contemRegex(s, CARACTERES_ESPECIAIS_REGEX);
    }

    private static boolean contemNumero(String s) {
        return contemRegex(s, NUMEROS_REGEX);
    }

    private static boolean contemLetraMaiuscula(String s) {
        return contemRegex(s, LETRAS_MAIUSCULAS_REGEX);
    }

    private static boolean contemLetraMinuscula(String s) {
        return contemRegex(s, LETRAS_MINUSCULAS_REGEX);
    }

    private static boolean comprimentoSuficiente(String s) {
        return s.length() >= 8;
    }

    private static boolean contemRegex(String s, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }
}