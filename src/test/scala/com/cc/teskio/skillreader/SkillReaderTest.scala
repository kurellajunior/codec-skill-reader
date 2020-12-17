package com.cc.teskio.skillreader

import org.scalatest.FunSuite

/**
 */
class SkillReaderTest extends FunSuite {

  test("runReader") {
    val skillReader = new SkillReader
    skillReader.fetchAndPublish();
  }
}
