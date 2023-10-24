package org.example.senders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Logout {
    public static void logout(String token, Socket socket) throws Exception {
        // Criar o JSON para a ação de logout
        Map<String, Object> jsonMapLogout = new HashMap<>();
        jsonMapLogout.put("action", "logout-usuario");

        Map<String, String> dataMapLogout = new HashMap<>();
        dataMapLogout.put("token", token);
        jsonMapLogout.put("data", dataMapLogout);

        // Enviar o JSON para o servidor
        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest= objectMapper.writeValueAsString(jsonMapLogout);

        System.out.println("Enviando JSON para o servidor...");
        outToServer.println(jsonRequest + "\r\n");
        outToServer.flush();
    }

}
