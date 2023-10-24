package org.example.senders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Cad {
    String name;
    String email;
    String password;
    String type;
    String token;
    Socket socket;

    public Cad(String name, String email, String password, String type, String token, Socket socket) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.token = token;
        this.socket = socket;
    }

    public Cad(String name, String email, String password, Socket socket) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.socket = socket;
    }

    public void registrar() throws IOException {
        Map<String, Object> jsonMapCadastro = new HashMap<>();
        jsonMapCadastro.put("action", "cadastro-usuario");

        Map<String, String> dataMapCadastro = new HashMap<>();
        dataMapCadastro.put("nome", name);
        dataMapCadastro.put("email", email);
        dataMapCadastro.put("senha", password);
        dataMapCadastro.put("token", token);
        dataMapCadastro.put("tipo", type);
        jsonMapCadastro.put("data", dataMapCadastro);

        // Enviar o JSON para o servidor
        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(jsonMapCadastro);

        System.out.println("Enviando JSON...\n");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest + "\n");
        outToServer.flush();
    }
    public void auto_Registrar() throws IOException {
        Map<String, Object> jsonMapCadastro = new HashMap<>();
        jsonMapCadastro.put("action", "autocadastro-usuario");

        Map<String, String> dataMapCadastro = new HashMap<>();
        dataMapCadastro.put("nome", name);
        dataMapCadastro.put("email", email);
        dataMapCadastro.put("senha", password);
        jsonMapCadastro.put("data", dataMapCadastro);

        // Enviar o JSON para o servidor
        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(jsonMapCadastro);

        System.out.println("Enviando JSON...\n");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest + "\n");
        outToServer.flush();
    }
}
