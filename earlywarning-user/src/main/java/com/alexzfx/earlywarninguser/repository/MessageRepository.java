package com.alexzfx.earlywarninguser.repository;

import com.alexzfx.earlywarninguser.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/21 16:18
 * Description :
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Modifying
    @Query(value = "update Message m set m.isRead=true where m.id in ?1 and m.uid = ?2")
    void readMsgByIdsAndUid(List<Integer> ids, int uid);

    @Modifying
    @Query(value = "update Message m set m.isRead=true where m.uid = ?1")
    void readAllMsgByUid(Integer uid);

    Page<Message> findByUid(int uid, Pageable pageable);

    @Modifying
    @Query(value = "delete from Message m where m.id in ?1 and m.uid=?2")
    void deleteByIdsAndUid(List<Integer> ids, int uid);

    @Query(value = "select count (m.id) from Message m where m.uid=?1 and m.isRead=false")
    Long countByUidAndUnRead(int uid);

}
