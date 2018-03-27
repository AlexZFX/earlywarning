package com.alexzfx.earlywarning.util.GsonUtil;

import com.alexzfx.earlywarning.util.GsonUtil.GsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Author : Alex
 * Date : 2018/3/20 20:16
 * Description :
 */
public class GsonIgnoreStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        Collection<Annotation> annotations = fieldAttributes.getAnnotations();
        return annotations.contains(GsonIgnore.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
