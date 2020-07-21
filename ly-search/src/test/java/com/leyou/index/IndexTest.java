package com.leyou.index;

import com.leyou.search.LyEsApplication;
import com.leyou.search.client.CategoryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author Srd
 * @date 2020/7/22  0:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyEsApplication.class)
public class IndexTest {

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void test() {
        /**
         * 表面是执行的CategoryApi接口中的方法，实际上执行的还是item-service服务中CategoryController中的方法
         */
        List<String> nameByIds = categoryClient.queryNameByIds(Arrays.asList(1L, 2L, 3L));
        nameByIds.forEach(name -> {
            System.out.println("name = " + name);
        });
    }
}
