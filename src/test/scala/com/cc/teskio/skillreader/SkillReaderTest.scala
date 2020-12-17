package com.cc.teskio.skillreader

import com.cc.teskio.skillstore.SkillStoreController
import org.scalatest.{FunSuite, Matchers}

/**
 */
class SkillReaderTest extends FunSuite with Matchers{

  test("runReader") {
    val skillStore = new SkillStoreController
    val skillReader = new SkillReader(skillStore)
    skillReader.fetchAndPublish()
    skillStore.getResponse("s7nio") should include("Ruby")
  }
}
