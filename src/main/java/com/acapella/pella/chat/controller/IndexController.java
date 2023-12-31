package com.acapella.pella.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/waiting")
    public String waiting() { return "waiting"; }

    @GetMapping("/chat")
    public String chat() { return "chat"; }
}
