package com.controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/login")
public class LoginController {

    public static String CURRENT_TOKEN;

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password, ModelMap model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject json1 = new JSONObject();
            json1.put("username", username);
            json1.put("password", password);

            HttpEntity<String> request = new HttpEntity<>(json1.toString(), headers);

            String url = "http://localhost:8080/wc/auth/signin";

            RestTemplate restTemplate = new RestTemplate();
            String authResponse = restTemplate.postForObject(url, request, String.class);

            JSONObject json2 = new JSONObject(authResponse);
            CURRENT_TOKEN = json2.getString("token");

            return "redirect:/goods";

        } catch (Exception exception) {
            String message = "Authentication error\n" +
                    "Incorrect username or password";
            model.put("message", message);

            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(ModelMap model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<>("parameters", headers);

        String url = "http://localhost:8080/wc/auth/logout";
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);

        CURRENT_TOKEN = null;

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return "login";
        } else {
            throw new RuntimeException("Error of logout");
        }
    }

    public static boolean tokenIsValidate() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);

        String url = "http://localhost:8080/wc/auth/checkToken/" + CURRENT_TOKEN;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url, request, String.class);
        JSONObject json = new JSONObject(response);

        return json.getBoolean("validate");
    }

    public static String getCurrentUserRole() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);

        String url = "http://localhost:8080/wc/auth/getRoleCurrentUser/" + CURRENT_TOKEN;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getBody();
    }
}
