package org.nexivo.kt.vinyl.test.api

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.xgiven
import org.nexivo.kt.vinyl.api.BaseRecord
import org.nexivo.kt.vinyl.api.dsl.DataMappings

class BaseRecordTests : Spek({

    describe("\"BaseRecord\" behavior") {

        given("a \"BaseRecord\" with a map of values") {

            it("should stringify the key, value pairs") {

                val data: DataMappings = mapOf("Enabled" to "true", "Foo" to "Junk")
                val subject: BaseRecord = object: BaseRecord(data) {}
                val actual:   String              = subject.toString()
                val expected: String              = "Enabled:true\nFoo:Junk"

                actual.should.be.equal(expected)
            }
        }

        given("a \"BaseRecord\" with a map of values, with a custom formatter and delimiter") {

            it("should custom stringify the key, value pairs") {

                val data: DataMappings = mapOf("Enabled" to "true", "Foo" to "Junk")
                val subject: BaseRecord = object: BaseRecord(data, "|", { "${it.key}[${it.value}]" }) {}
                val actual:   String              = subject.toString()
                val expected: String              = "Enabled[true]|Foo[Junk]"

                actual.should.be.equal(expected)
            }
        }

        xgiven("two instances of \"BaseRecord\"s with two different maps, that contain the same key value pairs") {

            it("should equal one another, even if their formatter and/or delimiters differ") {

                val data1: DataMappings = mapOf("Enabled" to "true", "Foo" to "Junk")
                val subject1: BaseRecord = object: BaseRecord(data1, "|", { "${it.key}[${it.value}]" }) {}

                val data2: DataMappings = mapOf("Foo" to "Junk", "Enabled" to "true")
                val subject2: BaseRecord = object: BaseRecord(data2) {}

// the following statement, which is the main test, is unexpectedly failing
                subject1.should.be.equal(subject2)
            }
        }
    }
})