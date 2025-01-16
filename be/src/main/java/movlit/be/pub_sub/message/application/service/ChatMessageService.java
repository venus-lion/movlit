package movlit.be.pub_sub.message.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.pub_sub.message.entity.ChatMessage;
import movlit.be.pub_sub.message.infra.persistence.mongo.ChatMessageMongoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageMongoRepository chatMessageMongoRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageMongoRepository.save(message);
    }

}
