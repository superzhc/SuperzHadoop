package com.github.superzhc.kafka.jsdz.generator;

import com.github.superzhc.kafka.jsdz.dto.radarevent.ObjectPTCEventDetailDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author superz
 */
public class ObjectPTCEventDetailData extends EventDataGenerator<List<ObjectPTCEventDetailDTO>> {

    @Override
    protected List<ObjectPTCEventDetailDTO> eventDetail() {
        List<ObjectPTCEventDetailDTO> objectPTCEventDetailDTOS = new ArrayList<>();
        for (int i = 0, nums = 2 /*faker.random().nextInt(1, 5)*/; i < nums; i++) {
            ObjectPTCEventDetailDTO objectPTCEventDetailDTO = new ObjectPTCEventDetailDTO();
            objectPTCEventDetailDTO.setPtcType(faker.random().nextInt(1, 4));
            objectPTCEventDetailDTO.setPtcId(repeat("ptc_id", faker.random().nextInt(1, Integer.MAX_VALUE), 2));
            objectPTCEventDetailDTO.setSourceType(faker.random().nextInt(0, 5));
            objectPTCEventDetailDTO.setSpeed(faker.numberExt().randomFloat(2, 1, 120));
            objectPTCEventDetailDTO.setHeading(faker.numberExt().randomFloat(3, 1, 360));
            objectPTCEventDetailDTO.setLongAccele(faker.numberExt().randomFloat(2, 1, 120));
            objectPTCEventDetailDTO.setLatAccele(faker.numberExt().randomFloat(2, 1, 120));
            objectPTCEventDetailDTO.setVertAccele(faker.numberExt().randomFloat(2, 1, 120));
            objectPTCEventDetailDTO.setLength(faker.number().randomDouble(2, 1500, 2499));
            objectPTCEventDetailDTO.setWidth(faker.number().randomDouble(2, 300, 419));
            objectPTCEventDetailDTO.setTimestamp(faker.number().numberBetween(new Date(1970, 1, 1).getTime(), System.currentTimeMillis()));
            objectPTCEventDetailDTO.setLongitude(Double.valueOf(faker.address().longitude()));
            objectPTCEventDetailDTO.setLatitude(Double.valueOf(faker.address().latitude()));
            objectPTCEventDetailDTOS.add(objectPTCEventDetailDTO);
        }

        return objectPTCEventDetailDTOS;
    }

    public static void main(String[] args) {
        System.out.println(new ObjectPTCEventDetailData().convert2String());
    }
}
