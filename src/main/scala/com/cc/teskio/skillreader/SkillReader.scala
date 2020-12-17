package com.cc.teskio.skillreader

import com.typesafe.scalalogging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@Autowired
class SkillReader() {

  val LOG: Logger = Logger[SkillReader]

  @Scheduled(fixedDelayString = "3600000")
  def fetchAndPublish(): Unit = {}

}
