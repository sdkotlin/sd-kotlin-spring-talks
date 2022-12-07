package org.sdkotlin.springboot3demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBoot3DemoApplication

fun main(args: Array<String>) {
	runApplication<SpringBoot3DemoApplication>(*args)
}
