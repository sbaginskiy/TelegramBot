package com.example.demo.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class BotService {

    private String accessToken;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public boolean isNewUser(Long chatId) {
        setAccessToken();
        return getListChatIds().stream().map(Map.class::cast).map(it -> ((Map) it).get("data")).map(Map.class::cast)
                .map(it -> ((Map) it).get("chatId"))
                .noneMatch(it -> it.equals(chatId.toString()));
    }

    private List getListChatIds() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity request = new HttpEntity<>(headers);
        String url = "https://cimdemo.stage.xm-online.com/entity/api/xm-entities?typeKey=CHAT-ID&page=0&size=10000&sort=id";

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);
        return response.getBody();
    }


    private void setAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic d2ViYXBwOndlYmFwcA==");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", "cimdemo");
        map.add("password", "P@ssw0rd");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("https://cimdemo.stage.xm-online.com/uaa/oauth/token", request, String.class);
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            this.accessToken = root.path("access_token").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createXmEntityChatId(Message message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        JSONObject dataJsonObject = new JSONObject();
        dataJsonObject.put("chatId", message.getChatId().toString());

        JSONObject chatJsonObject = new JSONObject();
        chatJsonObject.put("name", message.getFrom().getUserName());
        chatJsonObject.put("key", UUID.randomUUID().toString());
        chatJsonObject.put("typeKey", "CHAT-ID");
        chatJsonObject.put("data", dataJsonObject);
        HttpEntity<String> request2 = new HttpEntity<String>(chatJsonObject.toString(), headers);
        restTemplate.postForEntity("https://cimdemo.stage.xm-online.com/entity/api/xm-entities", request2, String.class);
    }
}

