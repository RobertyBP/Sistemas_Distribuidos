package org.example.senders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Login {
    private final String email;
    private final String password;
    private final Socket socket;
    private String token;


    public Login(String email, String password, Socket socket) {
        this.email = email;
        this.password = password;
        this.socket = socket;
        this.token = "";
    }

    public void loginRequest() throws IOException {
        try {
            // Criar o JSON para o login
            Map<String, Object> jsonMapLogin = new HashMap<>();
            jsonMapLogin.put("action", "login");

            Map<String, String> dataMapLogin = new HashMap<>();
            dataMapLogin.put("email", email);
            dataMapLogin.put("password", password);
            jsonMapLogin.put("data", dataMapLogin);

            ObjectMapper objectMapper = new ObjectMapper();

            // Enviar o JSON de login para o servidor
            PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
            String jsonRequest = objectMapper.writeValueAsString(jsonMapLogin);

            System.out.println("Enviando JSON...");
            System.out.println(jsonRequest);
            outToServer.println(jsonRequest + "\r\n");
            outToServer.flush();

        } catch (IOException e) {
            System.err.println("Error " + e.getMessage());
        }

    }

}