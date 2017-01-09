package net.ozwolf.raml.exception;

import org.raml.v2.api.model.common.ValidationResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class RamlValidationException extends Exception {
    private final List<String> errors;

    public RamlValidationException(String message, List<ValidationResult> errors) {
        super(message);
        this.errors = errors.stream().map(v -> v.getMessage()).collect(toList());
    }

    public List<String> getErrors() {
        return errors;
    }
}
