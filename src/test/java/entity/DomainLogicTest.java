package entity;

import entity.approval.Approval;
import entity.approval.ApprovalChannel;
import entity.approval.ApprovalStatus;
import entity.client.Client;
import entity.document.Document;
import entity.document.DocumentStatus;
import entity.document.DocumentType;
import entity.inventory.Part;
import entity.notification.Notification;
import entity.notification.NotificationChannel;
import entity.notification.NotificationStatus;
import entity.order.DeviceType;
import entity.order.Order;
import entity.order.OrderStatus;
import entity.payment.Payment;
import entity.payment.PaymentMethod;
import entity.user.Role;
import entity.user.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DomainLogicTest {
    private static BigDecimal money(String value) { return new BigDecimal(value); }

    private Order newOrder() {
        Client client = new Client("Айван", "Петров", null, "+79990000000", null);
        return new Order("SC-2026-000001", "public-token", client, 1L, 1L,
                Instant.now().plusSeconds(3600), DeviceType.SMARTPHONE, "Apple", "iPhone 13",
                "SN-1", "Не включается", null, null, "Царапины на корпусе", false, false,
                money("1000"), 30);
    }

    @Test
    void stockCannotBecomeNegativeOrOverflow() {
        Part part = new Part("Дисплей", "LCD-001");
        assertThrows(IllegalStateException.class, () -> part.adjustStock(-1));
        assertThrows(IllegalArgumentException.class, () -> part.setMinStock(-1));
        part.adjustStock(4);
        part.adjustStock(-3);
        assertEquals(1, part.getQuantityInStock());
    }

    @Test
    void notificationMustFollowLegalStateTransitions() {
        Notification notification = new Notification(1L, 1L, null, 0,
                NotificationChannel.SMS, "+79990000000", null, "Заказ принят");
        assertThrows(IllegalStateException.class, notification::markDelivered);
        notification.markSent();
        notification.markDelivered();
        notification.markRead();
        assertEquals(NotificationStatus.READ, notification.getStatus());
    }

    @Test
    void orderApprovalDoesNotPartiallyUpdateOnInvalidDeadline() {
        Order order = newOrder();
        order.changeStatus(OrderStatus.ACCEPTED, 1L, null);
        order.changeStatus(OrderStatus.DIAGNOSTICS, 1L, null);
        order.changeStatus(OrderStatus.AWAITING_APPROVAL, 1L, null);
        assertThrows(IllegalArgumentException.class,
                () -> order.approveConditions(money("2000"), order.getAcceptedAt().minusSeconds(1)));
        assertNull(order.getApprovedPrice());
        assertNull(order.getApprovedDeadline());
    }

    @Test
    void negativeCostComponentCannotBeHiddenByPositiveComponent() {
        Order order = newOrder();
        assertThrows(IllegalArgumentException.class,
                () -> order.finalizePricing(money("1000"), money("-100"), money("200")));
        assertNull(order.getFinalPrice());
    }

    @Test
    void orderCannotBeMarkedPaidBeforePricingIsFinalized() {
        Order order = newOrder();
        order.changeStatus(OrderStatus.ACCEPTED, 1L, null);
        order.changeStatus(OrderStatus.DIAGNOSTICS, 1L, null);
        order.changeStatus(OrderStatus.IN_REPAIR, 1L, null);
        order.changeStatus(OrderStatus.COMPLETED, 1L, null);
        assertThrows(IllegalStateException.class,
                () -> order.changeStatus(OrderStatus.COMPLETED_AND_PAID, 1L, null));
    }

    @Test
    void paymentRejectsZeroAmountAndMissingOperatorOrMethod() {
        assertThrows(IllegalArgumentException.class,
                () -> Payment.income(1L, money("0"), PaymentMethod.CASH, 1L, null));
        assertThrows(IllegalArgumentException.class,
                () -> Payment.income(1L, money("10"), null, 1L, null));
        assertThrows(IllegalArgumentException.class,
                () -> Payment.income(1L, money("10"), PaymentMethod.CASH, null, null));
    }

    @Test
    void clientAlwaysRetainsAtLeastOneContactChannel() {
        Client client = new Client("Айван", null, null, "+79990000000", null);
        assertThrows(IllegalArgumentException.class, () -> client.setPhoneNumber(" "));
        client.setEmail("client@example.com");
        client.setPhoneNumber(null);
        assertEquals("client@example.com", client.getEmail());
    }

    @Test
    void repeatedDocumentPrintsUseIncreasingCopyNumbers() {
        Document doc = new Document(1L, DocumentType.ACCEPTANCE_ACT, "SC-2026-1",
                1L, 1, "<p>Act</p>", 1L);
        Document second = doc.reprint(1L);
        Document third = doc.reprint(1L);
        assertEquals(2, second.getCopyNumber());
        assertEquals(3, third.getCopyNumber());
        assertEquals(DocumentStatus.REPRINT, second.getStatus());
    }

    @Test
    void expiredApprovalCannotBeAccepted() {
        // Create with a future time, then expire manually
        Approval approval = new Approval(1L, 1L, money("100"), null, "Доплата",
                ApprovalChannel.SMS, Instant.now().plusSeconds(3600));
        assertEquals(ApprovalStatus.PENDING, approval.getStatus());
        approval.expire(); // Simulate expiration
        assertThrows(IllegalStateException.class, () -> approval.approve(ApprovalChannel.SMS, "Ок"));
        assertEquals(ApprovalStatus.EXPIRED, approval.getStatus());
    }

    @Test
    void userAggregateRollbackCannotCreateNegativeCounts() {
        User user = new User("master", "hashed", Role.MASTER, "Иван", "Иванов", "master@example.com");
        assertThrows(IllegalStateException.class, () -> user.unregisterCompletedWork());
        assertThrows(IllegalStateException.class, () -> user.unregisterCompletedOrderAsMaster(money("10")));
    }
}
