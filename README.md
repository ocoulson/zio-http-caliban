## Simple GraphQL API with Caliban and ZIO-Http using Scala 3

### Usage

A simple `sbt run` will run a http service on port 8090 locally

HTTP GET to `/schema` gives you the GraphQL schema

HTTP POST to `/graphQL` to access the API

All code of interest is in `src/main/scala/CatApp.scala`