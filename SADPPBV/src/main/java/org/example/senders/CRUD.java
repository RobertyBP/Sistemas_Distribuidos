package org.example.senders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRUD {
    String name;
    String email;
    String password;
    String type;
    String token;
    Socket socket;

    public CRUD(String name, String email, String password, String type, String token, Socket socket) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.token = token;
        this.socket = socket;
    }

    public CRUD(String name, String email, String password, Socket socket) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.socket = socket;
    }
    public static void responseCad(String action, Boolean erro, String mensagem, Socket socket) throws IOException {

        Map<String, Object> jsonMapCadastro = new HashMap<>();
        jsonMapCadastro.put("action", action);

        Map<String, String> dataMapCadastro = new HashMap<>();
        dataMapCadastro.put("error", String.valueOf(erro));
        dataMapCadastro.put("message", mensagem);
        jsonMapCadastro.put("data", dataMapCadastro);

        // Enviar o JSON para o cliente
        PrintWriter outToClientCadastro = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objMapper = new ObjectMapper();
        String jsonResponseCadastro = objMapper.writeValueAsString(jsonMapCadastro);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponseCadastro);
        outToClientCadastro.println(jsonResponseCadastro);
        outToClientCadastro.flush();

    }
    public void registrar() throws IOException {

        Map<String, Object> jsonMapCadastro = new HashMap<>();
        jsonMapCadastro.put("action", "cadastro-usuario");

        Map<String, String> dataMapCadastro = new HashMap<>();
        dataMapCadastro.put("name", name);
        dataMapCadastro.put("email", email);
        dataMapCadastro.put("password", password);
        dataMapCadastro.put("token", token);
        dataMapCadastro.put("type", type);
        jsonMapCadastro.put("data", dataMapCadastro);

        // Enviar o JSON para o servidor
        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(jsonMapCadastro);

        System.out.println("Enviando JSON...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }
    public void auto_Registrar() throws IOException {

        Map<String, Object> jsonMapCadastro = new HashMap<>();
        jsonMapCadastro.put("action", "autocadastro-usuario");

        Map<String, String> dataMapCadastro = new HashMap<>();
        dataMapCadastro.put("name", name);
        dataMapCadastro.put("email", email);
        dataMapCadastro.put("password", password);
        jsonMapCadastro.put("data", dataMapCadastro);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(jsonMapCadastro);

        System.out.println("Enviando JSON...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }
    /*public User pesquisar(){

    }*/
    public static void listarResponce1(String action, Boolean erro, String mensagem, List<User> allUsers, Socket socket)throws  IOException{
        Map<String, Object> jsonMapList = new HashMap<>();
        jsonMapList.put("action", action);

        Map<String, String> dataMapList = new HashMap<>();
        dataMapList.put("error", String.valueOf(erro));
        dataMapList.put("message", mensagem);
        dataMapList.put("user", allUsers.toString());
        jsonMapList.put("data", dataMapList);

        PrintWriter outToClientList = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objMap = new ObjectMapper();
        String jsonResponseList = objMap.writeValueAsString(jsonMapList);

        System.out.println("Enviando JSON...");
        System.out.println(jsonResponseList);
        outToClientList.println(jsonResponseList);
        outToClientList.flush();
    }
    public static void listarResponce2(String action, Boolean erro, String mensagem, Socket socket)throws  IOException{
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
    public static void listarRequest(String token, Socket socket) throws IOException {

        Map<String, Object> jsonMapLogout = new HashMap<>();
        jsonMapLogout.put("action", "listar-usuarios");

        Map<String, String> dataMapLogout = new HashMap<>();
        dataMapLogout.put("token", token);
        jsonMapLogout.put("data", dataMapLogout);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest= objectMapper.writeValueAsString(jsonMapLogout);

        System.out.println("Enviando JSON para o servidor...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }
}
