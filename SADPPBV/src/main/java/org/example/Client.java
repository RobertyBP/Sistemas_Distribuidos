package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.crud.*;
import org.example.system.Functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.example.crud.Response.responseLogin;

public class Client {
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {

        System.out.println("IP do servidor:");
        String ip = input.readLine();
        System.out.println("Porta do servidor:");
        int port = Integer.parseInt(input.readLine());

        System.out.println("Tentando conexão com o host " +
                ip + " na porta " + port + ".");

        try (Socket socket = new Socket(ip, port)) {
            boolean run = true;
            System.out.println("Qual tipo de usuário vc é:\n1- para administrador\t2-para usuário comum");
            String user = input.readLine();
            int i = 1;
            while (run) {
                try {
                    BufferedReader inputServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String serverResponse = "";
                    boolean escolha = true;
                    while (escolha) {
                        switch (user) {
                            case "1":
                                user = "admim";
                                escolha = false;
                                break;
                            case "2":
                                user = "comum";
                                escolha = false;
                                break;
                            default:
                                System.out.println("Tente novamente.");
                                System.out.println("Qual tipo de usuário vc é:\n1- para administrador\t2-para usuário comum");
                                user = input.readLine();
                                escolha = true;
                        }
                    }
                    while (user.equals("admim")) {
                        loginUsuario(socket);
                        JsonNode jsonNode = responseLogin(inputServer);
                        JsonNode dataNode;
                        dataNode = jsonNode.get("data");
                        String tokenAdm = "";
                        String erro = "true";
                        if (dataNode != null) {
                            tokenAdm = dataNode.get("token") != null ? dataNode.get("token").asText() : null;
                            erro = dataNode.get("error") != null ? dataNode.get("error").asText() : null;

                            if (erro.equals("false")) {
                                System.out.println("1-Cadastrar \n2-auto_Cadastrar \n3-listar\n4-pesquisar\n5-Editar\n6-Deletar\n7-Logout\n0- Finalizar");
                                int op = Integer.parseInt(input.readLine());
                                switch (op) {
                                    case 1:
                                        cadastroUsuario(socket, tokenAdm, user);
                                        Response.response(inputServer);
                                        break;
                                    case 2:
                                        cadastroUsuario(socket, tokenAdm, "comum");
                                        Response.response(inputServer);
                                        break;
                                    case 3:
                                        listar(socket, tokenAdm);
                                        Response.response(inputServer);
                                        break;
                                    case 4:
                                        pesquisar(socket, tokenAdm);
                                        Response.response(inputServer);
                                        break;
                                    case 5:
                                        editarUsuario(tokenAdm, socket, user);
                                        Response.response(inputServer);
                                        break;
                                    case 6:
                                        deleteUsuario(tokenAdm, socket, user);
                                        Response.response(inputServer);
                                        break;
                                    case 7:
                                        Logout.logoutRequest(tokenAdm, socket);
                                        tokenAdm = "";
                                        user = "0";
                                        break;

                                    case 0:
                                        System.out.println("Encerrando");
                                        run = false;
                                        break;
                                    default:
                                        System.out.println("Tente uma opção válida da próxima vez.");
                                        break;
                                }
                            } else {
                                break;
                            }
                        }
                    }

                    while (user.equals("comum")) {
                        loginUsuario(socket);
                        JsonNode jsonNode = responseLogin(inputServer);
                        JsonNode dataNode;
                        dataNode = jsonNode.get("data");
                        String token = "";
                        String erro = "";
                        if (dataNode != null) {
                            token = dataNode.get("token") != null ? dataNode.get("token").asText() : null;
                            erro = dataNode.get("error") != null ? dataNode.get("error").asText() : null;

                            if (erro.equals("false")) {

                                System.out.println("1-autocadastro\n2-pesquisar\n3-Logout\n0- Finalizar");

                                int op = Integer.parseInt(input.readLine());
                                switch (op) {
                                    case 1:
                                        cadastroUsuario(socket, token, user);
                                        Response.response(inputServer);
                                        break;
                                    case 2:
                                        pesquisar(socket, token);
                                        Response.response(inputServer);
                                        user = "0";
                                        break;
                                    case 3:
                                        Logout.logoutRequest(token, socket);
                                        user = "0";
                                        break;
                                    case 0:
                                        System.out.println("Encerrando");
                                        run = false;
                                        break;
                                    default:
                                        System.out.println("Tente uma opção válida da próxima vez.");
                                        break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("ERROR: " + e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                i++;
            }

            // Fecha o input após o loop
            input.close();
        } catch (UnknownHostException e) {
            System.err.println("Server host desconhecido: " + ip);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Não foi possível se conector com: " + ip);
            System.exit(1);
        }
    }

    public static void pesquisar(Socket socket, String token) throws IOException {
        Listar.pesquisar(token, socket);
    }

    public static void listar(Socket socket, String token) throws IOException {
            Listar.listar(socket, token);

    }

    public static void cadastroUsuario(Socket socket, String token, String user) throws IOException {
        if (user.equals("admin")) {
            System.out.println("\tCadastro ");
            System.out.println("Nome:");
            String name = input.readLine();
            System.out.println("E-mail");
            String email = input.readLine();
            System.out.println("Senha:");
            String password = Functions.hash(input.readLine());

            Cadastro crud = new Cadastro(name, email, password, user, token, socket);
            crud.registrar();
        } else {
            System.out.println("\tCadastro ");
            System.out.println("Nome:");
            String name = input.readLine();
            System.out.println("E-mail");
            String email = input.readLine();
            System.out.println("Senha:");
            String password = Functions.hash(input.readLine());

            Cadastro crud = new Cadastro(name, email, password, socket);
            crud.auto_Registrar();
        }
    }

    public static void loginUsuario(Socket socket) throws IOException {

        System.out.println("\tLogin");

        System.out.println("E-mail:");
        String emailLogin = input.readLine();
        while (!Functions.isValidEmail(emailLogin)) {
            System.out.println("FOrmato de email inválido. Insira um e-mail válido.");
            emailLogin = input.readLine();
        }
        System.out.println("Senha:");
        String senhaLogin = Functions.hash(input.readLine());

        Login login = new Login(emailLogin, senhaLogin, socket);
        login.loginRequest();
    }

    private static void editarUsuario(String token, Socket socket, String user) throws IOException {
        if (user.equals("admin")) {
            System.out.println("id:");
            String id = input.readLine();
            System.out.println("Nome:");
            String name = input.readLine();
            System.out.println("E-mail");
            String email = input.readLine();
            System.out.println("Senha:");
            String password = Functions.hash(input.readLine());

            Editar.editar(token, name, email, password, user, id, socket);
        } else {
            System.out.println("id:");
            String id = input.readLine();
            System.out.println("Nome:");
            String name = input.readLine();
            System.out.println("E-mail");
            String email = input.readLine();
            String password = null;

            Editar.editarComum(id, name, email, password, socket);
        }
    }

    public static void deleteUsuario(String token, Socket socket, String user) throws IOException {
        if (user.equals("admin")) {
            System.out.println("id:");
            String id = input.readLine();
            Deletar.deletar(token, id, socket);
        } else {
            System.out.println("E-mail");
            String email = input.readLine();
            System.out.println("Senha:");
            String password = Functions.hash(input.readLine());
            Deletar.deletarComum(token, email, password, socket);
        }

    }


}
