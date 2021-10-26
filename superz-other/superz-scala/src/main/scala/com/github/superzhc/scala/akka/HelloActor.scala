package com.github.superzhc.scala.akka

import akka.actor.Actor

/**
 * Actors 是 Akka 的执行单元。Actor 模型是一种抽象，它让编写正确的并发、并行和分布式系统更容易
 *
 * @author superz
 * @create 2021/9/15 19:12
 */
class HelloActor extends Actor {
  /**
   * 定义 Actor 和消息
   * 消息可以是任意类型（Object 的任何子类型），可以将装箱类型作为消息发送，也可以将普通数据结构（如数组和集合类型）作为消息发送
   *
   * 在定义 Actor 及其消息时，请记住以下建议：
   *
   * 1. 因为消息是 Actor 的公共 API，所以定义具有良好名称、丰富语义和特定于域的含义的消息是一个很好的实践，即使它们只是包装你的数据类型，这将使基于 Actor 的系统更容易使用、理解和调试。
   * 2. 消息应该是不可变的，因为它们在不同的线程之间共享。
   * 3. 将 Actor 的关联消息作为静态类放在 Actor 的类中是一个很好的实践，这使得理解 Actor 期望和处理的消息类型更加容易。
   * 4. 在 Actor 类中使用静态props方法来描述如何构造 Actor 也是一种常见的模式。
   */

  override def receive: Receive = null
}
