package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.NotNull;

public class PartAdjustStockRequest {

    @NotNull(message = "Изменение количества обязательно")
    private Integer delta;

    public Integer getDelta() {
        return delta;
    }

    public void setDelta(Integer delta) {
        this.delta = delta;
    }
}
