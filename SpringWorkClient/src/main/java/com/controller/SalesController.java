package com.controller;

import com.google.gson.Gson;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/sales")
public class SalesController {

    private String add_message = "";

    @GetMapping
    public ModelAndView sales(@RequestParam(required = false, defaultValue = "") String name, ModelMap model) throws JSONException {
        String viewName;
        if (LoginController.getCurrentUserRole().equals("ROLE_ADMIN")) {
            viewName = "sales";
        } else {
            viewName = "salesForUser";
        }

        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object[]> responseEntity;
            Object[] sales;
            String url;

            try {
                if (name.equals("")) {
                    url = "http://localhost:8080/wc/sale/getAll";
                } else {
                    url = "http://localhost:8080/wc/sale/getByName/" + name;
                }
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Object[].class);
                sales = responseEntity.getBody();

                model.put("sales", sales);
                model.put("find_message", "");

            } catch (HttpClientErrorException exception) {
                model.put("find_message", "The entry with the specified good is not found");
            }
        }
        model.put("add_message", add_message);
        model.put("checkValidation", LoginController.tokenIsValidate());

        return new ModelAndView(viewName);
    }

    @PostMapping("/addSale")
    public String addSale(@RequestParam String nameGood, @RequestParam Long good_count, @RequestParam String create_date) throws JSONException {
        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url;

            add_message = "";

            try {
                    if (!nameGood.equals("") && good_count != null && isTimeStampValid(create_date)) {

                        url = "http://localhost:8080/wc/good/getByName/" + nameGood;
                        ResponseEntity<Object> goodResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
                        Object good = goodResponseEntity.getBody();

                        Gson gson = new Gson();
                        String jsonStringGood = gson.toJson(good);
                        JSONObject jsonGood = new JSONObject(jsonStringGood);
                        JSONObject json = new JSONObject();
                        json.put ("good", jsonGood);
                        json.put("good_count", good_count);
                        json.put("create_date", create_date);

                        HttpEntity<String> entity2 = new HttpEntity<>(json.toString(), headers);

                        url = "http://localhost:8080/wc/sale/add";

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

        return "redirect:/sales";
    }

    @PostMapping("/delete/{id}")
    public String deleteSale(@PathVariable("id") Long id) throws JSONException {
        if (LoginController.tokenIsValidate()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(LoginController.CURRENT_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8080/wc/sale/delete/" + id;
            restTemplate.postForObject(url, entity, String.class);
        }

        return "redirect:/sales";
    }

    private boolean isTimeStampValid(String timeStamp)
    {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");

        try{
            format.parse(timeStamp);
            return true;
        }
        catch(ParseException exception) {
            return false;
        }
    }
}
