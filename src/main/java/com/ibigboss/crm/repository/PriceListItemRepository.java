package com.ibigboss.crm.repository;

import entity.pricelist.PriceListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PriceListItemRepository extends JpaRepository<PriceListItem, Long>, JpaSpecificationExecutor<PriceListItem> {
    List<PriceListItem> findByCategory(String category);
    List<PriceListItem> findByActiveTrue();
}
