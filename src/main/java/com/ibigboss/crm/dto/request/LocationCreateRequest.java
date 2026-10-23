package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LocationCreateRequest {

    @NotBlank(message = "Название обязательно")
    private String name;

    @NotBlank(message = "Адрес обязателен")
    private String address;

    private String phone;
    private String workingHours;
    private boolean defaultLocation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public boolean isDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(boolean defaultLocation) {
        this.defaultLocation = defaultLocation;
    }
}
