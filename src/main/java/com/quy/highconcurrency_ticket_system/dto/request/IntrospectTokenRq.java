package com.quy.highconcurrency_ticket_system.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntrospectTokenRq {
    private String token;
}
