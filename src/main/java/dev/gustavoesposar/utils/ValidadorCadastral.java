package dev.gustavoesposar.utils;

public class ValidadorCadastral {
    public static String validarENormalizarTelefone(String phoneNumber) throws IllegalArgumentException {
        String regex = "^(\\(\\d{2,3}\\) ?)?(\\d{4,5}-\\d{4})$";

        if (!phoneNumber.matches(regex)) {
            throw new IllegalArgumentException("Número de telefone inválido: " + phoneNumber);
        }

        String normalizedPhoneNumber = phoneNumber.replaceAll("\\D", "");

        if (normalizedPhoneNumber.length() > 12) {
            throw new IllegalArgumentException("Número de telefone deve ter até 12 dígitos: " + normalizedPhoneNumber);
        }

        return normalizedPhoneNumber;
    }

    public static String validarENormalizarCnpj(String cnpj) throws IllegalArgumentException {
        // Remove todos os caracteres que não são dígitos
        String normalizedCNPJ = cnpj.replaceAll("\\D", "");

        // Verifica se o CNPJ normalizado tem exatamente 14 dígitos
        if (normalizedCNPJ.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve ter exatamente 14 dígitos: " + normalizedCNPJ);
        }

        return normalizedCNPJ;
    }
}
