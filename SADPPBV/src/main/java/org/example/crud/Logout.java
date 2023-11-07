package org.example.crud;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Logout {
    public static void logoutResponce(String action, Boolean erro, String mensagem, Socket socket) throws IOException {
        Map<String, Object> jsonMapLogout = new HashMap<>();
        jsonMapLogout.put("action", action);

        Map<String, String> dataMapLogout = new HashMap<>();
        dataMapLogout.put("error", String.valueOf(erro));
        dataMapLogout.put("message", mensagem);
        jsonMapLogout.put("data", dataMapLogout);

        PrintWriter outToClientLogout = new PrintWriter(socket.getOutputStream(),true);
        ObjectMapper objMap = new ObjectMapper();
        String jsonResponseLogout = objMap.writeValueAsString(jsonMapLogout);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponseLogout);
        outToClientLogout.println(jsonResponseLogout);
        outToClientLogout.flush();
    }
    public static void logoutRequest(String token, Socket socket) throws Exception {

        Map<String, Object> jsonMapLogout = new HashMap<>();
        jsonMapLogout.put("action", "logout-usuario");

        Map<String, String> dataMapLogout = new HashMap<>();
        dataMapLogout.put("token", token);
        jsonMapLogout.put("data", dataMapLogout);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest= objectMapper.writeValueAsString(jsonMapLogout);

        System.out.println("Enviando JSON...");
        outToServer.println(jsonRequest);
        outToServer.flush();
    }

}
