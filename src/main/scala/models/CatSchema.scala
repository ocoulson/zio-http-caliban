package models

import zio._
import services.CatsStoreService

import java.net.URL

case class FindCatArgs(name: String)
case class AddCatArgs(cat: Cat)
case class EditCatPictureArgs(name: String, picUrl: URL)

case class Queries(
    listCats: URIO[CatsStoreService, List[Cat]],
    findCat: FindCatArgs => RIO[CatsStoreService, Cat],
    randomCatPicture: URIO[CatsStoreService, String]
)

case class Mutations(
    addCat: AddCatArgs => URIO[CatsStoreService, Unit],
    editCatPicture: EditCatPictureArgs => RIO[CatsStoreService, Unit]
)