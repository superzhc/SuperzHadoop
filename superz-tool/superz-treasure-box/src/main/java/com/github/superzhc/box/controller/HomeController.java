package com.github.superzhc.box.controller;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    private static final Double TILE_WIDTH = 150.0;
    private static final Double TILE_HEIGHT = 150.0;
    /* 系统信息 */
    private static final SystemInfo SYSTEM_INFO = new SystemInfo();

    @FXML
    private FlowPane home;

    private Tile osTile;
    private Tile processorTile;
    private Tile clockTile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 获取操作系统信息
        OperatingSystem os = SYSTEM_INFO.getOperatingSystem();

        HardwareAbstractionLayer hal = SYSTEM_INFO.getHardware();

        osTile =TileBuilder.create()
                .skinType(Tile.SkinType.TEXT)
                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                .title("操作系统")
                // .text("Whatever text")
                .description(String.valueOf(os))
                .descriptionAlignment(Pos.CENTER)
                .textVisible(true)
                .build();

        processorTile=TileBuilder.create()
                .skinType(Tile.SkinType.TEXT)
                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                .title("处理器信息")
                // .text("Whatever text")
                .description(hal.getProcessor().toString())
                .descriptionAlignment(Pos.CENTER)
                .textVisible(true)
                .build();

        // 内存信息
        GlobalMemory memory= hal.getMemory();

        // CPU使用信息
        CentralProcessor cpu= hal.getProcessor();

        clockTile = TileBuilder.create()
                .skinType(Tile.SkinType.CLOCK)
                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                .title("当前时间")
                .text("Whatever text")
                .dateVisible(true)
                .locale(Locale.CHINA)
                .running(true)
                .build();

        home.getChildren().addAll(osTile,clockTile);
    }
}
