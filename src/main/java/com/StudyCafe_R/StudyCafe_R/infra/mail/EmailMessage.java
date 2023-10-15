package com.StudyCafe_R.StudyCafe_R.infra.mail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessage {

    private String to;

    private String subject;

    private String message;
}
