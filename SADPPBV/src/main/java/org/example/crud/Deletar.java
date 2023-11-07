package org.example.crud;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Deletar {
    public static void deletar(String token, String id, Socket socket) throws IOException {
        Map<String, Object> jsonMapDeletar = new HashMap<>();
        jsonMapDeletar.put("action", "excluir-usuario");

        Map<String, String> dataMapDeletar = new HashMap<>();
        dataMapDeletar.put("token", token);
        dataMapDeletar.put("user-id", String.valueOf(id));
        jsonMapDeletar.put("data", dataMapDeletar);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest= objectMapper.writeValueAsString(jsonMapDeletar);

        System.out.println("Enviando JSON para o servidor...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }
    public static void deletarResponce(String action, Boolean erro, String mensagem, Socket socket) throws IOException {
        Map<String, Object> jsonMapLogout = new HashMap<>();
        jsonMapLogout.put("action", action);

        Map<String, String> dataMapLogout = new HashMap<>();
        dataMapLogout.put("error", String.valueOf(erro));
        dataMapLogout.put("message", mensagem);
        jsonMapLogout.put("data", dataMapLogout);

        PrintWriter outToClientDelet = new PrintWriter(socket.getOutputStream(),true);
        ObjectMapper objMap = new ObjectMapper();
        String jsonResponseDelet = objMap.writeValueAsString(jsonMapLogout);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponseDelet);
        outToClientDelet.println(jsonResponseDelet);
        outToClientDelet.flush();
    }
    public static void deletarComum(String token, String email, String password, Socket socket) throws IOException {
        Map<String, Object> jsonMapDeletar = new HashMap<>();
        jsonMapDeletar.put("action", "excluir-proprio-usuario");

        Map<String, String> dataMapDeletar = new HashMap<>();
        dataMapDeletar.put("token", token);
        dataMapDeletar.put("email", email);
        dataMapDeletar.put("password", password);
        jsonMapDeletar.put("data", dataMapDeletar);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest= objectMapper.writeValueAsString(jsonMapDeletar);

        System.out.println("Enviando JSON para o servidor...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }
}
