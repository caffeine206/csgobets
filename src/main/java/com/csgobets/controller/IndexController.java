package com.csgobets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring web application launcher
 */
@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * Returns the name, excluding filetype, of the HTML file to be searched for
     * when reaching the webapp's root
     *
     * @return the name of the homepage file
     */
    @RequestMapping
    public String getIndexPage() {
        return "index";
    }
}
