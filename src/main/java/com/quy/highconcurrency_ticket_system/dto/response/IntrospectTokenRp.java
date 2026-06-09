package com.quy.highconcurrency_ticket_system.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IntrospectTokenRp {
    private boolean valid;
}
