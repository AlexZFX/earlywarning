package com.alexzfx.earlywarninguser.repository;

import com.alexzfx.earlywarninguser.entity.InstCategory;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Alex
 * Date : 2018/4/9 10:53
 * Description :
 */
@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Integer> {
    Page<Instrument> findByCategoryAndModel(InstCategory category, boolean model, Pageable pageable);

    Page<Instrument> findByCategoryAndCreater(InstCategory category, User creater, Pageable pageable);

    Page<Instrument> findByCreater(User creater, Pageable pageable);

    Page<Instrument> findByCreaterAndNameLike(User creater, String name, Pageable pageable);

    Page<Instrument> findByModel(boolean model, Pageable pageable);

    Long countByCategory(InstCategory category);

    Page<Instrument> findByModelAndNameLike(boolean model, String name, Pageable pageable);
}
