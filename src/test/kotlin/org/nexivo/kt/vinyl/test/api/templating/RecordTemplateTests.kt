package org.nexivo.kt.vinyl.test.api.templating

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.specifics.infixit.apply
import org.nexivo.kt.vinyl.api.templating.RecordTemplate
import org.nexivo.kt.vinyl.api.templating.dsl.`string template`
import java.util.*

class RecordTemplateTests : Spek({

    describe("\"RecordTemplate\" behavior") {

        given("a \"RecordTemplate\" with fields and matching parameters") {

            it("should create a \"String\" with fields replaced with matching parameter values") {

                val epoch:   String         = Calendar.getInstance().timeInMillis.toString()
                val subject: RecordTemplate = object: RecordTemplate("S,TIME,[rec id],[epoch]") {} apply {

                    parameters.put("rec id", "#453")
                    parameters.put("epoch",  epoch)
                }

                val actual:   String = subject.toString()
                val expected: String = "S,TIME,#453,$epoch"

                actual.should.be.equal(expected)
            }
        }

        given("a \"RecordTemplate\" with \"Array<T>\" fields and matching parameters") {

            it("should create a \"String\" with fields replaced with matching parameter values") {

                val dateTime: Calendar = Calendar.getInstance()
                val epoch1:   Long          = dateTime.timeInMillis

                dateTime.add(Calendar.DATE, 1)

                val epoch2:  Long           = dateTime.timeInMillis
                val subject: RecordTemplate = object: RecordTemplate("S,TIME,[rec id],[epoch]", "\t") {} apply {

                    parameters.put("rec id", "#453")
                    parameters.put("epoch",  arrayOf(epoch1, epoch2))
                }

                val actual:   String = subject.toString()
                val expected: String = "S,TIME,#453,$epoch1\t$epoch2"

                actual.should.be.equal(expected)
            }
        }

        given("a \"RecordTemplate\" with \"Iterable<T>\" fields and matching parameters") {

            it("should create a \"String\" with fields replaced with matching parameter values") {

                val dateTime: Calendar = Calendar.getInstance()
                val epoch1:   Long          = dateTime.timeInMillis

                dateTime.add(Calendar.DATE, 1)

                val epoch2:  Long           = dateTime.timeInMillis
                val subject: RecordTemplate = object: RecordTemplate("S,TIME,[rec id],[epoch]", "\t") {} apply {

                    parameters.put("rec id", "#453")
                    parameters.put("epoch",  listOf(epoch1, epoch2))
                }

                val actual:   String = subject.toString()
                val expected: String = "S,TIME,#453,$epoch1\t$epoch2"

                actual.should.be.equal(expected)
            }
        }

        given("a \"RecordTemplate\" with non \"Iterable<T>\" or \"Array<T>\" fields and matching parameters") {

            it("should not create a \"String\" with fields replaced with matching parameter values") {

                val dateTime: Calendar = Calendar.getInstance()
                val epoch1:   Long          = dateTime.timeInMillis

                dateTime.add(Calendar.DATE, 1)

                val epoch2: Long   = dateTime.timeInMillis
                val actual: String = { object: RecordTemplate("S,TIME,[rec id],[epoch]", "\t") {} } `string template` {

                    val values: LongArray = LongArray(2) apply {
                        this[0] = epoch1
                        this[1] = epoch2
                    }

                    parameters.put("rec id", "#453")
                    parameters.put("epoch",  values)
                }

                val expected: String = "S,TIME,#453,$epoch1\t$epoch2"

                actual.should.not.be.equal(expected)
            }
        }
    }
})
