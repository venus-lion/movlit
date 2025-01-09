package movlit.be.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

   // @Value("${spring.elasticsearch.username}")
    private String username = "elastic";


  //  @Value("${spring.elasticsearch.password}")
    private String password = "30wne=PkQKzjzF-eqE5u";

//    @Value("${spring.elasticsearch.uris}")
    private String esHost = "15.164.150.76:9200";

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(esHost)
                .withBasicAuth(username, password)
                .build();
    }
}