package com.cc.teskio.skillstore

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.{RequestMapping, _}
import play.api.libs.json._

@Autowired
@Service
@RestController
class SkillStoreController {

  /** mapping source ⇒ id ⇒ skills */
  var skillStore: Map[String, Map[String, Set[String]]] = Map();

  @RequestMapping(path = Array("/store/{id}"), method = Array(RequestMethod.GET))
  def getSkills(@PathVariable("id") id: String): ResponseEntity[String] = {
    ResponseEntity.ok(getResponse(id))
  }

  def getResponse(id:String): String = {
    val skills = skillStore.filter((x) ⇒ {x._2.keySet.contains(id)})
      .foldLeft(Set[String]())((target, entry) ⇒ target ++ entry._2(id))
    Json.stringify(Json.obj("id" → id, "skills" → skills))
  }

  /** this will be later callled from the POST controller, once this is a single service */
  def addSkills(source: String, id: String, skills: Set[String]): Unit = {
    skillStore = skillStore + (source → (skillStore.getOrElse(source, Map()) + (id → skills)))
  }

}
