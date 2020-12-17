package com.cc.teskio.skillstore

import org.scalatest.{FunSuite, Matchers}

/**
 */
class SkillStoreControllerTest extends FunSuite with Matchers{

  test("testAddSkills") {
    val storeController = new SkillStoreController
    storeController.skillStore should have size 0
    storeController.addSkills("github", "jku", Set("java"))
    storeController.skillStore.get("github").flatMap(_.get("jku")).get shouldEqual(Set("java"))
  }

  test("testUpdateSkills") {
    val storeController = new SkillStoreController
    storeController.skillStore should have size 0
    storeController.addSkills("github", "jku", Set("java"))
    storeController.addSkills("github", "jku", Set("scala"))
    storeController.skillStore.get("github").flatMap(_.get("jku")).get shouldEqual(Set("scala"))
  }

  test("testKeepOtherSkills") {
    val storeController = new SkillStoreController
    storeController.skillStore should have size 0
    storeController.addSkills("github", "jku", Set("scala"))
    storeController.addSkills("github", "eric", Set("java"))
    storeController.addSkills("stackoverflow", "eric", Set("java"))
    storeController.skillStore.get("github").flatMap(_.get("jku")).get shouldEqual(Set("scala"))
  }

  test("testGetSkillsSimple") {
    val storeController = new SkillStoreController
    storeController.getResponse("jku") shouldEqual("""{"id":"jku","skills":[]}""")
    storeController.addSkills("github", "jku", Set("java"))
    storeController.getResponse("jku") shouldEqual("""{"id":"jku","skills":["java"]}""")
  }
}
