package com.github.superzhc.data.jsdz.generateor;

import com.alibaba.fastjson.JSON;
import com.github.superzhc.data.jsdz.dto.EventDto;
import com.github.superzhc.data.jsdz.dto.radarevent.ObjectPTCEventDetailDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ObjectPTCEventDetailData extends DataGenerator<EventDto<List<ObjectPTCEventDetailDTO>>> {

    @Override
    public EventDto<List<ObjectPTCEventDetailDTO>> generate() {
        List<ObjectPTCEventDetailDTO> objectPTCEventDetailDTOS = new ArrayList<>();
        for (int i = 0, nums = faker.random().nextInt(1, 5); i < nums; i++) {
            ObjectPTCEventDetailDTO objectPTCEventDetailDTO = new ObjectPTCEventDetailDTO();
            objectPTCEventDetailDTO.setPtcType(faker.random().nextInt(1, 4));
            objectPTCEventDetailDTO.setPtcId(faker.random().nextInt(1, 100));
            objectPTCEventDetailDTO.setSourceType(faker.random().nextInt(0, 5));
            objectPTCEventDetailDTO.setSpeed((float) faker.number().randomDouble(2, 1, 120));
            objectPTCEventDetailDTO.setHeading((float) faker.number().randomDouble(3, 1, 360));
            objectPTCEventDetailDTO.setLongAccele((float) faker.number().randomDouble(2, 1, 120));
            objectPTCEventDetailDTO.setLatAccele((float) faker.number().randomDouble(2, 1, 120));
            objectPTCEventDetailDTO.setVertAccele((float) faker.number().randomDouble(2, 1, 120));
            objectPTCEventDetailDTO.setLength(faker.number().randomDouble(2, 1500, 2500));
            objectPTCEventDetailDTO.setWidth(faker.number().randomDouble(2, 300, 420));
            objectPTCEventDetailDTO.setTimestamp(faker.number().numberBetween(new Date(1970, 1, 1).getTime(), System.currentTimeMillis()));
            objectPTCEventDetailDTO.setLongitude(Double.valueOf(faker.address().longitude()));
            objectPTCEventDetailDTO.setLatitude(Double.valueOf(faker.address().latitude()));
            objectPTCEventDetailDTOS.add(objectPTCEventDetailDTO);
        }

        EventDto<List<ObjectPTCEventDetailDTO>> eventDto = new EventDto<>();
        eventDto.setEventId(faker.idNumber().invalid());
        eventDto.setDeviceId(faker.idNumber().ssnValid());
        eventDto.setDeviceType(String.valueOf(faker.number().numberBetween(0, 5)));
        eventDto.setSceneId(faker.idNumber().validSvSeSsn());
        eventDto.setSceneType(String.valueOf(faker.number().numberBetween(0, 5)));
        eventDto.setRegionId(faker.number().numberBetween(1, 100));
        eventDto.setTimestamp(faker.number().numberBetween(new Date(1997, 1, 1).getTime(), System.currentTimeMillis()));
        eventDto.setLongitude(Double.valueOf(faker.address().longitude()));
        eventDto.setLatitude(Double.valueOf(faker.address().latitude()));
        eventDto.setAttachCount(0);
        eventDto.setEventDetail(objectPTCEventDetailDTOS);
        return eventDto;
    }

    @Override
    public String convert2String() {
        return JSON.toJSONString(generate());
    }

    public static void main(String[] args) {
        System.out.println(new ObjectPTCEventDetailData().convert2String());
    }
}
