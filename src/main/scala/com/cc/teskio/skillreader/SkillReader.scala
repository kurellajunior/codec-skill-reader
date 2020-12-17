package com.cc.teskio.skillreader

import com.typesafe.scalalogging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import play.api.libs.json.Json
import scalaj.http.Http

import scala.util.{Success, Try}

@Service
@Autowired
class SkillReader() {

  val LOG: Logger = Logger[SkillReader]

  @Scheduled(fixedDelayString = "3600000")
  def fetchAndPublish(): Unit = {
    getUserIds // Seq("user-id")
      .flatMap(loadSkills) // Seq("user-id"-> Seq("skill"))

  }

  private def getUserIds: Try[Seq[String]] = {
    val query =
      """{"query": "query {
        |  organization(login:\"codecentric\") {
        |    name
        |    members:membersWithRole (first:3) {
        |      member:nodes { login id } } } }"
        |}""".stripMargin.replaceAll("\\s+", " ")
    val response = Http(gitGraphQL)
      .postData(query).header("content-type", "application/json")
      .timeout(connTimeoutMs = 1000, readTimeoutMs = 5000)
      .header("Authorization", s"bearer $gitPAT")
      .asString // TODO much error response handling possible: convert to Try

    // in case all is good
    Success(Json.parse(response.body) \ "data" \ "organization" \ "members" \ "member" \\ "id")
      .map(_.map(_.as[String]))
  }

  private def loadSkills(userIds :Seq[String]): Try[Seq[(String, Seq[String])]] = {
    Success(userIds.map(_ → Seq[String]()))
  }
}
