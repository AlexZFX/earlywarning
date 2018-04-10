package com.alexzfx.earlywarning.repository;

import com.alexzfx.earlywarning.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Alex
 * Date : 2018/4/9 10:53
 * Description :
 */
@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Integer> {
}
