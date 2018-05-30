package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.InstOrder;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.repository.InstOrderRepository;
import com.alexzfx.earlywarninguser.repository.RoleRepository;
import com.alexzfx.earlywarninguser.repository.UserRepository;
import com.alexzfx.earlywarninguser.service.OrderService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;

/**
 * Author : Alex
 * Date : 2018/4/22 16:52
 * Description :
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final InstOrderRepository instOrderRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final BaseException PermissionDenied;

    private final BaseException NotFoundError;

    @Autowired
    public OrderServiceImpl(InstOrderRepository instOrderRepository, RoleRepository roleRepository, UserRepository userRepository, BaseException PermissionDenied, BaseException NotFoundError) {
        this.instOrderRepository = instOrderRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.PermissionDenied = PermissionDenied;
        this.NotFoundError = NotFoundError;
    }

    @Override
    public Page getOrderList(Pageable pageable) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        Page<InstOrder> page;
        //如果是用户，就把维修者初始化，反之把所有者初始化。
        if (user.getRoleNames().contains("admin")) {
            page = instOrderRepository.findAll(pageable);
        } else if (user.getRoleNames().contains("maintainer")) {
            page = instOrderRepository.findByMaintainerId(user.getId(), pageable);
            User user1;//用户
            for (InstOrder order : page.getContent()) {
                user1 = userRepository.findById(order.getOwnerId()).get();
//                user1.setRoles(null); //会直接删除掉
                order.setOwner(user1);
            }
        } else {
            page = instOrderRepository.findByOwnerId(user.getId(), pageable);
            User maintainer;
            for (InstOrder order : page.getContent()) {
                maintainer = userRepository.findById(order.getMaintainerId()).get();
                order.setMaintainer(maintainer);
            }
        }
        return page;
    }

    @Override
    public InstOrder getOrderDetail(Long id) {
        InstOrder order;
        try {
            order = instOrderRepository.findById(id).get();
        } catch (EntityNotFoundException | NoSuchElementException e) {
            throw NotFoundError;
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getId().equals(order.getOwnerId()) || user.getId().equals(order.getMaintainerId())) {
            if (user.getRoleNames().contains("user")) {
                User maintainer = userRepository.findById(order.getMaintainerId()).get();
                order.setMaintainer(maintainer);
            } else {
                User user1 = userRepository.findById(order.getOwnerId()).get();
                order.setOwner(user1);
            }
            return order;
        } else if (user.getRoleNames().contains("admin")) {
            User maintainer = userRepository.findById(order.getMaintainerId()).get();
            order.setMaintainer(maintainer);
            User user1 = userRepository.findById(order.getOwnerId()).get();
            order.setOwner(user1);
            return order;
        } else {
            throw PermissionDenied;
        }
    }

    @Override
    public Page<InstOrder> getOrderListByStatus(MaintainStatus maintainStatus, Pageable pageable) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        Page<InstOrder> page = null;
        //如果是用户，就把维修者初始化，反之把所有者初始化。
        if (user.getRoleNames().contains("admin")) {
            page = instOrderRepository.findByMaintainStatus(maintainStatus, pageable);
        } else if (user.getRoleNames().contains("maintainer")) {
            page = instOrderRepository.findByMaintainerIdAndMaintainStatus(user.getId(), maintainStatus, pageable);
            User user1;//用户
            for (InstOrder order : page.getContent()) {
                user1 = userRepository.findById(order.getOwnerId()).get();
                order.setOwner(user1);
            }
        } else if (user.getRoleNames().contains("user")) {
            page = instOrderRepository.findByOwnerIdAndMaintainStatus(user.getId(), maintainStatus, pageable);
            User maintainer;
            for (InstOrder order : page.getContent()) {
                maintainer = userRepository.findById(order.getMaintainerId()).get();
                order.setMaintainer(maintainer);
            }
        }
        return page;
    }

    @Override
    public Page<InstOrder> getOrderListByUidAndStatus(Integer uid, Integer status, Pageable pageable) {
        Page<InstOrder> page;
        try {
            if (uid == null) {
                if (status == null) {
                    page = instOrderRepository.findAll(pageable);
                } else {
                    page = instOrderRepository.findByMaintainStatus(MaintainStatus.getStatusByNum(status), pageable);
                }
//                for (InstOrder order : page.getContent()) {
//                    order.setOwner(userRepository.findById(order.getOwnerId()).get());
//                    order.setMaintainer(userRepository.findById(order.getMaintainerId()).get());
//                }
            } else { //uid ! = null时
                User user = userRepository.findById(uid).get();
                if (user.getRoleNames().contains("user")) {
                    if (status != null) {
                        page = instOrderRepository.findByOwnerIdAndMaintainStatus(uid, MaintainStatus.getStatusByNum(status), pageable);
                    } else {
                        page = instOrderRepository.findByOwnerId(uid, pageable);
                    }
//                for (InstOrder order : page.getContent()) {
//                    order.setOwner(user);
//                    order.setMaintainer(userRepository.findById(order.getMaintainerId()).get());
//                }
                } else if (user.getRoleNames().contains("maintainer")) {
                    if (status != null) {
                        page = instOrderRepository.findByMaintainerIdAndMaintainStatus(uid, MaintainStatus.getStatusByNum(status), pageable);
                    } else {
                        page = instOrderRepository.findByMaintainerId(uid, pageable);
                    }
//                for (InstOrder order : page.getContent()) {
//                    order.setMaintainer(user);
//                    order.setOwner(userRepository.findById(order.getOwnerId()).get());
//                }
                } else {
                    return null;
                }
            }
        } catch (NoSuchElementException | EntityNotFoundException e) {
            throw NotFoundError;
        }
        return page;
    }

}
