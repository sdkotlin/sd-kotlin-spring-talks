package org.sdkotlin.springdemo.componentscannedservice

import org.springframework.stereotype.Service

fun interface ComponentScannedService {

	fun doIt(): String
}

@Service
internal class ComponentScannedServiceImpl : ComponentScannedService {

	override fun doIt(): String = "You've been scanned."
}
