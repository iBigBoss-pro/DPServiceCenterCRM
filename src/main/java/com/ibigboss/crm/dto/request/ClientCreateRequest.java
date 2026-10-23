package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ClientCreateRequest {

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    private String patronymic;

    @NotBlank(message = "Номер телефона обязателен")
    private String phoneNumber;

    private String email;

    private String telegramChatId;
    private boolean notifyBySms;
    private boolean notifyByEmail;
    private boolean notifyByTelegram;
    private boolean notifyByPush;
    private String preferredChannel;
    private String note;

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

    public boolean isNotifyBySms() {
        return notifyBySms;
    }

    public void setNotifyBySms(boolean notifyBySms) {
        this.notifyBySms = notifyBySms;
    }

    public boolean isNotifyByEmail() {
        return notifyByEmail;
    }

    public void setNotifyByEmail(boolean notifyByEmail) {
        this.notifyByEmail = notifyByEmail;
    }

    public boolean isNotifyByTelegram() {
        return notifyByTelegram;
    }

    public void setNotifyByTelegram(boolean notifyByTelegram) {
        this.notifyByTelegram = notifyByTelegram;
    }

    public boolean isNotifyByPush() {
        return notifyByPush;
    }

    public void setNotifyByPush(boolean notifyByPush) {
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
}
