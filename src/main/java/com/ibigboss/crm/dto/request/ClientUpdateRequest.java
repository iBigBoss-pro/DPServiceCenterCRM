package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ClientUpdateRequest {

    private String lastName;
    private String firstName;
    private String patronymic;

    private String phoneNumber;

    private String email;

    private String telegramChatId;
    private Boolean notifyBySms;
    private Boolean notifyByEmail;
    private Boolean notifyByTelegram;
    private Boolean notifyByPush;
    private String preferredChannel;
    private String note;
    private String status;
    private Boolean important;
    private String importanceNote;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public Boolean isNotifyBySms() {
        return notifyBySms;
    }

    public void setNotifyBySms(Boolean notifyBySms) {
        this.notifyBySms = notifyBySms;
    }

    public Boolean isNotifyByEmail() {
        return notifyByEmail;
    }

    public void setNotifyByEmail(Boolean notifyByEmail) {
        this.notifyByEmail = notifyByEmail;
    }

    public Boolean isNotifyByTelegram() {
        return notifyByTelegram;
    }

    public void setNotifyByTelegram(Boolean notifyByTelegram) {
        this.notifyByTelegram = notifyByTelegram;
    }

    public Boolean isNotifyByPush() {
        return notifyByPush;
    }

    public void setNotifyByPush(Boolean notifyByPush) {
        this.notifyByPush = notifyByPush;
    }

    public String getPreferredChannel() {
        return preferredChannel;
    }

    public void setPreferredChannel(String preferredChannel) {
        this.preferredChannel = preferredChannel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }

    public String getImportanceNote() {
        return importanceNote;
    }

    public void setImportanceNote(String importanceNote) {
        this.importanceNote = importanceNote;
    }
}
