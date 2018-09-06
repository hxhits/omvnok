package vn.com.omart.sharedkernel.application.response.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranslatableErrorConfigurer extends ErrorConfigurer {
    private List<String> messageVars;

    public TranslatableErrorConfigurer messageVar(List<String> vars) {
        if (vars != null) {
            getMessageVars().addAll(vars);
        } else {
            getMessageVars().addAll(Collections.emptyList());
        }
        return this;
    }

    public TranslatableErrorConfigurer messageVar(String var) {
        getMessageVars().add(var);
        return this;
    }

    protected List<String> messageVars() {
        return this.messageVars;
    }

    private List<String> getMessageVars() {
        if (this.messageVars == null) {
            this.messageVars = new ArrayList<>();
        }
        return this.messageVars;
    }
}
