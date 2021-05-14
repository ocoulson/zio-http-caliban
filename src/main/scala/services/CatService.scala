package services

import models.Cat
import zio._

import java.net.URL

trait CatService {
  def listCats: UIO[List[Cat]]
  def findCat(name: String): Task[Cat]
  def randomCatPicture: UIO[String]
  def addCat(cat: Cat): UIO[Unit]
  def editCatPicture(name: String, picUrl: URL): Task[Unit]
}
