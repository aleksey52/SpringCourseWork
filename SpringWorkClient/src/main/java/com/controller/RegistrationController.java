package com.controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(@RequestParam String username, @RequestParam String password, ModelMap model) throws JSONException {
        String message;

        if (!username.equals("") && !password.equals("")) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                JSONObject json = new JSONObject();
                json.put("username", username);
                json.put("password", password);

                HttpEntity<String> request = new HttpEntity<String>(json.toString(), headers);

                String url = "http://localhost:8080/wc/auth/registration";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForObject(url, request, String.class);

                return "redirect:/login";

            } catch (Exception exception) {
                message = "Registration error\n" +
                        "A user with this name already exists";
                model.put("message", message);
            }
        } else {
            message = "Invalid parameters";
        }

        model.put("message", message);

        return "registration";
    }
}
