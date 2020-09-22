package com.redistemplate.stream.demo.console.web;

import com.redistemplate.stream.demo.common.ErrorEnums;
import com.redistemplate.stream.demo.common.ResponseEntity;
import com.redistemplate.stream.demo.common.StreamCache;
import com.redistemplate.stream.demo.common.StreamCacheInfo;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author lulu
 * @Date 2020/9/20 21:55
 */
@RestController
@AllArgsConstructor
public class ConsumerManagerController {
    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/createConsumer")
    public ResponseEntity createConsuemer(@Valid @RequestBody StreamCacheInfo streamCacheInfo, BindingResult result) {
        if (Strings.isBlank(streamCacheInfo.getConsumerName())) {
            return ResponseEntity.error(ErrorEnums.CONSUMER_NAME_NOT_NULL);
        }
        if(result.hasFieldErrors()){
            return ResponseEntity.error(result.getFieldError().getDefaultMessage(),ErrorEnums.PARAM_ERROR.getCode());
        }
        if(!StreamCache.existsConsumerCache(streamCacheInfo)){
            String streamName = streamCacheInfo.getStreamName();
            String groupName = streamCacheInfo.getGroupName();
            redisTemplate.multi();
            StreamOperations streamOperations = redisTemplate.opsForStream();
            if(!StreamCache.existsGroupCache(streamCacheInfo)){

                streamOperations.createGroup(streamName, groupName);
            }
            //添加消费者
            Consumer consumer = Consumer.from(groupName,streamCacheInfo.getConsumerName());
            streamOperations.read(consumer)
            StreamCache.createConsumerCache(streamCacheInfo);
        }


    }

    @PostMapping("/createGroup")
    public void createGroup(@RequestParam String group, @RequestParam String streamKey) {


    }


    public StreamOperations getStreamOp() {
        return redisTemplate.opsForStream();
    }


}
