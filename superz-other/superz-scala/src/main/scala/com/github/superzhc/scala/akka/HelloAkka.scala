package com.github.superzhc.scala.akka

import akka.actor.{ActorRef, ActorSystem, Props}

/**
 * @author superz
 * @create 2021/9/15 17:23
 */
object HelloAkka extends App {
  // 每个 Akka 应用程序都必须创建一个名为 ActorSystem 的类，这个类代表 Actor 系统，其中包含了 Actor 对象的层次结构，该系统中的所有 Actor 对象都会使用同一套配置。
  // ActorSystem 是运行 Actor 的容器并管理 Actors 它们的生命周期。
  val system=ActorSystem("helloAkka")

  // 当 Actor 系统被创建时，会创建一些 Actor 对象，其中包括 root 守护对象、user 守护对象和 system 守护对象。这些对象是 Actor 系统中所有其他 Actor 对象的最高监督者，应用程序创建的 Actor 对象位于 user 守护对象下方

  // 创建 Actor 对象并获取该对象的 ActorRef 引用
//  val processManagersRef:ActorRef=system.actorOf(Props[ProcessManagers],"processManagers")
}
