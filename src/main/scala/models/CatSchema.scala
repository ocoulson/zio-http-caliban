package models

import zio._

import java.net.URL

case class FindCatArgs(name: String)
case class AddCatArgs(cat: Cat)
case class EditCatPictureArgs(name: String, picUrl: URL)

case class Queries(
    listCats: UIO[List[Cat]],
    findCat: FindCatArgs => Task[Cat],
    randomCatPicture: UIO[String]
)

case class Mutations(
    addCat: AddCatArgs => UIO[Unit],
    editCatPicture: EditCatPictureArgs => Task[Unit]
)