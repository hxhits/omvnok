package vn.com.omart.backend.port.adapter.slack;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.Data;

@Service
public class SlackNotify {
	
    @Value("${slack.url}")
    private String url;

    public void post(SlackBody payload) {
    	
    	if (StringUtils.isEmpty(url)) {
    		return;
    	}
    	
        RestTemplate template = new RestTemplate();
        template.postForObject(url, payload, String.class);
    }

    @Data
    public static class SlackBody {
        private String username;
        private String text;
    }
}
