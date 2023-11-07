package org.example.crud;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Response {
    public static JsonNode responseLogin(BufferedReader inFromServer) {
        try {
            String response = inFromServer.readLine();
            System.out.println(response.toString());

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append(response);

            String receivedJson = jsonBuilder.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(receivedJson);

            return jsonNode;

        } catch (IOException e) {
            System.err.println("Error reading server response: " + e.getMessage());
            return null;
        }
    }
    public static  void responseList(BufferedReader inFromServer) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonResponse = objectMapper.readValue(inFromServer, new TypeReference<Map<String, Object>>() {});

        Map<String, Object> dataMap = (Map<String, Object>) jsonResponse.get("data");
        List<Map<String, Object>> userList = (List<Map<String, Object>>) dataMap.get("users");

        for (Map<String, Object> userMap : userList) {
            Integer id = (Integer) userMap.get("id");
            String name = (String) userMap.get("name");
            String type = (String) userMap.get("type");
            String email = (String) userMap.get("email");
            System.out.println("id: "+id+"name: "+name+"tipo: "+type+"email: "+email);
        }
    }
    public static void response(BufferedReader inFromServer) throws IOException {
        String response = inFromServer.readLine();
        System.out.println(response);
    }


}
