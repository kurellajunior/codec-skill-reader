package com.cc.teskio.skillreader

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{ComponentScan, Configuration}
import org.springframework.scheduling.annotation.EnableScheduling

object Application {

  def main(args: Array[String]) {
    SpringApplication.run(classOf[Config])
  }

  @Configuration
  @EnableScheduling
  @SpringBootApplication
  @ComponentScan(Array("com.cc.teskio"))
  class Config {}

}
