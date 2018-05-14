package com.alexzfx.earlywarningmachine.util;

import java.util.Random;

/**
 * Author : Alex
 * Date : 2018/4/15 10:06
 * Description : 数据产生策略
 */
public class DataUtil {

    //参数为上界
    public int random(int up) {
        return new Random().nextInt(up);
    }

}
