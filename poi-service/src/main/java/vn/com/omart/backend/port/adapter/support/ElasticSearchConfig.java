package vn.com.omart.backend.port.adapter.support;

import java.io.File;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "vn.com.omart.backend.port.adapter.elasticsearch")
public class ElasticSearchConfig {

//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate(Client client) throws Exception {
//        return new ElasticsearchTemplate(client);
//    }
    
    @Bean
    public NodeBuilder nodeBuilder() {
        return new NodeBuilder();
    }
    
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
    	 File tmpDir = File.createTempFile("elastic", "omart");
         
         Settings.Builder elasticsearchSettings =
                 Settings.settingsBuilder()
                         .put("http.enabled", "true") // 1
                         .put("index.number_of_shards", "1")
                         .put("path.data", new File(tmpDir, "data").getAbsolutePath()) // 2
                         .put("path.logs", new File(tmpDir, "logs").getAbsolutePath()) // 2
                         .put("path.work", new File(tmpDir, "work").getAbsolutePath()) // 2
                         .put("path.home", tmpDir); // 3



         return new ElasticsearchTemplate(nodeBuilder()
                 .local(true)
                 .settings(elasticsearchSettings.build())
                 .node()
                 .client());

    }
}