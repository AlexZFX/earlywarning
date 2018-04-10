package com.alexzfx.earlywarning.repository;

import com.alexzfx.earlywarning.entity.InstCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Alex
 * Date : 2018/4/9 10:52
 * Description :
 */
@Repository
public interface CategoryRepository extends JpaRepository<InstCategory, Integer> {
}
