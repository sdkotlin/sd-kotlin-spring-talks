import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

allprojects {
	group = "org.sdkotlin"
	version = "0.0.1-SNAPSHOT"
}

tasks {

	named<Wrapper>("wrapper").configure {
		gradleVersion = "7.6"
		distributionType = ALL
	}
}
