package com.ice.universe.common.test;

import com.alibaba.fastjson.JSON;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * 请求接口数据模拟，修改模拟对象即可
 * @ClassName TestMock
 * @Description TODO
 * @Author liubin
 * @Date 2019/4/28 2:58 PM
 **/
public class MockInterface {
    public static void main(String[] args) {
        PodamFactory factory = new PodamFactoryImpl();
        Object user = factory.manufacturePojo(Object.class);
        System.out.println(JSON.toJSON(user));
    }
}
