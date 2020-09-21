package com.redistemplate.stream.demo.console.web;

import com.redistemplate.stream.demo.common.StreamCache;
import com.redistemplate.stream.demo.common.StreamCacheInfo;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public void createConsuemer(@RequestBody StreamCacheInfo streamCacheInfo, BindingResult result) {
        if(Strings.isBlank(streamCacheInfo.getConsumerName())){

        }
        StreamCache.createConsumerCache(streamCacheInfo);

    }

    @PostMapping("/createGroup")
    public void createGroup(@RequestParam String group, @RequestParam String streamKey) {


    }


    public StreamOperations getStreamOp() {
        return redisTemplate.opsForStream();
    }


}
