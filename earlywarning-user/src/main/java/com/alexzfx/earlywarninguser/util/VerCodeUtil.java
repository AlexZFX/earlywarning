package com.alexzfx.earlywarninguser.util;

import com.alexzfx.earlywarninguser.exception.BaseException;
import org.apache.shiro.session.Session;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class VerCodeUtil {

    // 图片高度
    private static final int IMG_HEIGHT = 100;
    // 图片宽度
    private static final int IMG_WIDTH = 30;
    // 验证码长度
    private static final int CODE_LEN = 4;

    // 验证码中所使用到的字符
    private static final char[] codeChar = "ABCDEFGHJKMNOPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz0123456".toCharArray();

    public static void getVerCode(Session session, OutputStream os) throws IOException {
        // 用于绘制图片，设置图片的长宽和图片类型（RGB)
        BufferedImage bi = new BufferedImage(IMG_HEIGHT, IMG_WIDTH, BufferedImage.TYPE_INT_RGB);
        // 获取绘图工具
        Graphics graphics = bi.getGraphics();
        graphics.setColor(new Color(200, 200, 200)); // 使用RGB设置背景颜色
        graphics.fillRect(0, 0, 100, 30); // 填充矩形区域
        StringBuilder captcha = new StringBuilder(); // 存放生成的验证码
        Random random = new Random();
        for (int i = 0; i < CODE_LEN; i++) { // 循环将每个验证码字符绘制到图片上
            int index = random.nextInt(codeChar.length);
            // 随机生成验证码颜色
            graphics.setColor(new Color(random.nextInt(150), random.nextInt(200), random.nextInt(255)));
            // 将一个字符绘制到图片上，并制定位置（设置x,y坐标）
            graphics.drawString(codeChar[index] + "", (i * 20) + 15, 20);
            captcha.append(codeChar[index]);
        }

        // 将生成的验证码code放入sessoin中
        session.setAttribute("verCode", captcha.toString());
        // 通过ImageIO将图片输出
        ImageIO.write(bi, "JPG", os);
    }

    public static Boolean checkVerCode(Session session, String verCode) {
        // 获取存放在session中的验证码
        String code = (String) session.getAttribute("verCode");
        if (code == null || verCode == null) {
            throw new BaseException(1000, "请先获取验证码");
        }
        // 获取页面提交的验证码
        return code.toLowerCase().equals(verCode.toLowerCase());
    }

    public static String getAuthCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

}


