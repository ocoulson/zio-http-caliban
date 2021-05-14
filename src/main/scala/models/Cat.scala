package models

import java.net.URL

case class Cat(name: String, nicknames: List[String], picUrl: Option[URL], colour: Colour)
