package movlit.be.pub_sub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Redis 설정 클래스에서 Listener Container 설정
 */
@Configuration
public class RedisPubSubConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            ChannelTopic topic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 특정 채널 구독 설정
        container.addMessageListener(listenerAdapter, topic);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RedisMessageSubscriber subscriber) {
        // subscriber의 onMessage 메소드를 호출하도록 설정
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    ChannelTopic topic() {
        // 구독할 채널 설정 (예: "myChannel")
        return new ChannelTopic("myChannel");
    }

}
