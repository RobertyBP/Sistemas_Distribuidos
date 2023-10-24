package org.example.Client_Server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;
import org.example.system.Functions;
import org.example.system.Session;
import org.example.system.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.system.Functions.validate;
import static org.example.system.Token.isAdmin;
import static org.example.system.Token.isValidToken;

public class Server {

    private static final List<User> allUsers = new ArrayList<>();



    public static void main(String[] args) throws IOException {
        String inicio=iniciaVetor();
        System.out.println(inicio);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("IP do servidor: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("informe a porta do servidor:");
        int serverPort = Integer.parseInt(in.readLine());
        in.close();
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Servidor esperando por conexões...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());
                new Thread(() -> {
                    try {
                        clientRequest(socket);
                        socket.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }

                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String iniciaVetor() {

        String senhaAdmin = Functions.hash("admin890");
        User admin = new User(true, "admin", "admin@email.com",senhaAdmin);
        allUsers.add(admin);
        return  admin.toString();

    }

    private static void clientRequest(Socket socket) throws IOException {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line = "";

            while ((line = inFromClient.readLine()) != null) {
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append(line);

                if (line.endsWith("}")) {
                    String receivedJson = jsonBuilder.toString();
                    System.out.println("JSON");
                    System.out.println(receivedJson);

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(receivedJson);
                    String action = jsonNode.get("action").asText();
                    JsonNode dataNode;

                    boolean isAdmin = false;
                    boolean erro = false;
                    String mensagem = "";
                    switch (action) {
                        case "cadastro-usuario":
                            dataNode = jsonNode.get("data");
                            String token = dataNode.get("token").asText();
                            String nome = dataNode.get("nome").asText();
                            String email = dataNode.get("email").asText();
                            String senha = dataNode.get("password").asText();
                            String tipo = dataNode.get("tipo").asText();

                            if (tipo.equals("admin")) isAdmin = isAdmin(token);
                            System.out.println(isAdmin(token));

                            erro = false;
                            mensagem = "usuario cadastrado com sucesso";

                            List<User> users = new ArrayList<>();


                            if (Functions.compararEmail(email) != null) {
                                erro = true;
                                mensagem = " email ja cadastrado";
                            } else {
                                if (tipo.equals("Admin") && isAdmin == false) {
                                    erro = true;
                                    mensagem = "o usuario não possui privilegio de admin";
                                } else {
                                    User newUser = new User(isAdmin, nome, email, senha);
                                    users.add(newUser);
                                }
                            }
                            allUsers.addAll(users);

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
                            outToClientCadastro.println(jsonResponseCadastro + "\r\n");
                            outToClientCadastro.flush();

                            jsonBuilder.setLength(0);
                            break;
                        case "autocadastro-usuario":
                            dataNode = jsonNode.get("data");
                            String autoNome = dataNode.get("nome").asText();
                            String autoEmail = dataNode.get("email").asText();
                            String autoSenha = dataNode.get("password").asText();

                            erro = false;
                            mensagem = "usuario cadastrado com sucesso";

                            List<User> autoUsers = new ArrayList<>();


                            if (Functions.compararEmail(autoEmail) != null) {
                                erro = true;
                                mensagem = " email ja cadastrado";
                            } else {
                                    User newAutoUser = new User(autoNome, autoEmail, autoSenha);
                                    autoUsers.add(newAutoUser);
                            }
                            allUsers.addAll(autoUsers);

                            Map<String, Object> jsonMapAutoCadastro = new HashMap<>();
                            jsonMapAutoCadastro.put("action", action);

                            Map<String, String> dataMapAutoCadastro = new HashMap<>();
                            dataMapAutoCadastro.put("error", String.valueOf(erro));
                            dataMapAutoCadastro.put("message", mensagem);
                            jsonMapAutoCadastro.put("data", dataMapAutoCadastro);

                            // Enviar o JSON para o cliente
                            PrintWriter outToClientAutoCadastro = new PrintWriter(socket.getOutputStream(), true);
                            ObjectMapper objAutoMapper = new ObjectMapper();
                            String jsonResponseAutoCadastro = objAutoMapper.writeValueAsString(jsonMapAutoCadastro);

                            System.out.println("Enviando JSON...");
                            System.out.println(jsonResponseAutoCadastro);
                            outToClientAutoCadastro.println(jsonResponseAutoCadastro + "\r\n");
                            outToClientAutoCadastro.flush();

                            jsonBuilder.setLength(0);
                            break;
                        case "login":
                            //login
                            dataNode = jsonNode.get("data");
                            String loginEmail = dataNode.get("email").asText();
                            String loginPassword = dataNode.get("password").asText();

                            // Obter o usuário que está tentando fazer login
                            User loggedInUser = Functions.compararEmail(loginEmail);
                            if (loggedInUser != null) System.out.println("Usuário encontrado.");
                            // Verificar se as credenciais são válidas

                            if (loggedInUser != null && validate(loginEmail, loginPassword)) {
                                // Login bem-sucedido
                                erro = false;
                                mensagem = "logado com sucesso";
                                // Criar o token
                                token = Token.gerarToken(1, loggedInUser.getIsAdmin());
                                // Criar a sessão
                                Session.createSession(token, loggedInUser);

                                // Criar o JSON de resposta
                                Map<String, Object> jsonMapLogin = new HashMap<>();
                                jsonMapLogin.put("action", action);
                                jsonMapLogin.put("error", erro);
                                jsonMapLogin.put("message", mensagem);

                                Map<String, String> dataMapLogin = new HashMap<>();
                                dataMapLogin.put("token", token);
                                jsonMapLogin.put("data", dataMapLogin);

                                // Enviar o JSON para o cliente
                                PrintWriter outToClientLogin = new PrintWriter(socket.getOutputStream(), true);
                                ObjectMapper objMapperLogin = new ObjectMapper();
                                String jsonResponseLogin = objMapperLogin.writeValueAsString(jsonMapLogin);

                                System.out.println("Enviando JSON...");
                                System.out.println(jsonResponseLogin);
                                outToClientLogin.println(jsonResponseLogin + "\r\n");
                                outToClientLogin.flush();
                            } else {
                                // Login falhou
                                erro = true;
                                mensagem = "falha no login";

                                Map<String, Object> jsonMapLogin = new HashMap<>();
                                jsonMapLogin.put("action", action);
                                jsonMapLogin.put("error", erro);
                                jsonMapLogin.put("message", mensagem);

                                Map<String, String> dataMapLogin = new HashMap<>();
                                jsonMapLogin.put("data", dataMapLogin);

                                // Enviar o JSON para o cliente
                                PrintWriter outToClientLogin = new PrintWriter(socket.getOutputStream(), true);
                                ObjectMapper objMapperLogin = new ObjectMapper();
                                String jsonResponseLogin = objMapperLogin.writeValueAsString(jsonMapLogin);

                                System.out.println("Enviando JSON...");
                                System.out.println(jsonResponseLogin);
                                outToClientLogin.println(jsonResponseLogin + "\r\n");
                                outToClientLogin.flush();
                            }
                            // Limpe o StringBuilder para a próxima solicitação
                            jsonBuilder.setLength(0);
                            break;
                        case "logout-usuario":
                            dataNode = jsonNode.get("data");
                            String logoutToken = dataNode.get("token").asText();
                            if(isValidToken(logoutToken)) Session.removeSession(logoutToken);

                            erro = false;
                            mensagem = "Logout efetuado com sucesso!";

                            // Criar o JSON para o logout
                            Map<String, Object> jsonMapLogout = new HashMap<>();
                            jsonMapLogout.put("action", action);

                            Map<String, String> dataMapLogout = new HashMap<>();
                            dataMapLogout.put("error", String.valueOf(erro));
                            dataMapLogout.put("message", mensagem);
                            jsonMapLogout.put("data", dataMapLogout);

                            // Enviar o JSON para o cliente
                            PrintWriter outToClientLogout = new PrintWriter(socket.getOutputStream(), true);
                            ObjectMapper objMap = new ObjectMapper();
                            String jsonResponseLogout = objMap.writeValueAsString(jsonMapLogout);

                            System.out.println("Enviando JSON...");
                            System.out.println(jsonResponseLogout);
                            outToClientLogout.println(jsonResponseLogout + "\r\n");
                            outToClientLogout.flush();

                            jsonBuilder.setLength(0);
                            break;

                        default:
                            System.out.println("Opção inválida");
                            break;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            //outToClient.close();
        }
    }
}