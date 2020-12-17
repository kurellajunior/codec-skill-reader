package com.cc.teskio.skillstore

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation._


@Autowired
@RestController
class SkillStoreController {

  @RequestMapping(path = Array("/store"), method = Array(RequestMethod.GET))
  def getSkills(): ResponseEntity[String]  ={
    ResponseEntity.ok("""{"result":"ok"}""")
  }

}
