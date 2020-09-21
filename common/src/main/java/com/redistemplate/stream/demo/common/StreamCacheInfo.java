package com.redistemplate.stream.demo.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @Author lulu
 * @Date 2020/9/20 22:25
 */
@Getter
@Setter
@Accessors(chain = true)
public class StreamCacheInfo {
     @NotEmpty(message = "流名字不能为空")
     String streamName;
     @NotEmpty(message = "消费组名字不能为空")
     String groupName;
     String consumerName;
}
