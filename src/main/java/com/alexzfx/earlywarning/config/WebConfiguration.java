package com.alexzfx.earlywarning.config;

import com.alexzfx.earlywarning.util.GsonUtil.GsonIgnoreStrategy;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/3/20 15:53
 * Description :
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @PostConstruct
    public void init(){

    }

    //排除jackson的依赖，将Gson设置为json处理器
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter); // 删除MappingJackson2HttpMessageConverter
        //将自定义的ExclusionStrategy(排除策略)添加到GsonBuilder的属性中去。
        GsonBuilder gsonBuilder = new GsonBuilder().addSerializationExclusionStrategy(new GsonIgnoreStrategy());
        converters.add(new GsonHttpMessageConverter(gsonBuilder.create())); // 添加GsonHttpMessageConverter
    }

    @Bean
    public String RootPath(){
        //TODO 路径
        return "F:\\earlywarning";
    }


}
