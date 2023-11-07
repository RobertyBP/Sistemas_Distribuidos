package org.example.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listar {
    public static void listarResponce(String action, Boolean erro, String mensagem, List<User> allUsers, Socket socket) throws IOException {
        Map<String, Object> jsonMapList = new HashMap<>();
        jsonMapList.put("action", action);

        List<Map<String, Object>> userList = new ArrayList<>();
        Map<String, Object> dataMapList = new HashMap<>();

        for (User user : allUsers) {
            if (user.getIsAdmin()){
                dataMapList.put("type", "admin");
            }else {
                dataMapList.put("type", "comum");
            }
                dataMapList.put("error", String.valueOf(erro));
                dataMapList.put("message", mensagem);
                dataMapList.put("id", user.getId());
                dataMapList.put("name", user.getName());
                dataMapList.put("email", user.getEmail());
                userList.add(dataMapList);

        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("users", userList);
        jsonMapList.put("data", dataMap);

        PrintWriter outToClientList = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objMap = new ObjectMapper();
        String jsonResponseList = objMap.writeValueAsString(jsonMapList);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponseList);
        outToClientList.println(jsonResponseList);
        outToClientList.flush();


    }
    public static void pesquisarResponce(String action, Boolean erro, String mensagem, User user, Socket socket) throws IOException {

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("action", action);

        Map<String, String> dataMap = new HashMap<>();

        if(user.getIsAdmin()){
                dataMap.put("error", String.valueOf(erro));
                dataMap.put("message", mensagem);

                dataMap.put("id", user.getId());
                dataMap.put("name", user.getName());
                dataMap.put("type", "admin");
                dataMap.put("email", user.getEmail());

                jsonMap.put("data", dataMap);
            }else{
                dataMap.put("error", String.valueOf(erro));
                dataMap.put("message", mensagem);

                dataMap.put("id", user.getId());
                dataMap.put("name", user.getName());
                dataMap.put("type", "comum");
                dataMap.put("email", user.getEmail());

                jsonMap.put("data", dataMap);
            }

        PrintWriter outToClient = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objMap = new ObjectMapper();
        String jsonResponse = objMap.writeValueAsString(jsonMap);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponse);
        outToClient.println(jsonResponse);
        outToClient.flush();
    }

    public static void listarResponceErro(String action, Boolean erro, String mensagem, Socket socket) throws IOException {
        Map<String, Object> jsonMapList = new HashMap<>();
        jsonMapList.put("action", action);

        Map<String, String> dataMapList = new HashMap<>();
        dataMapList.put("error", String.valueOf(erro));
        dataMapList.put("message", mensagem);
        jsonMapList.put("data", dataMapList);

        PrintWriter outToClientList = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objMap = new ObjectMapper();
        String jsonResponseList = objMap.writeValueAsString(jsonMapList);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponseList);
        outToClientList.println(jsonResponseList);
        outToClientList.flush();
    }

    public static void listar(Socket socket, String token) throws IOException {
        Map<String, Object> jsonMapPesquisar = new HashMap<>();
        jsonMapPesquisar.put("action", "listar-usuarios");

        Map<String, String> dataMapPesquisar = new HashMap<>();
        dataMapPesquisar.put("token", token);
        jsonMapPesquisar.put("data", dataMapPesquisar);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(jsonMapPesquisar);

        System.out.println("Enviando JSON...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();

    }
    public static void pesquisar(String token, Socket socket) throws IOException {

        Map<String, Object> jsonMapPesquisar = new HashMap<>();
        jsonMapPesquisar.put("action", "pedido-proprio-usuario");

        Map<String, String> dataMapPesquisar = new HashMap<>();
        dataMapPesquisar.put("token", token);
        jsonMapPesquisar.put("data", dataMapPesquisar);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(jsonMapPesquisar);

        System.out.println("Enviando JSON...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();

    }
}
