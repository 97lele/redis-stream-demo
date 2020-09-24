package com.redistemplate.stream.demo.console.web;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import com.redistemplate.stream.demo.common.*;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author lulu
 * @Date 2020/9/20 21:55
 */
@RestController
@AllArgsConstructor
public class ConsumerManagerController {

    private final RedisTemplate redisTemplate;
    private final NacosServiceDiscovery discovery;
    private static final String SUCCESS = "OK";

    @PostMapping("/createConsumer")
    public ResponseEntity createConsuemer(@Valid @RequestBody StreamCacheInfo streamCacheInfo, BindingResult result) {
        if (Strings.isBlank(streamCacheInfo.getConsumerName())) {
            return ResponseEntity.error(ErrorEnums.CONSUMER_NAME_NOT_NULL);
        }
        if (result.hasFieldErrors()) {
            return ResponseEntity.error(result.getFieldError().getDefaultMessage(), ErrorEnums.PARAM_ERROR.getCode());
        }
        if (!StreamCache.existsConsumerCache(streamCacheInfo)) {
            if (getCreateGroupResult(streamCacheInfo).equals(SUCCESS)) {
                //这里不能通过xreadgroup创建消费者，可能会把消息加入待处理列表或消息丢失
                StreamCache.createConsumerCache(streamCacheInfo);
            }
        }
        return ResponseEntity.success(streamCacheInfo);
    }

    @PostMapping("/createGroup")
    public ResponseEntity createGroup(@RequestBody StreamCacheInfo streamCacheInfo, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.error(result.getFieldError().getDefaultMessage(), ErrorEnums.PARAM_ERROR.getCode());
        }
        if (!StreamCache.existsGroupCache(streamCacheInfo)) {
            if (getCreateGroupResult(streamCacheInfo).equals(SUCCESS)) {
                //生成组名
                StreamCache.createGroupCache(streamCacheInfo);
            }
        }
        return ResponseEntity.success(streamCacheInfo);
    }

    @GetMapping("/getConsumerInstance")
    public ResponseEntity getConsumer() throws NacosException {
        List<ServiceInstance> instances = discovery.getInstances("redis-stream-demo-consumer");
        return ResponseEntity.success(instances);
    }

    private String getCreateGroupResult(StreamCacheInfo streamCacheInfo) {
        String streamName = streamCacheInfo.getStreamName();
        String groupName = streamCacheInfo.getGroupName();
        //如果其他线程访问了redis，则会被取消
        redisTemplate.watch(streamName);
        redisTemplate.multi();
        StreamOperations streamOperations = redisTemplate.opsForStream();
        if (!StreamCache.existsGroupCache(streamCacheInfo)) {
            String mark = streamCacheInfo.mark();
            ReadOffset offset = ReadOffset.from(mark);
            //从参数获取从哪里开始监听 有自定义、开始、最新
            streamOperations.createGroup(streamName, offset, groupName);
        }
        List exec = redisTemplate.exec();
        return exec.get(0).toString();
    }

}
