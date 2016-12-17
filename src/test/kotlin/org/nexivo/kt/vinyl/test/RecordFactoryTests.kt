package org.nexivo.kt.vinyl.test

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.vinyl.RecordFactory
import org.nexivo.kt.vinyl.api.*
import org.nexivo.kt.vinyl.api.dsl.Long
import java.util.*

class RecordFactoryTests: Spek({

    describe("\"RecordFactory\" behavior") {

        given ("a \"RecordFactory\"") {

            it("should correctly parse a record it contains a corresponding \"RecordType\"") {

                val status:     Field         = Field(FieldType.Companion.STRING_FIELD, "s|S")
                val statusType: Field         = Field(FieldType.Companion.STRING_FIELD, "TIME")
                val epoch:      Field         = Field(FieldType.Companion.LONG_FIELD,   Field.NO_PATTERN, "EpochTime")
                val timeStatus: RecordType    = RecordType(::TimeStatus, "Time Status", status, statusType, epoch)
                val factory:    RecordFactory = RecordFactory(::DataMapper, timeStatus)
                val expected:   Long          = Calendar.getInstance().timeInMillis
                val actual:     TimeStatus    = factory.parse("s,TIME,$expected") as TimeStatus

                actual.EpochTime.should.be.equal(expected)
            }
        }
    }
}) {

    class TimeStatus (data: DataMapper): BaseRecord(data.source) {
        val EpochTime: Long by data.Long
    }
}