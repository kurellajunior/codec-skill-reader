package com.cc.teskio.skillreader

import com.typesafe.scalalogging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import play.api.libs.json.{JsArray, JsObject, Json}
import scalaj.http.Http

import scala.util.{Success, Try}

@Service
@Autowired
class SkillReader() {

  val LOG: Logger = Logger[SkillReader]

  @Scheduled(fixedDelayString = "3600000")
  def fetchAndPublish(): Unit = {
    val skillsets = getUserIds // Seq("user-id")
      .flatMap(loadAllSkills) // Seq("user-id"-> Seq("skill"))
  }

  private def getUserIds: Try[Seq[String]] = {
    val query =
      """{"query": "query {
        |  organization(login:\"codecentric\") {
        |    name
        |    members:membersWithRole (first:3) {
        |      member:nodes { login id } } } }"
        |}""".stripMargin.replaceAll("\\s+", " ")
    val response = queryGraphQL(query)

    // in case all is good
    Success(Json.parse(response.body) \ "data" \ "organization" \ "members" \ "member" \\ "id")
      .map(_.map(_.as[String]))
  }

  private def queryGraphQL(query: String) = {
    val response = Http(gitGraphQL)
      .postData(query).header("content-type", "application/json")
      .timeout(connTimeoutMs = 1000, readTimeoutMs = 5000)
      .header("Authorization", s"bearer $gitPAT")
      .asString // TODO much error response handling possible: convert to Try
    response
  }

  private def loadAllSkills(userIds :Seq[String]): Try[Seq[(String, Seq[String])]] = {
    val userIdQueryString = userIds.map("\\\"" + _ + "\\\"").mkString("[", " ", "]")
    val query =
      s"""{"query": "query {
        | nodes(ids: $userIdQueryString) {
        |    ... on User {
        |      login
        |      repositories(first: 3) {
        |        repos: nodes {
        |          nameWithOwner
        |          languages(first: 10) {
        |            langs: nodes {skill:name}
        |  } } } } } }"
        |}""".stripMargin.replaceAll("\\s+", " ")
    val response = queryGraphQL(query)

    Success(
      (Json.parse(response.body) \ "data" \ "nodes")
        .as[JsArray].value
        .map(_.as[JsObject])
        .map(toSkillSet)
    )
  }

  private def toSkillSet(userInfo : JsObject): (String, Seq[String]) = {
    val login = (userInfo \ "login").as[String]
    val skills = (userInfo \\ "skill").map(_.as[String])
    (login → skills)
  }
}
