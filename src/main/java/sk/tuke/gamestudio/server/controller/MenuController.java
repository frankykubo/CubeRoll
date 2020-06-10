package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/menu")
public class MenuController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping
    public String menu() {
        return "menu";
    }
}
