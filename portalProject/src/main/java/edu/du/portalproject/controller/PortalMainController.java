package edu.du.portalproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortalMainController {

    @GetMapping("/")
    public String index() {
        return "portalMain/index";
    }
}
