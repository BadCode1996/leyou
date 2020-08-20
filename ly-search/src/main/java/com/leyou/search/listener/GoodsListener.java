package com.leyou.search.listener;

import com.leyou.search.service.IndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Srd
 * @date 2020/8/20  2:10
 */
@Component
public class GoodsListener {

    @Autowired
    private IndexService indexService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.create.index.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert", "item.update"}))
    public void listenCreate(Long id) throws Exception {
//        延迟500ms后执行
        Thread.sleep(500);
        if (id == null) {
            return;
        }
//        创建或更新索引
        this.indexService.createIndex(id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.delete.index.queue",durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = "item.delete"))
    public void listenDelete(Long id) throws Exception {
        Thread.sleep(500);
        if (id == null) {
            return;
        }
//        删除索引
        this.indexService.deleteIndex(id);
    }
}
