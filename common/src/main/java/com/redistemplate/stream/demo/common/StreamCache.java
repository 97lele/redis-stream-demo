package com.redistemplate.stream.demo.common;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * @Author lulu
 * @Date 2020/9/20 22:11
 * 用于缓存stream信息
 */
@UtilityClass
public class StreamCache {

    private static Map<String, ReadWriteLock> streamKeyLockMap = new ConcurrentHashMap<>();

    private static Map<String, Map<String, Set<String>>> streamInfoMap = new HashMap<>();


    public static void createGroupCache(StreamCacheInfo info) {
        String streamName = info.streamName;
        wrapperRunnable(() -> createGroupAndReturnConsumers(streamName,info.groupName), streamName, false);
    }

    public static void createConsumerCache(StreamCacheInfo info) {
        String streamName = info.streamName;
        String groupName = info.groupName;
        wrapperRunnable(() -> {
            Map<String, Set<String>> group = createStreamAndReturnGroupMap(streamName);
            Set<String> consumers = createGroupAndReturnConsumers(streamName, groupName);
            consumers.add(info.consumerName);
            group.put(groupName, consumers);
        }, streamName, false);
    }

    public static void createStreamCache(StreamCacheInfo info) {
        String streamName = info.streamName;
        wrapperRunnable(() -> {
            createStreamAndReturnGroupMap(streamName);
        }, streamName, false);
    }


    public static boolean existsConsumerCache(StreamCacheInfo info) {
        String streamName = info.streamName;
        String groupName = info.groupName;
        return wrapperSpplier(() -> {
                    Map<String, Set<String>> groupMap = streamInfoMap.get(streamName);
                    if (Objects.isNull(groupMap)) {
                        return false;
                    }
                    Set<String> consumers = groupMap.get(groupName);
                    if (Objects.isNull(consumers)) {
                        return false;
                    }
                    return consumers.contains(info.consumerName);
                }
                , streamName, true);
    }

    public static boolean existsGroupCache(StreamCacheInfo info) {
        String streamName = info.streamName;
        return wrapperSpplier(() -> {
            Map<String, Set<String>> groupMap = streamInfoMap.get(streamName);
            if (Objects.isNull(groupMap)) {
                return false;
            }
            return groupMap.containsKey(info.groupName);
        }, streamName, true);
    }

    public static boolean existsStreamCache(StreamCacheInfo info) {
        return streamInfoMap.containsKey(info.streamName);
    }

    public static boolean deleteStreamCache(StreamCacheInfo info) {
        String streamName = info.streamName;
        Map<String, Set<String>> groupMap = streamInfoMap.get(streamName);
        if (Objects.isNull(groupMap)) {
            return false;
        }
        wrapperRunnable(() -> {
            groupMap.clear();
        }, streamName, false);
         streamInfoMap.remove(streamName) ;
         return true;
    }

    public  static boolean deleteGroupCache(StreamCacheInfo info){
        String streamName = info.streamName;
        Map<String, Set<String>> groupMap = streamInfoMap.get(streamName);
        if(Objects.isNull(groupMap)){
            return false;
        }
        return wrapperSpplier(()->{
            String groupName = info.groupName;
            Set<String> consumers = groupMap.get(groupName);
            if(Objects.isNull(consumers)){
                return false;
            }
            consumers.clear();
            consumers=null;
            groupMap.remove(groupName);
            return true;
        }, streamName,false);

    }

    public static boolean deleteConsumer(StreamCacheInfo info){
        String streamName = info.streamName;
        Map<String, Set<String>> groupMap = streamInfoMap.get(streamName);
        if(Objects.isNull(groupMap)){
            return false;
        }
        return wrapperSpplier(()->{
            String groupName = info.groupName;
            Set<String> consumers = groupMap.get(groupName);
            if(Objects.isNull(consumers)){
                return false;
            }
            consumers.remove(info.consumerName);
            return true;
        }, streamName,false);
    }


    private static <T> T wrapperSpplier(Supplier<T> supplier, String streamName, Boolean read) {
        ReadWriteLock currentStreamLock = streamKeyLockMap.putIfAbsent(streamName, new ReentrantReadWriteLock());
        if (read) {
            currentStreamLock.readLock().lock();
        } else {
            currentStreamLock.writeLock().lock();
        }
        T value = supplier.get();
        if (read) {
            currentStreamLock.readLock().unlock();
        } else {
            currentStreamLock.writeLock().unlock();
        }
        return value;
    }

    private static void wrapperRunnable(Runnable runnable, String stream, Boolean read) {
        ReadWriteLock currentStreamLock = streamKeyLockMap.putIfAbsent(stream, new ReentrantReadWriteLock());
        if (read) {
            currentStreamLock.readLock().lock();
        } else {
            currentStreamLock.writeLock().lock();
        }
        runnable.run();
        if (read) {
            currentStreamLock.readLock().unlock();
        } else {
            currentStreamLock.writeLock().unlock();
        }
    }

    private static Map<String, Set<String>> createStreamAndReturnGroupMap(String streamName) {
        Map<String, Set<String>> map = streamInfoMap.get(streamName);
        if (Objects.isNull(map)) {
            streamInfoMap.put(streamName, new HashMap<>());
        }
        return map;
    }

    private static Set<String> createGroupAndReturnConsumers(String streamName, String group) {
        Map<String, Set<String>> groupMap = createStreamAndReturnGroupMap(streamName);
        Set<String> consumers = groupMap.get(group);
        if (Objects.isNull(consumers)) {
            consumers = new HashSet<>();
            groupMap.put(group, consumers);
        }
        return consumers;
    }

}
