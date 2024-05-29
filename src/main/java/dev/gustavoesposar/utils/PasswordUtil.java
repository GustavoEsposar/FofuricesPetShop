package dev.gustavoesposar.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    // Método para hash de senha
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.withDefaults().hashToString(12, plainTextPassword.toCharArray());
    }

    // Método para verificar a senha
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainTextPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
