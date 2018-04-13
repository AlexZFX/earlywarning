package com.alexzfx.earlywarninguser.util.GsonUtil;

/**
 * Author : Alex
 * Date : 2018/3/21 10:47
 * Description : 参考  http://blog.csdn.net/RekaDowney/article/details/52292567?locationNum=14
 * 定义接收一个泛型E, 该泛型用来表示枚举类型，里面有两个抽象方法
 */
public interface GsonEnum<E> {

    /**
     * 表示将魅族序列化为字符串
     *
     * @return 序列化后得到的字符串
     */
    String serialize();

    /**
     * 将字符串反序列化成为枚举并返回特定的枚举E
     *
     * @param jsonEnum
     * @return 特点枚举类型
     */
    E deserialize(String jsonEnum);

    E parse(String type);

}
