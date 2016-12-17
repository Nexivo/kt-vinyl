package org.nexivo.kt.vinyl.test.api

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.vinyl.api.Field
import org.nexivo.kt.vinyl.api.FieldType
import org.nexivo.kt.vinyl.api.dsl.`is a`
import org.nexivo.kt.vinyl.api.dsl.`looks like`

class FieldTests: Spek({

    describe("\"Field\" behavior") {

        given("a \"Field\" without a field name") {

            it("should not be captured") {

                val subject: Field = Field(FieldType.Companion.STRING_FIELD)
                val actual:  Boolean = subject.capture

                actual.should.be.`false`
                subject.toString().should.be.equal("string '[A-Za-z]+' [Not Captured, No Pattern]")
            }
        }

        given("a \"Field\" with a field name") {

            it("should be captured") {

                val subject: Field = Field(FieldType.Companion.STRING_FIELD, "Foo")
                val actual:  Boolean = subject.capture

                actual.should.be.`true`
                subject.toString().should.be.equal("string '[A-Za-z]+' [Foo, No Pattern]")
            }
        }

        given("a \"Field\" without a pattern") {

            it("should look like anything, but only match if valid") {

                val stringField: Field = Field(FieldType.Companion.STRING_FIELD)

                (""             `looks like` stringField).should.be.`true`
                ("  "           `looks like` stringField).should.be.`true`
                ("0,0-0,0"      `looks like` stringField).should.be.`true`
                ("Valid String" `looks like` stringField).should.be.`true`

                (""             `is a` stringField).should.be.`false`
                ("  "           `is a` stringField).should.be.`false`
                ("0,0-0,0"      `is a` stringField).should.be.`false`
                ("Valid String" `is a` stringField).should.be.`true`
            }
        }

        given("a \"Field\" with a pattern") {

            it("should look like and match if valid") {

                val stringField: Field = Field(FieldType.Companion.STRING_FIELD, pattern = "^STAT|STATUS$")

                ("0.0"    `looks like` stringField).should.be.`false`
                ("STAT"   `looks like` stringField).should.be.`true`
                ("STATUS" `looks like` stringField).should.be.`true`

                ("0.0"    `is a` stringField).should.be.`false`
                ("STAT"   `is a` stringField).should.be.`true`
                ("STATUS" `is a` stringField).should.be.`true`
            }
        }

        given("two instances of \"Field\"s with the same type, name and pattern") {

            it("should equal one another and have the same hashcode") {

                val subject1: Field = Field(FieldType.Companion.STRING_FIELD, "^STAT|STATUS$", "Foo")
                val subject2: Field = Field(FieldType.Companion.STRING_FIELD, "^STAT|STATUS$", "Foo")

                subject1.should.be.equal(subject2)

                subject1.hashCode().should.be.equal(subject2.hashCode())
            }
        }

        given("two separate instances of \"Field\"s with differing type, name and/or pattern") {

            it("should not equal one another, hash codes can not be guaranteed different") {

                val subject1: Field = Field(FieldType.Companion.STRING_FIELD, "^STAT|STATUS$", "Foo")
                val subject2: Field = Field(FieldType.Companion.STRING_FIELD, "^STAT$", "Fooey")

                subject1.should.not.be.equal(subject2)
            }
        }

        given("two separate instances of \"Field\"s with types of differing priorities") {

            it("should compare according to priority") {

                val subject1: Field = Field(FieldType.Companion.STRING_FIELD)
                val subject2: Field = Field(FieldType.Companion.LONG_FIELD)

                subject1.compareTo(subject2).should.be.below(0)
                subject2.compareTo(subject1).should.be.above(0)
                subject1.compareTo(subject1).should.be.equal(0)
                subject2.compareTo(subject2).should.be.equal(0)
            }
        }
    }
})