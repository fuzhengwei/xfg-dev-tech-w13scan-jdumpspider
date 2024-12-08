package cn.bugstack.xfg.dev.tech.test;

import cn.bugstack.xfg.dev.tech.infrastructure.dao.IUserOrderDao;
import cn.bugstack.xfg.dev.tech.infrastructure.po.UserOrderPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * https://www.eclipse.org/downloads/download.php?file=/mat/1.15.0/rcp/MemoryAnalyzer-1.15.0.20231206-macosx.cocoa.aarch64.dmg&mirror_id=1281
 * 官方网站:  https://www.eclipse.org/mat/
 * 下载地址: https://www.eclipse.org/mat/downloads.php
 * <p>
 * https://www.ej-technologies.com/download/jprofiler/files
 *
 * -Xms128M -Xmx128M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/github/xfg-dev-tech-dump/docs/dump
 *
 *  http://172.0.0.1:8091/api/actuator/heapdump
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IUserOrderDao userOrderDao;

    @Test
    public void test_insert() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                for (int j = 0; j < 500000; j++) {
                    try {
                        UserOrderPO userOrderPO = UserOrderPO.builder()
                                .userName("小傅哥")
                                .userId("xfg".concat(RandomStringUtils.randomNumeric(3)))
                                .userMobile("+86 13521408***")
                                .sku("13811216")
                                .skuName("《手写MyBatis：渐进式源码实践》")
                                .orderId(RandomStringUtils.randomNumeric(11))
                                .quantity(1)
                                .unitPrice(BigDecimal.valueOf(128))
                                .discountAmount(BigDecimal.valueOf(50))
                                .tax(BigDecimal.ZERO)
                                .totalAmount(BigDecimal.valueOf(78))
                                .orderDate(new Date())
                                .orderStatus(0)
                                .isDelete(0)
                                .uuid(UUID.randomUUID().toString().replace("-", ""))
                                .ipv4("127.0.0.1")
                                .ipv6("2001:0db8:85a3:0000:0000:8a2e:0370:7334".getBytes())
                                .extData("{\"device\": {\"machine\": \"IPhone 14 Pro\", \"location\": \"shanghai\"}}")
                                .build();

                        userOrderDao.insert(userOrderPO);
                    } catch (Exception ignore) {
                    }
                }

            }).start();
        }
        new CountDownLatch(1).await();
    }

    @Test
    public void test_java_heap_space_sql() throws InterruptedException {
        while (true){
            userOrderDao.queryPage();
        }
    }

    @Test
    public void test_java_heap_space_list() throws InterruptedException {
        List<byte[]> list = new ArrayList<>();
        while (true) {
            byte[] bytes = new byte[1024 * 1024 * 1024];
            list.add(bytes);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void test_thread_pool_java_heap_space() {
        // 创建一个固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            // 不断提交占用大量内存的任务
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                executorService.submit(new MemoryHogTask());
            }
        } catch (OutOfMemoryError e) {
            System.out.println("Caught OutOfMemoryError: " + e.getMessage());
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }
    }

    static class MemoryHogTask implements Runnable {
        @Override
        public void run() {
            try {
                // 分配一个大数组来占用内存
                int[] memoryHog = new int[1000000]; // 大约占用 4MB 内存
                // 模拟一些计算以避免 JIT 优化掉内存分配
                for (int i = 0; i < memoryHog.length; i++) {
                    memoryHog[i] = i;
                }
                // 保持任务在一定时间内占用内存
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
