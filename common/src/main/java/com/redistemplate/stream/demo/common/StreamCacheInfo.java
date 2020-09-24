package com.redistemplate.stream.demo.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

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
    boolean start;
    String id;

    public String mark() {
        if (Objects.nonNull(id)) {
            return id;
        }
        if (this.start) {
            return StreamMarkEnums.START.getValue();
        }
        return StreamMarkEnums.LAST.getValue();
    }
}
