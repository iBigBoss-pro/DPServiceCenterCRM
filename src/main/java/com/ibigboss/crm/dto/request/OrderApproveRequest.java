package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

public class OrderApproveRequest {

    @NotNull(message = "Согласованная цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть > 0")
    private BigDecimal approvedPrice;

    private Instant approvedDeadline;

    public BigDecimal getApprovedPrice() {
        return approvedPrice;
    }

    public void setApprovedPrice(BigDecimal approvedPrice) {
        this.approvedPrice = approvedPrice;
    }

    public Instant getApprovedDeadline() {
        return approvedDeadline;
    }

    public void setApprovedDeadline(Instant approvedDeadline) {
        this.approvedDeadline = approvedDeadline;
    }
}
