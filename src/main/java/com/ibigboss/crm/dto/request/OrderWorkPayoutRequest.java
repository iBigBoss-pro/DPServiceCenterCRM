package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderWorkPayoutRequest {

    @NotNull(message = "Сумма выплаты обязательна")
    @DecimalMin(value = "0.01", message = "Сумма должна быть > 0")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
