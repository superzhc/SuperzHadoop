package com.github.superzhc.data.jsdz.generator;

import com.github.superzhc.data.jsdz.dto.radarevent.ObjectPTCEventDetailDTO;

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
