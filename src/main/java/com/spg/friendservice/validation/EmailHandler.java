package com.spg.friendservice.validation;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailHandler {

    private Pattern pattern;

    private static final String EMAIL_PATTERN =
            "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";

    public EmailHandler() {
        pattern = Pattern.compile(EMAIL_PATTERN,  Pattern.CASE_INSENSITIVE);
    }

    public Matcher matcher(final String hex) {
        return pattern.matcher(hex);
    }
}
