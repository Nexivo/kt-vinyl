package org.nexivo.kt.vinyl.test.api

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.nexivo.kt.vinyl.api.*
import org.nexivo.kt.vinyl.api.dsl.Long
import org.nexivo.kt.vinyl.test.throws

class RecordTypeTests: Spek({

    describe("\"RecordType\" behavior") {

        given("two instances of \"RecordType\"s with the same templating, type name and fields") {

            it("should equal one another and have the same hashcode") {

                val statusType: Field      = Field(FieldType.Companion.STRING_FIELD, pattern = "TIME")
                val epoch:      Field      = Field(FieldType.Companion.LONG_FIELD,   "EpochTime")
                val status:     Field      = Field(FieldType.Companion.STRING_FIELD, pattern = "s|S")
                val subject1:   RecordType = RecordType(::TimeStatus, "Time Status", status, statusType, epoch)
                val subject2:   RecordType = RecordType(::TimeStatus, "Time Status", status, statusType, epoch)

                subject1.should.be.equal(subject2)

                subject1.hashCode().should.be.equal(subject2.hashCode())

                subject1.toString().should.be.equal("Time Status:\n   string '[A-Za-z]+' [Not Captured, s|S]\n   string '[A-Za-z]+' [Not Captured, TIME]\n   long '^\\s*(?:\\+|-)?\\s*\\d+\\s*$' [EpochTime, No Pattern]")
            }
        }

        given("two instances of \"RecordType\"s with the different templating, type name and/or fields") {

            it("should not equal one another, hash codes can not be guaranteed different") {

                val statusType: Field      = Field(FieldType.Companion.STRING_FIELD, pattern = "TIME")
                val epoch:      Field      = Field(FieldType.Companion.LONG_FIELD,   "EpochTime")
                val status:     Field      = Field(FieldType.Companion.STRING_FIELD, pattern = "s|S")
                val subject1:   RecordType = RecordType(::TimeStatus, "Time Status", status, statusType)
                val subject2:   RecordType = RecordType(::TimeStatus, "Time Status", status, statusType, epoch)

                subject1.should.not.be.equal(subject2)
            }
        }

        on("attempting to instantiate a \"RecordType\" with a blank type") {

            it("should throw an \"IllegalArgumentException\"") {

                throws<IllegalArgumentException>("'type' can not be blank!") {
                    RecordType(::TimeStatus, "   ")
                }
            }
        }

        on("attempting to instantiate a \"RecordType\" without defining any fields") {

            it("should throw an \"IllegalArgumentException\"") {

                throws<IllegalArgumentException>("you must define at least one 'field'!") {
                    RecordType(::TimeStatus, "Time Status")
                }
            }
        }
    }
}) {
    class TimeStatus (data: DataMapper): BaseRecord(data.source) {
        val EpochTime: Long by data.Long
    }
}