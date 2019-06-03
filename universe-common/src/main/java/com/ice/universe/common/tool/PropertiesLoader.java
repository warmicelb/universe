package com.ice.universe.common.tool;

import com.ice.universe.common.exception.OpenException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 配置文件加载类
 * @author: ice
 * @create: 2018/12/25
 **/
public class PropertiesLoader {

    private static Properties properties = null;

    /**
     * 获取配置
     * --根据key获取value
     * @param key
     * @return
     */
    public static String getPropertie(String key){
        if(properties==null){
            new PropertiesLoader().loadProperties();
        }
        return properties.getProperty(key);
    }

    /**
     * 加载配置文件
     * @return
     */
    private Properties loadProperties() {
        if(properties==null) {
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("classpath:conf/properties");
            try {
                properties.load(new InputStreamReader(inputStream,"utf-8"));
            } catch (IOException e) {
                throw new OpenException("properties配置文件加载错误");
            }
        }
        return properties;
    }
}
