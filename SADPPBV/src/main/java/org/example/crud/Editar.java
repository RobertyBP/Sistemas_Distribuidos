package org.example.crud;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Editar {
    public static void editar(String token, String name, String email, String password, String user, String id, Socket socket) throws IOException {
        Map<String, Object> jsonMapEditar = new HashMap<>();
        jsonMapEditar.put("action", "edicao-usuario");

        Map<String, String> dataMapEditar = new HashMap<>();
        dataMapEditar.put("token", token);
        dataMapEditar.put("id", String.valueOf(id));
        dataMapEditar.put("name", name);
        dataMapEditar.put("email", email);
        dataMapEditar.put("password", password);
        dataMapEditar.put("type", user);
        jsonMapEditar.put("data", dataMapEditar);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest= objectMapper.writeValueAsString(jsonMapEditar);

        System.out.println("Enviando JSON para o servidor...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }
    public static void editarComum(String id, String name, String email, String password, Socket socket) throws IOException {
        Map<String, Object> jsonMapEditar = new HashMap<>();
        jsonMapEditar.put("action", "autoedicao-usuario");

        Map<String, String> dataMapEditar = new HashMap<>();

        dataMapEditar.put("id", String.valueOf(id));
        dataMapEditar.put("name", name);
        dataMapEditar.put("email", email);
        dataMapEditar.put("password", password);
        jsonMapEditar.put("data", dataMapEditar);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest= objectMapper.writeValueAsString(jsonMapEditar);

        System.out.println("Enviando JSON para o servidor...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }
}
