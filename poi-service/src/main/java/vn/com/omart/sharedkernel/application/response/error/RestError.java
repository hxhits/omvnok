package vn.com.omart.sharedkernel.application.response.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;
import vn.com.omart.sharedkernel.application.model.error.ErrorLocation;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class RestError {

    private int code;
    private boolean translatable = false;
    private String message;

    private List<Error> errors;

    private RestError() {
    }

    public RestError(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean isTranslatable() {
        return translatable;
    }

    public void setTranslatable(boolean translatable) {
        this.translatable = translatable;
    }


    @SuppressWarnings("unused")
    public static class Error {

        private String reason;
        private String message;
        private String location;
        private String locationType;

        private Error(String reason, String message, String location, ErrorLocation locationType) {
            this.reason = reason;
            this.message = message;
            this.location = location;

            if (locationType != null) {
                this.locationType = locationType.camelCaseName();
            }
        }

        public String getReason() {
            return reason;
        }

        public String getMessage() {
            return message;
        }

        public String getLocation() {
            return location;
        }

        public String getLocationType() {
            return locationType;
        }

        @Override
        public String toString() {
            return "Error{" +
                "reason='" + reason + '\'' +
                ", message='" + message + '\'' +
                ", location='" + location + '\'' +
                ", locationType='" + locationType + '\'' +
                '}';
        }
    }


    public static class TranslatableError extends Error {
        private List<String> messageVars;

        protected TranslatableError(String reason, String message, String location, ErrorLocation locationType, List<String> messageVars) {
            super(reason, message, location, locationType);
            this.messageVars = messageVars;
        }

        public List<String> getMessageVars() {
            return messageVars;
        }

        @Override
        public String toString() {
            return "Error{" +
                "reason='" + super.getReason() + '\'' +
                ", message='" + super.getMessage() + '\'' +
                ", location='" + super.getLocation() + '\'' +
                ", locationType='" + super.getLocationType() + '\'' +
                ", messageVars='" + this.messageVars + '\'' +
                '}';
        }
    }


    public static class Builder {
        private int code;
        private boolean translatable = false;
        private String message;
        private List<ErrorConfigurer> errorConfigurers = new ArrayList<>();

        public Builder(int code) {
            this.code = code;
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder translatable(boolean translatable) {
            this.translatable = translatable;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        protected List<? extends ErrorConfigurer> errorConfigurers() {
            return this.errorConfigurers;
        }

        public Builder error(String reason, String message) {
            error(reason, message, null, null);
            return this;
        }

        public Builder translatableError(String reason, String message, List<String> messageVars) {
            translatableError(reason, message, null, null, messageVars);
            return this;
        }

        public Builder translatableError(String reason, String message, String location, ErrorLocation locationType, List<String> messageVars) {
            this.translatable = true;
            TranslatableErrorConfigurer configurer = new TranslatableErrorConfigurer();
            configurer.reason(reason);
            configurer.message(message);
            configurer.location(location);
            configurer.locationType(locationType);
            configurer.messageVar(messageVars);
            this.errorConfigurers.add(configurer);
            return this;
        }

        public Builder error(String reason, String message, String location, ErrorLocation locationType) {
            ErrorConfigurer configurer = new ErrorConfigurer();
            configurer.reason(reason);
            configurer.message(message);
            configurer.location(location);
            configurer.locationType(locationType);
            this.errorConfigurers.add(configurer);
            return this;
        }

        protected String message() {
            return this.message;
        }

        public int code() {
            return this.code;
        }

        public RestError build() {
            RestError restError = new RestError();

            restError.message = message();
            restError.code = code();
            restError.translatable = this.translatable;
            restError.errors = new ArrayList<>();

            List<? extends ErrorConfigurer> errorConfigurers = errorConfigurers();
            for (ErrorConfigurer e : errorConfigurers) {
                if (e instanceof TranslatableErrorConfigurer) {
                    restError.errors.add(new TranslatableError(
                        e.reason(),
                        e.message(),
                        e.location(),
                        e.locationType(),
                        ((TranslatableErrorConfigurer) e).messageVars()));
                } else {
                    restError.errors.add(new Error(
                        e.reason(),
                        e.message(),
                        e.location(),
                        e.locationType()));
                }
            }

            return restError;
        }
    }
}
