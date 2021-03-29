package com.github.superzhc.data.jsdz.generator;

import com.github.superzhc.data.jsdz.dto.bayonetPass.BayonetPassEventDetailDTO;

/**
 * @author superz
 * @create 2021/3/26 15:53
 */
public class BayonetPassEventDetailData extends EventDataGenerator<BayonetPassEventDetailDTO> {
    private static final String[] DEVICE_ID_DATAS = {"111-11-11", "222-22-22", "333-33-33", "444-44-444", "555-55-555", "666-66-666", "777-77-777", "888-88-888", "999-99-999"};

    @Override
    protected BayonetPassEventDetailDTO eventDetail() {
        BayonetPassEventDetailDTO data = new BayonetPassEventDetailDTO();
        data.setLaneId(faker.number().numberBetween(1, 6));
        data.setPlateNumber(faker.car().licensePlate());
        data.setPlateColor(faker.number().numberBetween(1, 6));
        data.setVehicleSpeed(faker.number().numberBetween(0, 120));
        data.setVehicleSpeedKM(faker.number().numberBetween(0, 120));
        data.setVehicleType(faker.number().numberBetween(1, 6));
        data.setVehicleTypeDesc("");
        data.setVehicleColor(faker.number().numberBetween(1, 100));
        return data;
    }

    @Override
    protected String deviceId() {
        return faker.options().option(DEVICE_ID_DATAS);
    }

    public static void main(String[] args) {
        System.out.println(new BayonetPassEventDetailData().convert2String());
    }
}
