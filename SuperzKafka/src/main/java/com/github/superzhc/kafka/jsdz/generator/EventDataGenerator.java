package com.github.superzhc.kafka.jsdz.generator;

import com.alibaba.fastjson.JSON;
import com.github.superzhc.kafka.jsdz.dto.EventDto;
import com.github.superzhc.data.generator.DataGenerator;

import java.util.Date;

/**
 * @author superz
 * @create 2021/3/26 15:36
 */
public abstract class EventDataGenerator<T> extends DataGenerator<EventDto<T>> {
    @Override
    public EventDto<T> generate() {
        EventDto<T> eventDto = new EventDto<>();
        eventDto.setEventId(eventId());
        eventDto.setDeviceId(deviceId());
        eventDto.setDeviceType(deviceType());
        eventDto.setSceneId(sceneId());
        eventDto.setSceneType(sceneType());
        eventDto.setRegionId(regionId());
        eventDto.setTimestamp(faker.number().numberBetween(new Date(1997, 1, 1).getTime(), System.currentTimeMillis()));
        eventDto.setLongitude(longitude());
        eventDto.setLatitude(latitude());
        eventDto.setAttachCount(0);
        eventDto.setEventDetail(eventDetail());
        return eventDto;
    }

    protected String eventId(){
        return faker.idNumber().invalid();
    }

    protected String deviceType(){
        return String.valueOf(faker.number().numberBetween(0, 5));
    }

    protected String deviceId(){
        return faker.idNumber().ssnValid();
    }

    protected String sceneId(){
        return faker.idNumber().validSvSeSsn();
    }

    protected String sceneType(){
        return String.valueOf(faker.number().numberBetween(0, 5));
    }

    protected Integer regionId(){
        return faker.number().numberBetween(1, 100);
    }

    protected Double longitude(){
        return Double.valueOf(faker.address().longitude());
    }

    protected Double latitude(){
        return Double.valueOf(faker.address().latitude());
    }

    protected abstract T eventDetail();

    @Override
    public String convert2String() {
        return JSON.toJSONString(generate());
    }
}
