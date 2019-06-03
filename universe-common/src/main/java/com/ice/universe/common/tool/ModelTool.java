package com.ice.universe.common.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型转化工具
 * @DetaTime 2018-05-03 17:19:32
 * @author ice
 */
public class ModelTool {


    /**
     * 日志工具
     */
    private static Logger logger = LoggerFactory.getLogger(ModelTool.class);


    /**
     * 转换数据模型
     * -- 对象
     * @param source 需要转换的对象
     * @param targetClazz 需要转换成的对象
     * @param <T>
     * @return
     */
    public static <T> T convertDataModel(Object source, Class<T> targetClazz) {
        if (source == null) return null;
        try {
            T target = targetClazz.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 转换数据模型
     * -- 集合
     * @param sources 需要转换的集合
     * @param targetClazz 需要转换成的集合
     * @param <T>
     * @return
     */
    public static <T> List<T> convertDataModel(List<? extends Object> sources, Class<T> targetClazz) {
        if (sources == null) return null;
        List<T> targets = new ArrayList(sources.size());
        for (Object source : sources) {
            try {
                targets.add(convertDataModel(source, targetClazz));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return targets;
    }

}
