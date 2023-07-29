package com.acapella.pella.chat.controller.api;

import com.acapella.pella.chat.component.TopicProperty;
import com.acapella.pella.chat.dto.Topic;
import com.acapella.pella.chat.service.WaitingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ApiController {
    private final TopicProperty topicProperty;

    @GetMapping("/topics")
    public List<Topic> getTopics() {
        return topicProperty.getTopics();
    }

    @GetMapping("/enter-waiting")
    public String enterWaiting() {
        return WaitingRoomService.getWaitingRoomId();
    }
}
