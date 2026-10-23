package com.ibigboss.crm.dto.response;

public class LocationResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String workingHours;
    private boolean active;
    private boolean defaultLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(boolean defaultLocation) {
        this.defaultLocation = defaultLocation;
    }
}
