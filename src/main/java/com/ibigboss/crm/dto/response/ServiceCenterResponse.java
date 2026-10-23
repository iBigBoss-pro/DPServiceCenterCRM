package com.ibigboss.crm.dto.response;

public class ServiceCenterResponse {

    private Long id;
    private String name;
    private String legalName;
    private String inn;
    private String ogrn;
    private String legalAddress;
    private String actualAddress;
    private String phone;
    private String email;
    private String directorName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }

    public String getInn() { return inn; }
    public void setInn(String inn) { this.inn = inn; }

    public String getOgrn() { return ogrn; }
    public void setOgrn(String ogrn) { this.ogrn = ogrn; }

    public String getLegalAddress() { return legalAddress; }
    public void setLegalAddress(String legalAddress) { this.legalAddress = legalAddress; }

    public String getActualAddress() { return actualAddress; }
    public void setActualAddress(String actualAddress) { this.actualAddress = actualAddress; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDirectorName() { return directorName; }
    public void setDirectorName(String directorName) { this.directorName = directorName; }
}
