package org.example.system;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Functions {
    private static final List<User> allUsers = new ArrayList<>();
    public static String hash(String password) {
        return DigestUtils.md5Hex(password).toUpperCase();
    }

    public static boolean verificaSenha(String password, User logged) {
        if(logged.getPassword().equals(password))
            return true;
        else
            return false;
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean validate(String email, String password, User logged) {
        // Verificar se o email e a senha são válidos
        if (email == null || password == null) {
            System.out.println("Error. um ou mais campos estão faltando.");
            System.out.println("Email: " + email);
            System.out.println("Senha: " + password);

            return false;
        }
        // Verificar se o email está em um formato válido
        if (!isValidEmail(email)) {
            System.out.println("Error. email não está em um formato válido.");
            return false;
        }
        if (!verificaSenha(password, logged)){
            System.out.println("Error. senhas não combinam.");
            return false;
        }
        return true;
    }
}
