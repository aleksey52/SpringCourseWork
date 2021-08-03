package com.controller;

import com.google.gson.Gson;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/warehouse")
public class WarehousesController {

    private String add_message = "";

    @GetMapping
    public String warehouses() {
        return "chooseWarehouse";
    }

    @GetMapping("/{num}")
    public ModelAndView warehouse(@PathVariable("num") Long num, @RequestParam(required = false, defaultValue = "") String name, ModelMap model) throws JSONException {
        String viewName;
        if (LoginController.getCurrentUserRole().equals("ROLE_ADMIN")) {
            viewName = "warehouses";
        } else {
            viewName = "warehousesForUser";
        }

        model.put("num", num);

        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object[]> responseEntity;
            Object[] warehouses;
            String url;

            try {
                if (name.equals("")) {
                    url = "http://localhost:8080/wc/warehouses/getAll/" + num;
                } else {
                    url = "http://localhost:8080/wc/warehouses/getByName/" + num + "/" + name;
                }
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Object[].class);
                warehouses = responseEntity.getBody();

                model.put("warehouses", warehouses);
                model.put("find_message", "");

            } catch (HttpClientErrorException exception) {
                model.put("find_message", "The entry with the specified good is not found");
            }
        }
        model.put("add_message", add_message);
        model.put("checkValidation", LoginController.tokenIsValidate());

        return new ModelAndView(viewName);
    }

    @PostMapping("/add/{num}")
    public String addWarehouse(@PathVariable("num") Long num, @RequestParam String nameGood, @RequestParam Long good_count) throws JSONException {
        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url;

            add_message = "";

            try {
                if (!nameGood.equals("") && good_count != null) {
                    url = "http://localhost:8080/wc/good/getByName/" + nameGood;
                    ResponseEntity<Object> goodResponseEntity= restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
                    Object good = goodResponseEntity.getBody();

                    Gson gson = new Gson();
                    String jsonGoodString = gson.toJson(good);
                    JSONObject jsonGood = new JSONObject(jsonGoodString);
                    JSONObject json = new JSONObject();
                    json.put("good", jsonGood);
                    json.put("good_count", good_count);

                    HttpEntity<String> entity2 = new HttpEntity<>(json.toString(), headers);

                    url = "http://localhost:8080/wc/warehouses/add/" + num;

                    restTemplate.postForObject(url, entity2, String.class);
                } else {
                    add_message = "Invalid parameters";
                }

            } catch (HttpClientErrorException exception) {
                String exceptionMessage = exception.getMessage();

                exceptionMessage = exceptionMessage.substring(exceptionMessage.lastIndexOf("\"message\":\"") + 11);
                String delete = exceptionMessage.substring(exceptionMessage.lastIndexOf("\",\"path\""));
                exceptionMessage = exceptionMessage.replace(delete, "");

                add_message = exceptionMessage;
            }
        }

        return "redirect:/warehouse/" + num;
    }

    @PostMapping("/delete/{num}/{id}")
    public String deleteSale(@PathVariable("num") Long num, @PathVariable("id") Long id) throws JSONException {
        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8080/wc/warehouses/delete/" + num + "/" + id;
            restTemplate.postForObject(url, entity, String.class);
        }

        return "redirect:/warehouse/" + num;
    }
}
