package org.nexivo.kt.vinyl.test

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.vinyl.api.BaseRecord
import org.nexivo.kt.vinyl.api.DataMapper
import org.nexivo.kt.vinyl.api.dsl.*

class StandardMapperTests : Spek({

    describe("\"DataMapper\" behavior") {

        given("a \"DataMapper\" with a complete set of standard mappings") {

            it("should parse it's source data correctly") {

                val data: DataMappings = mapOf(
                        "BooleanValue"  to "true",
                        "BooleanValues" to "false true true",
                        "DoubleValue"   to "0.1",
                        "DoubleValues"  to "0.2 0.1 0.3",
                        "FloatValue"    to "0.1",
                        "FloatValues"   to "0.2 0.1 0.3",
                        "IntegerValue"  to "42",
                        "IntegerValues" to "24 42 44 22",
                        "LongValue"     to "2147483649",
                        "LongValues"    to "10 2147483649 1",
                        "StringValue"   to "Lorem",
                        "StringValues"  to "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                )

                val subject: DataMapper = DataMapper(data)
                val actual: CompleteStandardMappers = CompleteStandardMappers(subject)

                actual.BooleanValue.should.be.`true`
                actual.BooleanValues.toList().should.have.all.elements(false, true, true)
                
                actual.DoubleValue.should.equal(0.1)
                actual.DoubleValues.toList().should.have.all.elements(0.2, 0.1, 0.3)
                
                actual.FloatValue.should.equal(0.1F)
                actual.FloatValues.toList().should.have.all.elements(0.2F, 0.1F, 0.3F)
                
                actual.IntegerValue.should.equal(42)
                actual.IntegerValues.toList().should.have.all.elements(24, 42, 44, 22)
                
                actual.LongValue.should.equal(2147483649)
                actual.LongValues.toList().should.have.all.elements(10, 2147483649, 1)
                
                actual.StringValue.should.equal("Lorem")
                actual.StringValues.toList().should.have.all.elements("Lorem", "ipsum", "dolor", "sit", "amet,", "consectetur", "adipiscing", "elit.")
            }
        }
    }
}){
    class CompleteStandardMappers (data: DataMapper): BaseRecord(data.source) {

        val BooleanValue:  Boolean        by data.Boolean

        val BooleanValues: Array<Boolean> by data.Booleans

        val DoubleValue:   Double         by data.Double

        val DoubleValues:  Array<Double>  by data.Doubles

        val FloatValue:    Float          by data.Float

        val FloatValues:   Array<Float>   by data.Floats

        val IntegerValue:  Int            by data.Integer

        val IntegerValues: Array<Int>     by data.Integers

        val LongValue:     Long           by data.Long

        val LongValues:    Array<Long>    by data.Longs

        val StringValue:   String         by data.String

        val StringValues:  Array<String>  by data.Strings
    }
}