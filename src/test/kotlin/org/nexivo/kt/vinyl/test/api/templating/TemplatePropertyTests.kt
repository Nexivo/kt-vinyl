package org.nexivo.kt.vinyl.test.api.templating

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.specifics.infixit.apply
import org.nexivo.kt.vinyl.api.templating.RecordTemplate
import org.nexivo.kt.vinyl.api.templating.TemplateProperty
import java.util.*

class TemplatePropertyTests: Spek({

    describe("\"TemplateProperty\" behavior") {

        given("a \"TemplateProperty\"") {

            it("should set the values of the referenced \"RecordTemplate\"") {

                val dateTime: Calendar = Calendar.getInstance()
                val epoch1:   Long          = dateTime.timeInMillis

                dateTime.add(Calendar.DATE, 1)

                val epoch2:  Long           = dateTime.timeInMillis

                val target = object : RecordTemplate("S,TIME,[rec id],[epoch]", "\t") {

                    var `rec id`: String      by TemplateProperty()

                    var epoch:    Array<Long> by TemplateProperty()
                } apply {

                    `rec id` = "#453"
                    epoch    = arrayOf(epoch1, epoch2)
                }

                val actual:   String = target.toString()
                val expected: String = "S,TIME,#453,$epoch1\t$epoch2"

                target.parameters.keys.containsAll(listOf("rec id", "epoch"))
                target.`rec id`.should.be.equal("#453")

                actual.should.be.equal(expected)
            }
        }
    }
})
