package com.acapella.pella.chat.component;

import com.acapella.pella.chat.dto.Topic;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("application")
public class TopicProperty {
    @Getter
    @Setter
    private int numberOfTopics;

    @Getter
    @Setter
    private List<Topic> topics;

    public Topic getTopic(int topicId) {
        return topics.get(topicId - 1);
    }
}
