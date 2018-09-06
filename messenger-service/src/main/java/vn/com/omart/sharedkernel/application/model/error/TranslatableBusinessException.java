package vn.com.omart.sharedkernel.application.model.error;

import java.util.List;

public class TranslatableBusinessException extends BusinessException {
    List<String> messageVariables;

    public TranslatableBusinessException(String reason, String message, List<String> messageVariables) {
        super(reason, message);
        this.messageVariables = messageVariables;
    }

    public List<String> messageVariables() {
        return this.messageVariables;
    }

    @Override
    public String toString() {
        return super.toString() + ". Variable: " + this.messageVariables;
    }
}
