package movlit.be.pub_sub;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisListnerConfing {

    private final RedisMessageSubscriber subscriber;
    
    /**
     * Topic 사용을 위한 Bean 설정
     */
    @Bean
    public ChannelTopic sendMessageTopic() {
        return new ChannelTopic("sendMessage");
    }

    @Bean
    public ChannelTopic updateRoomTopic() {
        return new ChannelTopic("updateRoom");
    }

    @Bean
    public ChannelTopic readMessageTopic() {
        return new ChannelTopic("readMessage");
    }

    /** 메시지 전송을 처리하는 subscriber 설정 추가*/
    @Bean
    public MessageListenerAdapter listenerAdapterSendMessage(RedisMessageSubscriber subscriber) {
        // subscriber 내의 메서드명을 지정
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    /** 채팅방정보 변경을 처리하는 subscriber 설정 추가*/
    @Bean
    public MessageListenerAdapter listenerAdapterUpdateRoom(RedisMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "updateRoom");
    }

    /** 채팅 읽음을 처리하는 subscriber 설정 추가*/
    @Bean
    public MessageListenerAdapter listenerAdapterReadMessage(RedisMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "readMessage");
    }

    /**
     * redis 에 발행(publish)된 메시지 처리를 위한 리스너 설정
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter listenerAdapterSendMessage,
            MessageListenerAdapter listenerAdapterUpdateRoom,
            MessageListenerAdapter listenerAdapterReadMessage,
            ChannelTopic sendMessageTopic,
            ChannelTopic updateRoomTopic,
            ChannelTopic readMessageTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        container.addMessageListener(listenerAdapterSendMessage, sendMessageTopic);
        container.addMessageListener(listenerAdapterUpdateRoom, updateRoomTopic);
        container.addMessageListener(listenerAdapterReadMessage, readMessageTopic);
        return container;
    }

}
