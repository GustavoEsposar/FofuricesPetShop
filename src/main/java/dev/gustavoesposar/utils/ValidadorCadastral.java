package dev.gustavoesposar.utils;

public class ValidadorCadastral {
    public static String validarENormalizarTelefone(String phoneNumber) throws IllegalArgumentException {
        String regex = "^(\\(?\\d{2,3}\\)? ?)?(\\d{4,5}-?\\d{4})$";

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

    public static String formatarCnpj(String cnpjNormalizado) throws IllegalArgumentException {

        String formattedCNPJ = String.format("%s.%s.%s/%s-%s",
                cnpjNormalizado.substring(0, 2),
                cnpjNormalizado.substring(2, 5),
                cnpjNormalizado.substring(5, 8),
                cnpjNormalizado.substring(8, 12),
                cnpjNormalizado.substring(12, 14));

        return formattedCNPJ;
    }

    public static String formatarTelefone(String phoneNumber) throws IllegalArgumentException {
        String formattedPhoneNumber;

        if (phoneNumber.length() == 10) {
            // Formato para números com 10 dígitos: (XX) XXXX-XXXX
            formattedPhoneNumber = String.format("(%s) %s-%s",
                    phoneNumber.substring(0, 2),
                    phoneNumber.substring(2, 6),
                    phoneNumber.substring(6, 10));
        } else if (phoneNumber.length() == 11) {
            // Formato para números com 11 dígitos: (XX) XXXXX-XXXX
            formattedPhoneNumber = String.format("(%s) %s-%s",
                    phoneNumber.substring(0, 2),
                    phoneNumber.substring(2, 7),
                    phoneNumber.substring(7, 11));
        } else {
            // Formato para números com 12 dígitos: (XXX) XXXX-XXXX
            formattedPhoneNumber = String.format("(%s) %s-%s",
                    phoneNumber.substring(0, 3),
                    phoneNumber.substring(3, 7),
                    phoneNumber.substring(7, 11));
        }

        return formattedPhoneNumber;
    }

}
