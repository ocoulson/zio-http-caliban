package services

import models.{Cat, Colour}
import zio._

import java.net.URL

import scala.util.{Try, Random}

class CatStore extends CatService {

  private def Url(s: String)= Try(new URL(s)).toOption
  private var data: List[Cat] = List(
      Cat("Faustus", List("Fluffy", "The Baws"), Url("https://i.natgeofe.com/n/3861de2a-04e6-45fd-aec8-02e7809f9d4e/02-cat-training-NationalGeographic_1484324.jpg"), Colour.Ginger),
      Cat("Mephisopheles", List("Smudge", "Mefi"), None, Colour.Black),
      Cat("Dave", List("The great horned one", "Lil' Dave"), Url("https://static.scientificamerican.com/sciam/cache/file/32665E6F-8D90-4567-9769D59E11DB7F26_source.jpg?w=590&h=800&7E4B4CAD-CAE1-4726-93D6A160C2B068B2"), Colour.Ginger),
      Cat("Licksworth", List("Sir"), Url("https://cdn.mos.cms.futurecdn.net/VSy6kJDNq2pSXsCzb6cvYF-1024-80.jpg.webp"), Colour.Tabby )
  )

  def listCats: UIO[List[Cat]] = UIO(data)

  def findCat(name: String): Task[Cat] = Task.fromEither(data.find(_.name == name).toRight(new Exception("NOPE"))) 

  def randomCatPicture: UIO[String] = UIO(
      data.flatMap(_.picUrl) match {
          case Nil => "No Pictures found"
          case withPics => withPics(Random.nextInt(withPics.size - 1)).toString
      }
  )

  def addCat(cat: Cat): UIO[Unit] = UIO{
      data = data :+ cat
  }

  def editCatPicture(name: String, picUrl: URL): Task[Unit] = UIO{
    data = data.map{
        case cat if cat.name == name => cat.copy(picUrl = Some(picUrl))
    }

  }
  
}
