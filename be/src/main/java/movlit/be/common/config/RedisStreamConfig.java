package movlit.be.common.config;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisStreamConfig {

    // TODO : AppConfig에서 정의한 threadPoolExecutor의 설정이 Redis Stream 처리에도 적합한지 확인.(스레드 풀 크키, 큐 크기)
//    private final ThreadPoolExecutor threadPoolExecutor;
//    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
    ) {
        log.info("redisconnectfactory {}", redisConnectionFactory);
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setMaxPoolSize(16);
        taskExecutor.setThreadNamePrefix("redis-stream-");
        taskExecutor.initialize();
        log.info("==== StreamMessageListenerContainer threadPoolExecutor : {} ", taskExecutor);

        StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options = StreamMessageListenerContainerOptions
                .builder()
                .executor(taskExecutor)
                .batchSize(10)
                .pollTimeout(Duration.ofSeconds(1))
                .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(
                redisConnectionFactory, options);
        container.start();
        log.info("==== StreamMessageListenerContainer 빈 등록 : {}", container);
        return container;
    }

}
