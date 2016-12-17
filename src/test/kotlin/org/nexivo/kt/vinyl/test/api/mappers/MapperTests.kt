package org.nexivo.kt.vinyl.test.api.mappers

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.vinyl.api.Record
import org.nexivo.kt.vinyl.api.dsl.DataMappings
import org.nexivo.kt.vinyl.api.mappers.Mapper

class MapperTests: Spek({

    describe("\"Mapper\" behavior") {

        given("a \"Mapper\"") {

            it("should parse and process a string input") {

                val actual: String = object: Record {

                    override val data: DataMappings = mapOf("value" to "Lorem ipsum dolor sit amet, consectetur adipiscing elit.")

                    val value: String by Mapper({ it.toUpperCase()})
                }.value

                val expected: String = "LOREM IPSUM DOLOR SIT AMET, CONSECTETUR ADIPISCING ELIT."

                actual.should.be.equal(expected)
            }
        }
    }
})