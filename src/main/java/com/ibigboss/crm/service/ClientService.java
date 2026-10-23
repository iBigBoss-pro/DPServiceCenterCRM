package com.ibigboss.crm.service;

import com.ibigboss.crm.dto.request.ClientCreateRequest;
import com.ibigboss.crm.dto.request.ClientUpdateRequest;
import com.ibigboss.crm.dto.response.ClientResponse;
import entity.client.Client;
import entity.common.Money;
import com.ibigboss.crm.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ClientResponse create(ClientCreateRequest request, Long serviceCenterId) {
        Client client = new Client(
            request.getFirstName(),
            request.getLastName(),
            request.getPatronymic(),
            request.getPhoneNumber(),
            request.getEmail()
        );
        client.setServiceCenterIdForTest(serviceCenterId);
        if (request.getPhoneNumber() != null) client.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) client.setEmail(request.getEmail());
        client.setTelegramChatId(request.getTelegramChatId());
        client.setNotifyBySms(request.isNotifyBySms());
        client.setNotifyByEmail(request.isNotifyByEmail());
        client.setNotifyByTelegram(request.isNotifyByTelegram());
        client.setNotifyByPush(request.isNotifyByPush());
        client.setPreferredChannel(request.getPreferredChannel());
        client.setNote(request.getNote());
        client = clientRepository.save(client);
        return toResponse(client);
    }

    @Transactional
    public ClientResponse update(Long id, ClientUpdateRequest request, Long serviceCenterId) {
        Client client = clientRepository.findByIdAndServiceCenterId(id, serviceCenterId)
            .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + id));
        if (request.getFirstName() != null) client.setFirstName(request.getFirstName());
        if (request.getLastName() != null) client.setLastName(request.getLastName());
        if (request.getPatronymic() != null) client.setPatronymic(request.getPatronymic());
        if (request.getPhoneNumber() != null) client.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) client.setEmail(request.getEmail());
        if (request.getTelegramChatId() != null) client.setTelegramChatId(request.getTelegramChatId());
        Boolean nbs = request.isNotifyBySms();
        if (nbs != null) client.setNotifyBySms(nbs);
        Boolean nbe = request.isNotifyByEmail();
        if (nbe != null) client.setNotifyByEmail(nbe);
        Boolean nbt = request.isNotifyByTelegram();
        if (nbt != null) client.setNotifyByTelegram(nbt);
        Boolean nbp = request.isNotifyByPush();
        if (nbp != null) client.setNotifyByPush(nbp);
        if (request.getPreferredChannel() != null) client.setPreferredChannel(request.getPreferredChannel());
        if (request.getNote() != null) client.setNote(request.getNote());
        if (request.getStatus() != null) {
            entity.client.ClientStatus status = entity.client.ClientStatus.valueOf(request.getStatus());
            client.setStatus(status, true);
        }
        if (request.getImportant() != null) client.setImportant(request.getImportant(), request.getImportanceNote());
        client = clientRepository.save(client);
        return toResponse(client);
    }

    public ClientResponse getById(Long id, Long serviceCenterId) {
        Client client = clientRepository.findByIdAndServiceCenterId(id, serviceCenterId)
            .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + id));
        return toResponse(client);
    }

    public List<ClientResponse> listAll(Long serviceCenterId) {
        return clientRepository.findAll().stream()
            .filter(c -> c.getServiceCenterId().equals(serviceCenterId))
            .filter(c -> !c.isDeleted())
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public void delete(Long id, Long serviceCenterId) {
        Client client = clientRepository.findByIdAndServiceCenterId(id, serviceCenterId)
            .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + id));
        client.setDeletedAt(Instant.now());
        clientRepository.save(client);
    }

    @Transactional
    public void registerCompletedOrder(Long clientId, BigDecimal revenue, BigDecimal profit, Long serviceCenterId) {
        Client client = clientRepository.findByIdAndServiceCenterId(clientId, serviceCenterId)
            .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + clientId));
        client.registerCompletedOrder(revenue, profit);
        clientRepository.save(client);
    }

    @Transactional
    public void unregisterCompletedOrder(Long clientId, BigDecimal revenue, BigDecimal profit, Long serviceCenterId) {
        Client client = clientRepository.findByIdAndServiceCenterId(clientId, serviceCenterId)
            .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + clientId));
        client.unregisterCompletedOrder(revenue, profit);
        clientRepository.save(client);
    }

    private ClientResponse toResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setLastName(client.getLastName());
        response.setFirstName(client.getFirstName());
        response.setPatronymic(client.getPatronymic());
        response.setPhoneNumber(client.getPhoneNumber());
        response.setEmail(client.getEmail());
        response.setTelegramChatId(client.getTelegramChatId());
        response.setStatus(client.getStatus() != null ? client.getStatus().name() : null);
        response.setImportant(client.isImportant());
        response.setDiscountPercent(client.getDiscountPercent());
        response.setTotalOrders(client.getTotalOrders());
        response.setTotalSpent(client.getTotalSpent());
        response.setTotalProfit(client.getTotalProfit());
        response.setFirstVisitAt(client.getFirstVisitAt());
        response.setLastVisitAt(client.getLastVisitAt());
        return response;
    }
}
