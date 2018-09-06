package vn.com.omart.auth.port.smsgw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

//@Service
@Slf4j
public class SmsGatewayService {

    private final String url;
    private final String user;
    private final String pass;
    private final String from;

    public SmsGatewayService(String url, String user, String pass, String from) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.from = from;
    }

    public void send(String to, String content) {

        MultiValueMap<String, String> vars = new SmsRequest(this.user, this.pass, this.from)
            .compose(to, content)
            .build();

        UriComponents uriComponents = UriComponentsBuilder
            .fromHttpUrl(this.url)
            .queryParams(vars).build();
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity(uriComponents.toUri(), String.class);

        log.info("Send to={} content={} response={}", to, content, forEntity.getBody());

    }

}
