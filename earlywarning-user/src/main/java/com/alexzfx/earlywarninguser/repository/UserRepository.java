package com.alexzfx.earlywarninguser.repository;

import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/3/21 17:15
 * Description :
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    //TODO 检验正确性
    @Query(nativeQuery = true, value = "select u.id from user u " +
            "inner join user_role ur on u.id=ur.uid and ur.role_id=?1 " +// 角色为维修人员
            "left join inst_order io on u.id=io.maintainer_id and io.maintain_status <> ?2 " +//左联订单表，订单状态不为完成
            "group by u.id order by count(io.maintainer_id)" +
            "limit 5")
    List<Integer> findFreeMaintainer(Integer maintainId, MaintainStatus status);


    @Query("select u from User u inner join u.roles r where r.id=?1 and u.username like ?2")
    Page findByRoleIdAndUsernameLike(Integer rid, String username, Pageable pageable);

    @Query("select u from User u inner join u.roles r where r.id=?1")
    Page findByRoleId(Integer id, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "update user u left join user_role r on u.id=r.uid set u.is_locked=0 where u.id in (?1) and r.role_id <> ?2")
    void lockUsers(List<Integer> intIds, Integer rid);

    @Modifying
    @Query(value = "update User u set u.isLocked=1 where u.id in ?1")
    void unlockUsers(List<Integer> intIds);
}
