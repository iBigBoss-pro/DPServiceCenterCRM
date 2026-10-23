package entity.client;

import entity.common.BaseEntity;
import entity.common.Money;
import entity.order.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Клиент сервисного центра.
 */
@Entity
@Table(name = "clients")

public class Client extends BaseEntity {

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "patronymic", length = 255)
    private String patronymic;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "telegram_chat_id", length = 255)
    private String telegramChatId;

    @Column(name = "notify_by_sms")
    private boolean notifyBySms;

    @Column(name = "notify_by_email")
    private boolean notifyByEmail;

    @Column(name = "notify_by_telegram")
    private boolean notifyByTelegram;

    @Column(name = "notify_by_push")
    private boolean notifyByPush;

    @Column(name = "preferred_channel", length = 50)
    private String preferredChannel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private ClientStatus status;

    @Column(name = "status_manual_override")
    private boolean statusManualOverride;

    @Column(name = "important")
    private boolean important;

    @Column(name = "importance_note", length = 1000)
    private String importanceNote;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "total_orders")
    private int totalOrders;

    @Column(name = "total_spent", precision = 15, scale = 2)
    private BigDecimal totalSpent;

    @Column(name = "total_profit", precision = 15, scale = 2)
    private BigDecimal totalProfit;

    @Column(name = "first_visit_at")
    private Instant firstVisitAt;

    @Column(name = "last_visit_at")
    private Instant lastVisitAt;

    @Column(name = "note", length = 2000)
    private String note;

    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST)
    private List<Order> orders = new ArrayList<>();

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
    public List<Order> getOrders() { return orders; }

    public void setPhoneNumber(String phoneNumber) {
        ensureContactAvailable(phoneNumber, this.email);
        this.phoneNumber = phoneNumber;
        if (phoneNumber == null || phoneNumber.isBlank()) this.notifyBySms = false;
    }

    public void setEmail(String email) {
        ensureContactAvailable(this.phoneNumber, email);
        this.email = email;
        if (email == null || email.isBlank()) this.notifyByEmail = false;
    }

    private static void ensureContactAvailable(String phoneNumber, String email) {
        boolean hasPhone = phoneNumber != null && !phoneNumber.isBlank();
        boolean hasEmail = email != null && !email.isBlank();
        if (!hasPhone && !hasEmail) {
            throw new IllegalArgumentException("У клиента должен оставаться телефон или email");
        }
    }

    public void setTelegramChatId(String telegramChatId) { this.telegramChatId = telegramChatId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }
    public void setNotifyBySms(boolean v) { this.notifyBySms = v; }
    public void setNotifyByEmail(boolean v) { this.notifyByEmail = v; }
    public void setNotifyByTelegram(boolean v) { this.notifyByTelegram = v; }
    public void setNotifyByPush(boolean v) { this.notifyByPush = v; }
    public void setPreferredChannel(String v) { this.preferredChannel = v; }
    public void setNote(String note) { this.note = note; }

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

    public void setDiscountPercent(BigDecimal discountPercent) {
        if (discountPercent == null) throw new IllegalArgumentException("Скидка обязательна");
        if (discountPercent.signum() < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Скидка должна быть от 0 до 100%");
        }
        this.discountPercent = discountPercent.setScale(2, RoundingMode.HALF_UP);
    }

    public void registerCompletedOrder(BigDecimal revenue, BigDecimal profit) {
        BigDecimal validatedRevenue = Money.of(revenue, "Выручка");
        if (profit == null) throw new IllegalArgumentException("Прибыль обязательна");
        BigDecimal validatedProfit = profit.setScale(2, RoundingMode.HALF_UP);
        Instant now = Instant.now();
        if (firstVisitAt == null) firstVisitAt = now;
        lastVisitAt = now;
        totalOrders++;
        totalSpent = totalSpent.add(validatedRevenue);
        totalProfit = totalProfit.add(validatedProfit);
    }

    public void unregisterCompletedOrder(BigDecimal revenue, BigDecimal profit) {
        if (revenue == null || revenue.signum() < 0) {
            throw new IllegalArgumentException("Выручка обязательна и не может быть отрицательной");
        }
        if (profit == null) throw new IllegalArgumentException("Прибыль обязательна");
        if (totalOrders <= 0) {
            throw new IllegalStateException("Нет завершённых заказов для отката");
        }
        BigDecimal validatedRevenue = Money.of(revenue, "Выручка");
        BigDecimal currentSpent = totalSpent == null ? Money.zero() : totalSpent;
        if (currentSpent.compareTo(validatedRevenue) < 0) {
            throw new IllegalStateException("Откат превышает накопленную выручку");
        }
        totalOrders--;
        totalSpent = currentSpent.subtract(validatedRevenue);
        totalProfit = totalProfit.subtract(profit.setScale(2, RoundingMode.HALF_UP));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(serviceCenterId, client.serviceCenterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceCenterId);
    }
}
