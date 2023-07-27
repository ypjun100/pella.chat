package com.acapella.pella.chat.controller;

import com.acapella.pella.chat.component.TopicProperty;
import com.acapella.pella.chat.dto.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {
    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/waiting")
    public String waiting() { return "waiting"; }
}
