package org.nexivo.kt.vinyl.test.api.mappers

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.vinyl.api.Record
import org.nexivo.kt.vinyl.api.dsl.DataMappings
import org.nexivo.kt.vinyl.api.mappers.dsl.ArrayMapper

class ArrayMapperTests : Spek({

    describe("\"ArrayMapper\" behavior") {

        given("a \"ArrayMapper\"") {

            it("should parse a string of space delimited \"String\"s") {

                val subject = object: Record {

                    override val data: DataMappings = mapOf("values" to "Lorem ipsum dolor sit amet, consectetur adipiscing elit.")

                    val values: Array<String> by ArrayMapper({ it.toUpperCase() })
                }

                val actual:   List<String>  = subject.values.toList()
                val expected: Array<String> = arrayOf("LOREM", "IPSUM", "DOLOR", "SIT", "AMET,", "CONSECTETUR", "ADIPISCING", "ELIT.")

                actual.should.have.all.elements(*expected)
            }
        }

        given("a \"ArrayMapper\" with a custom splitter") {

            it("should parse a string of custom delimited \"List\"") {

                val subject = object: Record {

                    override val data: DataMappings = mapOf("values" to "Lorem\tipsum\tdolor\tsit\tamet,\tconsectetur\tadipiscing\telit.")

                    val values: Array<String> by ArrayMapper({ it.toUpperCase()}, "\t")
                }
                val actual:   List<String>  = subject.values.toList()
                val expected: Array<String> = arrayOf("LOREM", "IPSUM", "DOLOR", "SIT", "AMET,", "CONSECTETUR", "ADIPISCING", "ELIT.")

                actual.should.have.all.elements(*expected)
            }
        }
    }
})