package com.controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private String add_message = "";
    private String delete_message = "";

    @GetMapping
    public ModelAndView goods(@RequestParam(required = false, defaultValue = "") String name, ModelMap model) throws JSONException {
        String viewName;
        if (LoginController.getCurrentUserRole().equals("ROLE_ADMIN")) {
            viewName = "goods";
        } else {
            viewName = "goodsForUser";
        }

        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            RestTemplate restTemplate = new RestTemplate();
            Object[] goods;

            try {
                if (name.equals("")) {
                    String url = "http://localhost:8080/wc/good/getAll";
                    ResponseEntity<Object[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Object[].class);
                    goods = responseEntity.getBody();
                } else {
                    String url = "http://localhost:8080/wc/good/getByName/" + name;
                    ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
                    goods = new Object[]{responseEntity.getBody()};
                }
                model.put("goods", goods);
                model.put("find_message", "");

            } catch (HttpClientErrorException exception) {
                model.put("find_message", "Such good not found");
            }
        }
        model.put("add_message", add_message);
        model.put("delete_message", delete_message);
        model.put("checkValidation", LoginController.tokenIsValidate());

        delete_message = "";

        return new ModelAndView(viewName);
    }

    @PostMapping("/add")
    public String addGood(@RequestParam String name, @RequestParam Float priority) throws JSONException {
        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url;

            add_message = "";

            if (!name.equals("") && priority != null) {
            try {
                url = "http://localhost:8080/wc/good/getByName/" + name;
                restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
                add_message = "Good with this name already exists";

            } catch (HttpClientErrorException exception) {
                    JSONObject json = new JSONObject();
                    json.put("name", name);
                    json.put("priority", priority);

                    HttpEntity<String> entity2 = new HttpEntity<>(json.toString(), headers);

                    url = "http://localhost:8080/wc/good/add";

                    restTemplate.postForObject(url, entity2, String.class);
            }
            } else {
                add_message = "Invalid parameters";
            }
        }

        return "redirect:/goods";
    }

    @PostMapping("/delete/{id}")
    public String deleteGood(@PathVariable("id") Long id) throws JSONException {
        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8080/wc/good/delete/" + id;

            try {
                restTemplate.postForObject(url, entity, String.class);
            } catch (HttpClientErrorException exception) {
                String exceptionMessage = exception.getMessage();

                exceptionMessage = exceptionMessage.substring(exceptionMessage.lastIndexOf("\"message\":\"") + 11);
                String delete = exceptionMessage.substring(exceptionMessage.lastIndexOf("\",\"path\""));
                exceptionMessage = exceptionMessage.replace(delete, "");

                delete_message = exceptionMessage;
            }
        }

        return "redirect:/goods";
    }
}
