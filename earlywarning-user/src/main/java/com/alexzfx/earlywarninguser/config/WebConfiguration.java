package com.alexzfx.earlywarninguser.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/3/20 15:53
 * Description :
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${web.upload-path}")
    private String rootPath;

    //排除jackson的依赖，将Gson设置为json处理器
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter); // 删除MappingJackson2HttpMessageConverter
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
//        config.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //关闭对循环引用的处理，即防止出现ref
        config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
        converter.setFastJsonConfig(config);
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(converter);
//        //将自定义的ExclusionStrategy(排除策略)添加到GsonBuilder的属性中去。
//        GsonBuilder gsonBuilder = new GsonBuilder().addSerializationExclusionStrategy(new GsonIgnoreStrategy());
////        GsonBuilder gsonBuilder = new GsonBuilder().setExclusionStrategies(new GsonIgnoreStrategy());
//        converters.add(new GsonHttpMessageConverter(gsonBuilder.create())); // 添加GsonHttpMessageConverter
    }

//    @Bean
//    public Gson gson() {
//        return new GsonBuilder().addSerializationExclusionStrategy(new GsonIgnoreStrategy()).create();
//    }

    @Bean(value = {"userSocketPath"})
    public String userSocketPath() {
        return "/msg/user";
    }

    @Bean(value = {"maintainerSocketPath"})
    String maintainerSocketPath() {
        return "/msg/maintainer";
    }

    @Bean(value = {"RootPath"})
    public String RootPath() {
        return rootPath;
    }


}
