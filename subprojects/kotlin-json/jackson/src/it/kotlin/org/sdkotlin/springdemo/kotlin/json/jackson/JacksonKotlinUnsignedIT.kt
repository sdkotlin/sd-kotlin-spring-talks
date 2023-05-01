package org.sdkotlin.springdemo.kotlin.json.jackson

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ThrowingConsumer
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.json.JsonContent
import org.springframework.context.annotation.Configuration

@JsonTest
internal class JacksonKotlinUnsignedIT(
	@Autowired
	val signedTypesJacksonTester: JacksonTester<SignedTypesEntity>,
	@Autowired
	val unsignedTypesJacksonTester: JacksonTester<UnsignedTypesEntity>,
) {

	@Configuration
	internal class TestConfig

	@Test
	fun `test signed value serialization`() {

		val signedTypesEntity =
			SignedTypesEntity(
				signedInteger = 1,
				signedLong = 2,
			)

		val signedTypesJsonContent: JsonContent<SignedTypesEntity> =
			signedTypesJacksonTester.write(signedTypesEntity)

		assertThat(signedTypesJsonContent)
			.extractingJsonPathNumberValue("$.signedInteger")
			.isEqualTo(1)
		assertThat(signedTypesJsonContent)
			.extractingJsonPathNumberValue("$.signedLong")
			.satisfies(ThrowingConsumer { number ->
				assertThat(number.toLong()).isEqualTo(2L)
			})
	}

	@Test
	fun `test signed value deserialization`() {

		val signedTypesJson = """
			{
				"signedInteger": 1,
				"signedLong": 2
			}
		""".trimIndent()

		val content: SignedTypesEntity =
			signedTypesJacksonTester.parseObject(signedTypesJson)

		assertThat(content.signedInteger).isEqualTo(1)
		assertThat(content.signedLong).isEqualTo(2L)
	}

	@Test
	fun `test unsigned value serialization`() {

		val unsignedTypesEntity =
			UnsignedTypesEntity(
				unsignedInteger = 1u,
				unsignedLong = 2uL,
			)

		val unsignedTypesJsonContent: JsonContent<UnsignedTypesEntity> =
			unsignedTypesJacksonTester.write(unsignedTypesEntity)

		assertThat(unsignedTypesJsonContent)
			.extractingJsonPathNumberValue("$.unsignedInteger")
			.isEqualTo(1)
		assertThat(unsignedTypesJsonContent)
			.extractingJsonPathNumberValue("$.unsignedLong")
			.satisfies(ThrowingConsumer { number ->
				assertThat(number.toLong()).isEqualTo(2L)
			})
	}

	@Test
	@Disabled("https://github.com/FasterXML/jackson-module-kotlin/issues/650")
	fun `test unsigned value deserialization`() {

		val unsignedTypesJson = """
			{
				"unsignedInteger": 1,
				"unsignedLong": 2
			}
		""".trimIndent()

		val content: UnsignedTypesEntity =
			unsignedTypesJacksonTester.parseObject(unsignedTypesJson)

		assertThat(content.unsignedInteger).isEqualTo(1u)
		assertThat(content.unsignedLong).isEqualTo(2uL)
	}
}

data class SignedTypesEntity(
	val signedInteger: Int,
	val signedLong: Long,
)

data class UnsignedTypesEntity(
	val unsignedInteger: UInt,
	val unsignedLong: ULong,
)
