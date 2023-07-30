package com.acapella.pella.chat.controller.api;

import com.acapella.pella.chat.component.TopicProperty;
import com.acapella.pella.chat.dto.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ApiController {
    private final TopicProperty topicProperty;

    // 모든 토픽 데이터를 가져옴
    @GetMapping("/topics")
    public List<Topic> getTopics() {
        return topicProperty.getTopics();
    }
}
