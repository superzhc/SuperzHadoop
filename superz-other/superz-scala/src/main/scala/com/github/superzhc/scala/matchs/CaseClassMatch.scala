package com.github.superzhc.scala.matchs

/**
 * 样例类匹配
 *
 * @author superz
 * @create 2021/9/10 17:47
 */
object CaseClassMatch {
  def showNotification(notification: Notification): String = notification match {
    /* note：对于被忽略（不使用）的属性，可以使用下划线(_)替代 */
    case Email(sender, title, _) => s"You got an email from $sender with title: $title"
    case SMS(number, message) => s"You got an SMS from $number! Message: $message"
    case VoiceRecording(name, link) => s"you received a Voice Recording from $name! Click the link to hear it: $link"
  }

  /* 模式守卫 */
  def showImportantNotification(notification: Notification, importantPeopleInfo: Seq[String]): String = {
    notification match {
      case Email(sender, _, _) if importantPeopleInfo.contains(sender) =>
        "You got an email from special someone!"
      case SMS(number, _) if importantPeopleInfo.contains(number) =>
        "You got an SMS from special someone!"
      case other =>
        showNotification(other) // nothing special, delegate to our original showNotification function
    }
  }

  /* 仅匹配类型 */
  def typeMatch(notification: Notification) = notification match {
    case email: Email => s"This is a email,${email.body}"
    case sms: SMS => s"This is a sms,${sms.message}"
    case voiceRecording: VoiceRecording => s"This is a voice recording,${voiceRecording.link}"
  }

  def main(args: Array[String]): Unit = {
    val someSms = SMS("12345", "Are you there?")
    val someVoiceRecording = VoiceRecording("Tom", "voicerecording.org/id/123")

    println(showNotification(someSms))
    println(showNotification(someVoiceRecording))

    val importantPeopleInfo = Seq("867-5309", "jenny@gmail.com")

    val someSms2 = SMS("867-5309", "Are you there?")
    val someVoiceRecording2 = VoiceRecording("Tom", "voicerecording.org/id/123")
    val importantEmail = Email("jenny@gmail.com", "Drinks tonight?", "I'm free after 5!")
    val importantSms = SMS("867-5309", "I'm here! Where are you?")

    println(showImportantNotification(someSms2, importantPeopleInfo))
    println(showImportantNotification(someVoiceRecording2, importantPeopleInfo))
    println(showImportantNotification(importantEmail, importantPeopleInfo))
    println(showImportantNotification(importantSms, importantPeopleInfo))

    println(typeMatch(someSms2))
    println(typeMatch(importantEmail))
    println(typeMatch(someVoiceRecording2))
  }
}

sealed trait Notification

case class Email(sender: String, title: String, body: String) extends Notification

case class SMS(caller: String, message: String) extends Notification

case class VoiceRecording(contactName: String, link: String) extends Notification

