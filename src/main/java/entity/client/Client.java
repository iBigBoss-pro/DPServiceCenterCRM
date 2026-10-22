package main.java.entity.client;

import main.java.entity.common.BaseEntity;
import main.java.entity.common.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class Client extends BaseEntity {

    // ── Идентификация ────────────────────────────────────────────
    private String firstName;
    private String lastName;
    private String patronymic;

    // ── Контакты ─────────────────────────────────────────────────
    private String phoneNumber;
    private String email;
    private String telegramChatId;

    // ── Согласие на уведомления ──────────────────────────────────
    private boolean notifyBySms;
    private boolean notifyByEmail;
    private boolean notifyByTelegram;
    private boolean notifyByPush;
    private String preferredChannel;

    // ── Сегментация и лояльность ─────────────────────────────────
    private ClientStatus status;             // NEW / REGULAR / VIP / PROBLEM / BLACKLIST
    private boolean statusManualOverride;    // true = авто-правила не применяются
    private boolean important;               // «нельзя потерять»
    private String importanceNote;           // почему важен
    private BigDecimal discountPercent;      // 0..100, ставит ADMIN/DIRECTOR

    // ── Денормализованные агрегаты (обновляет сервис) ───────────
    private int totalOrders;                 // завершённых и оплаченных
    private BigDecimal totalSpent;           // накопленная выручка
    private BigDecimal totalProfit;          // накопленная прибыль
    private Instant firstVisitAt;
    private Instant lastVisitAt;

    private String note;                     // внутренние заметки

    public Client(String firstName, String lastName, String patronymic,
                  String phoneNumber, String email) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Имя не должно быть пустым");
        }
        boolean hasPhone = phoneNumber != null && !phoneNumber.isBlank();
        boolean hasEmail = email != null && !email.isBlank();
        if (!hasPhone && !hasEmail) {
            throw new IllegalArgumentException("Нужно средство связи: телефон или email");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = ClientStatus.NEW;
        this.statusManualOverride = false;
        this.important = false;
        this.discountPercent = Money.zero();
        this.totalOrders = 0;
        this.totalSpent = Money.zero();
        this.totalProfit = Money.zero();
    }

    // ── Геттеры ──────────────────────────────────────────────────
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPatronymic() { return patronymic; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getTelegramChatId() { return telegramChatId; }
    public boolean isNotifyBySms() { return notifyBySms; }
    public boolean isNotifyByEmail() { return notifyByEmail; }
    public boolean isNotifyByTelegram() { return notifyByTelegram; }
    public boolean isNotifyByPush() { return notifyByPush; }
    public String getPreferredChannel() { return preferredChannel; }
    public ClientStatus getStatus() { return status; }
    public boolean isStatusManualOverride() { return statusManualOverride; }
    public boolean isImportant() { return important; }
    public String getImportanceNote() { return importanceNote; }
    public BigDecimal getDiscountPercent() { return discountPercent; }
    public int getTotalOrders() { return totalOrders; }
    public BigDecimal getTotalSpent() { return totalSpent; }
    public BigDecimal getTotalProfit() { return totalProfit; }
    public Instant getFirstVisitAt() { return firstVisitAt; }
    public Instant getLastVisitAt() { return lastVisitAt; }
    public String getNote() { return note; }

    // ── Сеттеры контактов ────────────────────────────────────────
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setTelegramChatId(String telegramChatId) { this.telegramChatId = telegramChatId; }
    public void setNotifyBySms(boolean v) { this.notifyBySms = v; }
    public void setNotifyByEmail(boolean v) { this.notifyByEmail = v; }
    public void setNotifyByTelegram(boolean v) { this.notifyByTelegram = v; }
    public void setNotifyByPush(boolean v) { this.notifyByPush = v; }
    public void setPreferredChannel(String v) { this.preferredChannel = v; }
    public void setNote(String note) { this.note = note; }

    // ── Бизнес-методы ────────────────────────────────────────────

    /**
     * Ручная установка статуса. Только ADMIN/DIRECTOR (проверка прав — в сервисе).
     * manualOverride = true, если статус выставлен вручную и не должен перезаписываться авто-правилами.
     */
    public void setStatus(ClientStatus status, boolean manualOverride) {
        if (status == null) throw new IllegalArgumentException("Статус клиента обязателен");
        this.status = status;
        this.statusManualOverride = manualOverride;
    }

    public void setImportant(boolean important, String importanceNote) {
        if (important && (importanceNote == null || importanceNote.isBlank())) {
            throw new IllegalArgumentException("Укажите, почему клиент важен");
        }
        this.important = important;
        this.importanceNote = importanceNote;
    }

    /**
     * Персональная скидка. Только ADMIN/DIRECTOR (проверка прав — в сервисе).
     */
    public void setDiscountPercent(BigDecimal discountPercent) {
        if (discountPercent == null) throw new IllegalArgumentException("Скидка обязательна");
        if (discountPercent.signum() < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Скидка должна быть от 0 до 100%");
        }
        this.discountPercent = discountPercent.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Вызывается сервисом при переводе заказа в COMPLETED_AND_PAID.
     */
    public void registerCompletedOrder(BigDecimal revenue, BigDecimal profit) {
        if (revenue == null) throw new IllegalArgumentException("Выручка обязательна");
        if (profit == null) throw new IllegalArgumentException("Прибыль обязательна");
        Instant now = Instant.now();
        if (firstVisitAt == null) firstVisitAt = now;
        lastVisitAt = now;
        totalOrders++;
        totalSpent = totalSpent.add(revenue);
        totalProfit = totalProfit.add(profit);
    }

    /**
     * Обратный метод — при возврате / откате оплаты.
     */
    public void unregisterCompletedOrder(BigDecimal revenue, BigDecimal profit) {
        if (revenue == null) throw new IllegalArgumentException("Выручка обязательна");
        if (profit == null) throw new IllegalArgumentException("Прибыль обязательна");
        if (totalOrders > 0) totalOrders--;
        totalSpent = totalSpent.subtract(revenue);
        totalProfit = totalProfit.subtract(profit);
    }

    public BigDecimal getAverageProfitPerOrder() {
        if (totalOrders == 0) return Money.zero();
        return totalProfit.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (lastName != null && !lastName.isBlank()) sb.append(lastName);
        if (firstName != null && !firstName.isBlank()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(firstName);
        }
        if (patronymic != null && !patronymic.isBlank()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(patronymic);
        }
        return sb.toString();
    }
}