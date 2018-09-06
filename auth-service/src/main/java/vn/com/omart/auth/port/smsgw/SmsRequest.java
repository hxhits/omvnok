package vn.com.omart.auth.port.smsgw;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SmsRequest {

    private String u;

    private String pwd;

    private String from;

    private String phone;

    private String sms;

    public SmsRequest(String u, String pwd, String from) {
        this.u = u;
        this.pwd = pwd;
        this.from = from;
    }

    public SmsRequest compose(String to, String content) {
        this.phone = to;
        this.sms = content;
        return this;
    }

    public MultiValueMap<String, String> build() {
        MultiValueMap<String, String> vars = new LinkedMultiValueMap<>();
        vars.add("u", this.u);
        vars.add("pwd", this.pwd);
        vars.add("from", this.from);
        vars.add("phone", this.phone);
        vars.add("sms", this.sms);
        return vars;
    }
}
