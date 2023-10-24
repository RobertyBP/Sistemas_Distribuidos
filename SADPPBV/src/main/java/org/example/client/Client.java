package org.example.client;

import org.example.senders.Cad;
import org.example.senders.Login;
import org.example.senders.Logout;
import org.example.system.Functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

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
            while (run) {
                try {
                    BufferedReader inputServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String serverResponse = "";
                    String token = "";


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
                    if (user.equals("admim")) {
                        System.out.println("1-Cadastrar \n2-Login \n 3-Logout\n 4-auto_Cadastrar \n0- Finalizar");
                        int op = Integer.parseInt(input.readLine());
                        switch (op) {
                            case 1:
                                cadastroUsuario(socket, token, user);
                                serverResponse = response();

                                System.out.println("serverResponse:" + serverResponse);
                                break;
                            case 2:
                                loginUsuario(socket);
                                break;
                            case 3:
                                logoutUsuario(token, socket);
                                serverResponse = response();
                                System.out.println("serverResponse:" + serverResponse);
                                break;
                            case 4:
                                auto_cadastroUsuario(socket, token, user);
                                serverResponse = response();
                                System.out.println("serverResponse:" + serverResponse);
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
                        System.out.println(serverResponse);
                        System.out.println("2-Login \n3-Logout\n0- Finalizar");
                        int op = Integer.parseInt(input.readLine());
                        switch (op) {
                            case 2:
                                loginUsuario(socket);
                                break;
                            case 3:
                                logoutUsuario(token, socket);
                                serverResponse = response();
                                System.out.println("serverResponse:" + serverResponse);
                                break;
                            case 0:
                                System.out.println("Encerrando");
                                run = false;
                                break;
                            default:
                                System.out.println("Tente uma opção válida da próxima vez.");
                                break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("ERROR: " + e);
                }
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

    public static void auto_cadastroUsuario(Socket socket, String token, String user) throws IOException {
        System.out.println("\tCadastro ");
        System.out.println("Nome:");
        String name = input.readLine();
        System.out.println("E-mail");
        String email = input.readLine();
        System.out.println("Senha:");
        String password = Functions.hash(input.readLine());

        Cad cad = new Cad(name, email, password, socket);
        cad.auto_Registrar();

    }

    public static void cadastroUsuario(Socket socket, String token, String user) throws IOException {
        System.out.println("\tCadastro ");
        System.out.println("Nome:");
        String name = input.readLine();
        System.out.println("E-mail");
        String email = input.readLine();
        System.out.println("Senha:");
        String password = Functions.hash(input.readLine());

        Cad cad = new Cad(name, email, password, user, token, socket);
        cad.registrar();

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

    private static void logoutUsuario(String token, Socket socket) throws IOException {
        try {
            System.out.println("\tLogout ");

            Logout.logout(token, socket);

        } catch (IOException e) {
            System.err.println("Couldn't get I/O: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String response() {
        String line = "";
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append(line);

        String serverResponse = jsonBuilder.toString();
        return serverResponse;
    }
}
