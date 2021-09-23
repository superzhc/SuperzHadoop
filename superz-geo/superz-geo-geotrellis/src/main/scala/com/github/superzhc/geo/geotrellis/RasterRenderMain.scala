package com.github.superzhc.geo.geotrellis

import com.github.superzhc.geo.geotrellis.util.DateUtils
import geotrellis.raster
import geotrellis.raster.{ColorRamps, IntArrayTile}
import geotrellis.raster.render.{ColorRamp, Png, RGB}

/**
 * @author superz
 * @create 2021/9/13 19:43
 */
object RasterRenderMain {

  def render(): Unit = {
    // Generate the tile - let's paint it red with #FF0000FF
    // (red = 0xFF or 255; green = 0x00 or 0; blue = 0x00 or 0; and alpha = 0xFF or 255, which is completely opaque)
    val hexColored: IntArrayTile = IntArrayTile.fill(0xFF0000FF, 100, 100)

    // Making a PNG
    val png: Png = hexColored.renderPng
    // 直接返回字节数组
    // png.bytes
    // 直接写到文件中
    png.write(s"${GeoTrellisConfig.root}raster_render_demo_${DateUtils.format()}.png")

    // JPG variation
    // val jpg: Jpg = hexColorsHere.renderJpg
  }

  /**
   * 详细见：`https://geotrellis.readthedocs.io/en/latest/guide/rasters.html`
   *
   * @return
   */
  def colorRamp(): ColorRamp = {
    /**
     * Color Schemes
     */
    /*Blue to Orange*/
    ColorRamps.BlueToOrange
    /*Blue to Red*/
    ColorRamps.BlueToRed
    /*Green to Red-Orange*/
    ColorRamps.GreenToRedOrange

    /**
     * Sequential Color Schemes
     */
    /*Light to Dark - Sunset*/
    ColorRamps.LightToDarkSunset
    /*Light to Dark - Green*/
    ColorRamps.LightToDarkGreen
    /*Yellow to Red - Heatmap*/
    ColorRamps.HeatmapYellowToRed
    /*Blue to Yellow to Red Spectrum - Heatmap*/
    ColorRamps.HeatmapBlueToYellowToRedSpectrum
    /*Dark Red to Yellow-White - Heatmap*/
    ColorRamps.HeatmapDarkRedToYellowWhite
    /*Light Purple to Dark Purple to White*/
    ColorRamps.HeatmapLightPurpleToDarkPurpleToWhite

    /**
     * Qualitative or Categorical Schemes
     */
    /*Bold Land Use*/
    ColorRamps.ClassificationBoldLandUse
    /*Muted Terrain*/
    ColorRamps.ClassificationMutedTerrain

    /**
     * Viridis, Magma, Plasma and Inferno
     * */

    /**
     * Custom Color Ramps
     */
    /*方式一*/
    val colorRamp =
    ColorRamp(
      RGB(0, 255, 0),
      RGB(63, 255, 51),
      RGB(102, 255, 102),
      RGB(178, 255, 102),
      RGB(255, 255, 0),
      RGB(255, 255, 51),
      RGB(255, 153, 51),
      RGB(255, 128, 0),
      RGB(255, 51, 51),
      RGB(255, 0, 0)
    )
    /*方式二*/
    val colorRamp2 =
      ColorRamp(0xFF0000FF, 0x0000FFFF)
        .stops(100)
        .setAlphaGradient(0xFF, 0xAA)

    null
  }


  /**
   * ColorMap 实际上决定了瓦片的颜色和值的关系
   */
  def colorMap(): Unit ={

  }

  def main(args: Array[String]): Unit = {
    render()
  }
}
