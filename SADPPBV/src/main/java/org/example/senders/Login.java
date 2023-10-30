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

    public static void loginResponce1(String action, Boolean erro, String mensagem, Socket socket, String token) throws IOException {

        Map<String, Object> jsonMapLogin = new HashMap<>();
        jsonMapLogin.put("action", action);

        Map<String, String> dataMapLogin = new HashMap<>();
        dataMapLogin.put("error", String.valueOf(erro));
        dataMapLogin.put("message", mensagem);
        dataMapLogin.put("token", token);
        jsonMapLogin.put("data", dataMapLogin);

        PrintWriter outToClientLogin = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objMapperLogin = new ObjectMapper();
        String jsonResponseLogin = objMapperLogin.writeValueAsString(jsonMapLogin);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponseLogin);
        outToClientLogin.println(jsonResponseLogin);
        outToClientLogin.flush();
    }

    public static void loginResponce2(String action, Boolean erro, String mensagem, Socket socket) throws IOException {

        Map<String, Object> jsonMapLogin = new HashMap<>();
        jsonMapLogin.put("action", action);

        Map<String, String> dataMapLogin = new HashMap<>();
        dataMapLogin.put("error", String.valueOf(erro));
        dataMapLogin.put("message", mensagem);
        jsonMapLogin.put("data", dataMapLogin);

        PrintWriter outToClientLogin = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objMapperLogin = new ObjectMapper();
        String jsonResponseLogin = objMapperLogin.writeValueAsString(jsonMapLogin);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponseLogin);
        outToClientLogin.println(jsonResponseLogin);
        outToClientLogin.flush();

    }

    public void loginRequest() throws IOException {

        Map<String, Object> jsonMapLogin = new HashMap<>();
        jsonMapLogin.put("action", "login");

        Map<String, String> dataMapLogin = new HashMap<>();
        dataMapLogin.put("email", email);
        dataMapLogin.put("password", password);
        jsonMapLogin.put("data", dataMapLogin);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(jsonMapLogin);

        System.out.println("Enviando JSON...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }

}