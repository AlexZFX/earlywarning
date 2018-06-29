package com.alexzfx.earlywarninguser.repository;

import com.alexzfx.earlywarninguser.entity.InstOrder;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Author : Alex
 * Date : 2018/4/20 11:26
 * Description :
 */
@Repository
public interface InstOrderRepository extends JpaRepository<InstOrder, Long> {
    //    List<Integer>
    @Query(nativeQuery = true, value = "select id from inst_order where inst_id = ?1 and maintain_status <> ?2")
    Long findIsFixing(int instId, int maintainStatus);

    Page<InstOrder> findByMaintainStatus(MaintainStatus status, Pageable pageable);

    Page<InstOrder> findByOwnerId(Integer ownerId, Pageable pageable);

    Page<InstOrder> findByMaintainerId(Integer maintainerId, Pageable pageable);

    Page<InstOrder> findByOwnerIdAndMaintainStatus(Integer ownerId, MaintainStatus status, Pageable pageable);

    Page<InstOrder> findByMaintainerIdAndMaintainStatus(Integer maintainerId, MaintainStatus status, Pageable pageable);

    InstOrder findByInstrumentAndMaintainStatus(Instrument instrument, MaintainStatus status);
}
