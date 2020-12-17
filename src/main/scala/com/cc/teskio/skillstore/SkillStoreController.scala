package com.cc.teskio.skillstore

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.{RequestMapping, _}


@Autowired
@Service
@RestController
class SkillStoreController {

  /** mapping source ⇒ id ⇒ skills */
  var skillStore: Map[String, Map[String, Set[String]]] = Map();

  @RequestMapping(path = Array("/store"), method = Array(RequestMethod.GET))
  def getSkills(): ResponseEntity[String] = {
    ResponseEntity.ok(s"""{"result":"ok", "count":${skillStore.size}""")
  }

  /** this will be later callled from the POST controller, once this is a single service */
  def addSkills(source: String, id: String, skills: Set[String]): Unit = {
    skillStore = skillStore + (source → (skillStore.getOrElse(source, Map()) + (id → skills)))
  }

}
