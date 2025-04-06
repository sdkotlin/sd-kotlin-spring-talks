package org.sdkotlin.buildlogic.actions

import org.gradle.api.Action

inline fun <reified T : Any> actions(
	vararg actions: Action<in T>
): Action<in T> =
	Action {
		actions.forEach { action ->
			action.execute(this)
		}
	}
