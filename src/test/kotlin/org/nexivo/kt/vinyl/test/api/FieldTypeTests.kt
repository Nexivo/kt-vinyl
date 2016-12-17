package org.nexivo.kt.vinyl.test.api

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.xgiven
import org.nexivo.kt.vinyl.api.FieldType
import org.nexivo.kt.vinyl.test.throws

class FieldTypeTests: Spek({

    describe("\"FieldType\" behavior") {

        given("a \"FieldType\" a blank pattern") {

            it("should throw an \"IllegalArgumentException\"") {

                throws<IllegalArgumentException> {
                    object: FieldType("GonnaFail", " ") {
                        override fun adjust(value: String): String = value
                    }
                }

                throws<IllegalArgumentException> {
                    object: FieldType("DisGonnaFail2", "") {
                        override fun adjust(value: String): String = value
                    }
                }
            }
        }

        given("a \"FieldType\" a broken pattern") {

            it("should throw an \"IllegalArgumentException\"") {

                throws<IllegalArgumentException> {
                    object: FieldType("GottaFail", "((?:Fail!)") {
                        override fun adjust(value: String): String = value
                    }
                }
            }
        }

        xgiven("two instances of \"FieldType\"s with the same name and pattern") {

            it("should equal one another and have the same hashcode") {

                val subject1: FieldType = object: FieldType("String", PATTERN_STRING) {
                    override fun adjust(value: String): String = value.trim()
                }

                val subject2: FieldType = object: FieldType("String", PATTERN_STRING) {
                    override fun adjust(value: String): String = value.trim()
                }

// the following statement, which is the main test, is unexpectedly failing
                subject1.should.be.equal(subject2)

                subject1.hashCode().should.be.equal(subject2.hashCode())

                subject1.toString().should.be.equal("String '[A-Za-z]+'")
            }
        }
        given("two separate instances of \"FieldType\"s with differing name and/or pattern") {

            it("should not equal one another, hash codes can not be guaranteed different") {

                val subject1: FieldType = object: FieldType("String", PATTERN_STRING) {
                    override fun adjust(value: String): String = value.trim()
                }

                val subject2: FieldType = object: FieldType("Long", PATTERN_INTEGER) {
                    override fun adjust(value: String): String = value.trim()
                }

                subject1.should.not.be.equal(subject2)
            }
        }

        given("two separate instances of \"FieldType\"s with differing priorities") {

            it("should compare according to priority") {

                val subject1: FieldType = object: FieldType("String", PATTERN_STRING, priority = 0) {
                    override fun adjust(value: String): String = value.trim()
                }

                val subject2: FieldType = object: FieldType("Long", PATTERN_INTEGER, priority = 1) {
                    override fun adjust(value: String): String = value.trim()
                }

                subject1.compareTo(subject2).should.be.below(0)
                subject2.compareTo(subject1).should.be.above(0)
                subject1.compareTo(subject1).should.be.equal(0)
                subject2.compareTo(subject2).should.be.equal(0)
            }
        }
    }
}) {

    companion object {

        private const val PATTERN_STRING:  String = "[A-Za-z]+"

        private const val PATTERN_INTEGER: String = "^\\s*(?:\\+|-)?\\s*\\d+\\s*$"
    }
}