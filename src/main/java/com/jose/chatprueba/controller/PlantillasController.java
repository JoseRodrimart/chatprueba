package com.jose.chatprueba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PlantillasController {

    @RequestMapping("/tienda")
    public String tienda(Model modelo){
        modelo.addAttribute("mensaje","hola mundo desde thymeleaf");
        return "tienda";
    }
}
