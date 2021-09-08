/* 创建一个本目录的 Project 工程定义 */
lazy val root = (project in file("."))
  /* Project 工程对应一个不可变的映射表来描述工程 */
  .settings(
    /* 项目的名称 */
    name := "superz-scala",
    // name := baseDirectory.value.getName,
    organization := "com.github.superzhc",
    /* 项目的版本 */
    version := "0.2.0",
    /* 项目所使用的 scala 版本 */
    scalaVersion := "2.11.8"
  )