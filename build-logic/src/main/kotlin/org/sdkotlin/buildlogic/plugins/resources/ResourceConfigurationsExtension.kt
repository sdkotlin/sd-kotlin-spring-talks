package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.provider.ListProperty

interface ResourceConfigurationsExtension {
	val resourceConfigurations: ListProperty<String>
}
