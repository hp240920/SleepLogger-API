package com.noom.interview.fullstack.sleep

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(basePackages = ["com.noom.interview.fullstack.sleep.entity"])
@EnableJpaRepositories(basePackages = ["com.noom.interview.fullstack.sleep.repository"])
@ComponentScan(basePackages = ["com.noom.interview.fullstack.sleep.*"])
class SleepApplication {
	companion object {
		const val UNIT_TEST_PROFILE = "unittest"
	}
}

fun main(args: Array<String>) {
	runApplication<SleepApplication>(*args)
}
