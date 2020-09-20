package com.redistemplate.stream.demo.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author lulu
 * @Date 2020/9/20 22:25
 */
@Getter
@Setter
@Accessors(chain = true)
public class StreamCacheInfo {
     @NonNull
     String streamName;
     String groupName;
     String consumerName;
}
