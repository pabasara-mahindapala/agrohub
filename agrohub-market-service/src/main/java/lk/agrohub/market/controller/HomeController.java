package lk.agrohub.market.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String index() {
        return "Index Page";
    }

    @GetMapping("about")
    public String about() {
        return "About Page";
    }
}
