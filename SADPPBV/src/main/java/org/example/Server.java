package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.crud.*;
import org.example.model.User;
import org.example.system.Functions;
import org.example.system.Session;
import org.example.system.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.example.system.Functions.validate;
import static org.example.system.Token.gerarToken;
import static org.example.system.Token.isValidToken;

public class Server {

    private static final List<User> allUsers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        iniciaVetor();
        lista();
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
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void iniciaVetor() {
        String senhaAdmin = Functions.hash("admin890");
        String senhaComum = Functions.hash("comum890");

        User admin = new User("1", true, "admin", "admin@email.com", senhaAdmin, gerarToken(1, true));
        User comum = new User("2", false, "comum", "comum@email.com", senhaComum, gerarToken(2, false));

        allUsers.add(comum);
        allUsers.add(admin);

    }

    public static void lista() {
        for (User user : allUsers) {
            System.out.println(user.toString());
        }
    }

    private static void clientRequest(Socket socket) throws IOException {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line = "";
            int i = 1;
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
                            if (dataNode != null) {
                                String id = dataNode.get("id") != null ? dataNode.get("id").asText() : null;
                                String token = dataNode.get("token") != null ? dataNode.get("token").asText() : null;
                                String nome = dataNode.get("name") != null ? dataNode.get("name").asText() : null;
                                String email = dataNode.get("email") != null ? dataNode.get("email").asText() : null;
                                String senha = dataNode.get("password") != null ? dataNode.get("password").asText() : null;
                                String tipo = dataNode.get("type") != null ? dataNode.get("type").asText() : null;

                                isAdmin = Token.isAdmin(token, allUsers);
                                System.out.println(isAdmin);
                                erro = false;
                                mensagem = "usuario cadastrado com sucesso";

                                if (compararEmail(email) != null) {
                                    erro = true;
                                    mensagem = " email ja cadastrado";
                                } else {
                                    User newUser = new User(id, true, nome, email, senha, token);
                                    allUsers.add(newUser);
                                }

                                Cadastro.responseCad(action, erro, mensagem, socket);

                                jsonBuilder.setLength(0);

                            } else {
                                erro = true;
                                mensagem = "usuario não cadastrado";

                            }
                            Cadastro.responseCad(action, erro, mensagem, socket);

                            jsonBuilder.setLength(0);
                            break;
                        case "autocadastro-usuario":
                            dataNode = jsonNode.get("data");
                            if (dataNode != null) {
                                String autoId = dataNode.get("id") != null ? dataNode.get("id").asText() : null;
                                String auToken = dataNode.get("token") != null ? dataNode.get("token").asText() : null;
                                String autoNome = dataNode.get("name") != null ? dataNode.get("name").asText() : null;
                                String autoEmail = dataNode.get("email") != null ? dataNode.get("email").asText() : null;
                                String autoSenha = dataNode.get("password") != null ? dataNode.get("password").asText() : null;

                                erro = false;
                                mensagem = "usuario cadastrado com sucesso";


                                if (compararEmail(autoEmail) != null) {
                                    erro = true;
                                    mensagem = " email ja cadastrado";
                                } else {

                                    User newAutoUser = new User(autoId, false, autoNome, autoEmail, autoSenha, auToken);
                                    allUsers.add(newAutoUser);
                                }

                                Cadastro.responseCadComum(action, erro, mensagem, socket);

                                jsonBuilder.setLength(0);
                            } else {
                                erro = true;
                                mensagem = "usuario não cadastrado";

                                Cadastro.responseCadComum(action, erro, mensagem, socket);

                                jsonBuilder.setLength(0);
                            }
                            break;
                        case "pedido-proprio-usuario":
                            dataNode = jsonNode.get("data");
                            if (dataNode != null) {
                                String pesquisaToken = dataNode.get("token") != null ? dataNode.get("token").asText() : null;
                                User pesq = new User();

                                for (User user : allUsers) {
                                    if (user.getToken() == pesquisaToken) {
                                        pesq = user;
                                        erro = false;
                                        mensagem = "sucesso!";
                                    }else{
                                        erro = true;
                                        mensagem = "usuario não encontrado";
                                    }
                                }
                                Listar.pesquisarResponce(action, erro, mensagem, pesq, socket);

                                jsonBuilder.setLength(0);

                            } else {
                                erro = true;
                                mensagem = "campo vasio";
                            }
                            Listar.listarResponceErro(action, erro, mensagem, socket);

                            jsonBuilder.setLength(0);
                            break;
                        case "listar-usuarios":
                            dataNode = jsonNode.get("data");
                            if (dataNode != null) {
                                String listToken = dataNode.get("token") != null ? dataNode.get("token").asText() : null;
                                if (Token.isAdmin(listToken, allUsers)) {
                                    if (isValidToken(listToken)) {
                                        erro = false;
                                        mensagem = "sucesso!";
                                        lista();
                                        Listar.listarResponce(action, erro, mensagem, allUsers, socket);

                                        jsonBuilder.setLength(0);
                                    } else {
                                        erro = true;
                                        mensagem = "token invalido";
                                        Listar.listarResponceErro(action, erro, mensagem, socket);

                                        jsonBuilder.setLength(0);
                                    }
                                } else {
                                    erro = true;
                                    mensagem = "usuario não tem privilegio de adm";
                                }
                            } else {
                                erro = true;
                                mensagem = "campo vasio";
                            }
                            Listar.listarResponceErro(action, erro, mensagem, socket);

                            jsonBuilder.setLength(0);
                            break;

                        case "login":
                            dataNode = jsonNode.get("data");
                            if (dataNode != null) {
                                String loginEmail = dataNode.get("email") != null ? dataNode.get("email").asText() : null;
                                String loginPassword = dataNode.get("password") != null ? dataNode.get("password").asText() : null;

                                // Obter o usuário que está tentando fazer login

                                User loggedInUser = new User();
                                loggedInUser = compararEmail(loginEmail);

                                if (loggedInUser != null) System.out.println("Usuário encontrado.");
                                // Verificar se as credenciais são válidas

                                if (loggedInUser != null && validate(loginEmail, loginPassword, loggedInUser)) {
                                    // Login bem-sucedido
                                    erro = false;
                                    mensagem = "logado com sucesso";
                                    // Criar o token
                                    String token = loggedInUser.getToken();
                                    // Criar a sessão
                                    Session.createSession(token, loggedInUser);

                                    Login.loginResponce1(action, erro, mensagem, socket, token);

                                    jsonBuilder.setLength(0);
                                } else {
                                    erro = true;
                                    mensagem = "falha no login";

                                    Login.loginResponce2(action, erro, mensagem, socket);

                                    jsonBuilder.setLength(0);
                                }
                            } else {
                                erro = true;
                                mensagem = "campo vasio";

                                Login.loginResponce2(action, erro, mensagem, socket);

                                jsonBuilder.setLength(0);
                            }
                            break;
                        case "logout-usuario":
                            dataNode = jsonNode.get("data");
                            if (dataNode != null) {
                                String logoutToken = dataNode.get("token") != null ? dataNode.get("token").asText() : null;

                                if (isValidToken(logoutToken)) {
                                    Session.removeSession(logoutToken);
                                    erro = false;
                                    mensagem = "Logout efetuado com sucesso!";

                                    Logout.logoutResponce(action, erro, mensagem, socket);

                                    jsonBuilder.setLength(0);
                                }
                            } else {
                                erro = true;
                                mensagem = "campo vasio";

                                Logout.logoutResponce(action, erro, mensagem, socket);

                                jsonBuilder.setLength(0);
                            }
                            break;
                        case "excluir-usuario":
                            dataNode = jsonNode.get("data");
                            if (dataNode != null) {
                                String deleteToken = dataNode.get("token") != null ? dataNode.get("token").asText() : null;
                                String deleteID = dataNode.get("user-id") != null ? dataNode.get("user-id").asText() : null;
                                if (isValidToken(deleteToken)) {
                                    for (User user : allUsers) {
                                        if (user.getId() == deleteID) {
                                            allUsers.remove(user);
                                            erro = false;
                                            mensagem = "deletado com sucesso!";

                                            Deletar.deletarResponce(action, erro, mensagem, socket);

                                            jsonBuilder.setLength(0);
                                        } else {
                                            erro = true;
                                            mensagem = "falha na deleção";

                                            Deletar.deletarResponce(action, erro, mensagem, socket);

                                            jsonBuilder.setLength(0);
                                        }
                                    }
                                }

                            } else {
                                erro = true;
                                mensagem = "campo vasio";

                                Logout.logoutResponce(action, erro, mensagem, socket);

                                jsonBuilder.setLength(0);
                            }
                            break;
                        case "excluir-proprio-usuario":
                            dataNode = jsonNode.get("data");
                            if (dataNode != null) {
                                String deleteToken = dataNode.get("token") != null ? dataNode.get("token").asText() : null;
                                String deleteEmail = dataNode.get("email") != null ? dataNode.get("email").asText() : null;
                                String deletePassword = dataNode.get("password") != null ? dataNode.get("password").asText() : null;

                                if (isValidToken(deleteToken)) {
                                    for (User user : allUsers) {
                                        if (user != null && validate(deleteEmail, deletePassword, user)) {

                                            erro = false;
                                            mensagem = "deleção efetuada com sucesso";

                                            allUsers.remove(user);

                                            Deletar.deletarResponce(action, erro, mensagem, socket);

                                            jsonBuilder.setLength(0);
                                        } else {
                                            erro = true;
                                            mensagem = "falha na deleção";

                                            Deletar.deletarResponce(action, erro, mensagem, socket);

                                            jsonBuilder.setLength(0);
                                        }
                                    }


                                }

                            } else {
                                erro = true;
                                mensagem = "campo vasio";

                                Logout.logoutResponce(action, erro, mensagem, socket);

                                jsonBuilder.setLength(0);
                            }
                            break;
                        default:
                            System.out.println("Opção inválida");
                            break;

                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    public static User compararEmail(String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}