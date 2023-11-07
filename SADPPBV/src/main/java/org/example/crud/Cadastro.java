package org.example.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.system.Token;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cadastro {
    String name;
    String email;
    String password;
    String type;
    String token;
    Socket socket;
    Random aleatorio = new Random();



    public Cadastro(String name, String email, String password, String type, String token, Socket socket) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.token = token;
        this.socket = socket;
    }

    public Cadastro(String name, String email, String password, Socket socket) {
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
        int valor = aleatorio.nextInt(300) + 1;
        Map<String, Object> jsonMapCadastro = new HashMap<>();
        jsonMapCadastro.put("action", "cadastro-usuario");

        Map<String, String> dataMapCadastro = new HashMap<>();
        dataMapCadastro.put("id", String.valueOf(valor));
        dataMapCadastro.put("name", name);
        dataMapCadastro.put("email", email);
        dataMapCadastro.put("password", password);
        dataMapCadastro.put("token", Token.gerarToken(valor, true));
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
    public static void responseCadComum(String action, Boolean erro, String mensagem, Socket socket) throws IOException {

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

    public void auto_Registrar() throws IOException {
        int valor = aleatorio.nextInt(300) + 1;

        Map<String, Object> jsonMapCadastro = new HashMap<>();
        jsonMapCadastro.put("action", "autocadastro-usuario");

        Map<String, String> dataMapCadastro = new HashMap<>();
        dataMapCadastro.put("id", String.valueOf(valor));
        dataMapCadastro.put("name", name);
        dataMapCadastro.put("email", email);
        dataMapCadastro.put("password", password);
        dataMapCadastro.put("token", Token.gerarToken(valor, false));

        jsonMapCadastro.put("data", dataMapCadastro);

        PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(jsonMapCadastro);

        System.out.println("Enviando JSON...");
        System.out.println(jsonRequest);
        outToServer.println(jsonRequest);
        outToServer.flush();
    }
}
