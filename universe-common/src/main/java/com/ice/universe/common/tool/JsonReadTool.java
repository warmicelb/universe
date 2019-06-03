package com.ice.universe.common.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ice.universe.common.exception.OpenException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author: ice
 * @create: 2018/12/20
 **/
public class JsonReadTool {

    private static final Logger logger = LoggerFactory.getLogger(JsonReadTool.class);

    /**
     * 文件转为jsonobject对象
     * @param filePath 文件路径
     * @return
     */
    public static JSONObject readToObject(String filePath){
        try {
            String jsonStr = IOUtils.toString(new FileReader(filePath));
            return JSON.parseObject(jsonStr);
        } catch (IOException e) {
            logger.error("json字符串解析失败");
            throw new OpenException("json字符串解析失败");
        }
    }

    /**
     * 文件转为jsonArray对象
     * @param filePath 文件路径
     * @return
     */
    public static JSONArray readToArray(String filePath){
        try {
            String jsonStr = IOUtils.toString(new FileReader(filePath));
            return JSON.parseArray(jsonStr);
        } catch (IOException e) {
            logger.error("json字符串解析失败");
            throw new OpenException("json字符串解析失败");
        }
    }
}
