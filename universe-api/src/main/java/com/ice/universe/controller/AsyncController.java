package com.ice.universe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步测试controller
 * @ClassName AsyncController
 * @Description TODO
 * @Author liubin
 * @Date 2019/6/6 3:33 PM
 **/
@RestController
@RequestMapping("/test")
public class AsyncController {

    private  static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private static final String[] NEWS = {"男人看了会沉默，女人看了会流泪","震惊！某18岁少女十年前竟然才8岁","一生中必注意的十大误区"};

    /**
     * 请求进入后，通过异步任务处理。
     * 优点：长轮询下，可以解决高并发下同时占用tomcat服务器下线程的问题
     * @return
     */
    @RequestMapping("/async")
    public Object testAsync(HttpServletRequest request){
        DeferredResult<String> deferredResult = new DeferredResult<>();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = new Random().nextInt(2);
                deferredResult.setResult(NEWS[i]);
            }
        });
        return deferredResult;
    }

    /**问题：通过springMVC返回，处理返回结果后，会字段关闭连接（因为sse的重连机制，所以前端会不断请求）。
     * SSE：长连接，通过流方式传输数据(纯文本，注意格式)
     * 默认状态下，springmvc逻辑处理后请求完成，tomcat会主动关闭连接
     * @param request
     * @return
     */
    @RequestMapping(value = "/asyncSSE",produces = "text/event-stream;;charset=UTF-8")
    public String asyncSSE(HttpServletRequest request){
        DeferredResult<String> deferredResult = new DeferredResult<>();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        int i = new Random().nextInt(2);

        StringBuilder sb = new StringBuilder("retry:2000\n");
        sb.append("data").append(NEWS[i]).append("\n\n");
        return sb.toString();
    }

    /**
     * 测试SSE
     * 这里获取response对象后，手动返回内容（此处避免了经过springMVC处理返回数据，从而避免链接被springMVC管理直接被关闭）。
     * @param response
     */
    @RequestMapping(value = "/testSSE",produces = "text/event-stream;;charset=UTF-8")
    public void testSSE(HttpServletResponse response){
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            for(int i=0;++i<11;){
                if(writer.checkError()){
                    System.out.println("服务器连接已断开");
                    return;
                }
                Thread.sleep(2000);
                StringBuilder sb = new StringBuilder("retry:2000\n");
                sb.append("data").append("尝试写入第"+i+"次数据").append("\n\n");
                writer.write(sb.toString());
                writer.flush();
            }
            System.out.println("数据写入完毕，结束连接");
            writer.write("data:stop\n\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
