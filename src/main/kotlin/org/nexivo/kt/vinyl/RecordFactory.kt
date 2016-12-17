package org.nexivo.kt.vinyl

import org.nexivo.kt.specifics.flow.ifSet
import org.nexivo.kt.specifics.infixit.apply
import org.nexivo.kt.vinyl.api.Field
import org.nexivo.kt.vinyl.api.FieldType
import org.nexivo.kt.vinyl.api.Record
import org.nexivo.kt.vinyl.api.RecordType
import org.nexivo.kt.vinyl.api.dsl.MapperClass
import java.util.*

class RecordFactory(mapper: MapperClass, vararg recordTypes: RecordType) {

    private val _dataMapper: MapperClass = mapper

    private class RecordMatcher(recordTypes: Array<out RecordType>) {

        private class Bookmark(val recordType: RecordType, val depth: Int) {

            val fields: LinkedList<Field>
                by lazy {
                    LinkedList(recordType.fields.filterIndexed { index: Int, _ -> index > recordType.fields.size - depth - 1 })
                }

            override fun toString(): String  = "${recordType.type}:\n   ${fields.joinToString("\n   ")}"
        }

        typealias Bookmarks          = LinkedHashMap<Field, Bookmark>
        typealias FieldsByType       = TreeMap<FieldType, MutableList<Field>>
        typealias NextRecordMatchers = LinkedHashMap<Field, RecordMatcher>
        typealias RecordTypes        = LinkedHashMap<Field, RecordType>

        private val _nextMatchers: Lazy<NextRecordMatchers> = lazy { NextRecordMatchers() }
        private val _fieldsByType: Lazy<FieldsByType>       = lazy { FieldsByType() }
        private val _recordTypes:  Lazy<RecordTypes>        = lazy { RecordTypes() }
        private val _bookmarks:    Lazy<Bookmarks>          = lazy { Bookmarks() }

        init { add(recordTypes) }

        private constructor(recordType: RecordType, fields: LinkedList<Field>) : this(arrayOf()) { add(recordType, fields) }

        fun match(source: Iterable<String>): RecordType?
            = match(source.iterator())

        fun match(source: Iterator<String>, next: String? = null): RecordType? {

            if (next != null || source.hasNext())
            {
                val value: String = next ?: source.next()

                if (_bookmarks.isInitialized()) {
                    _bookmarks.value.keys
                        .find { it.looksLike(value) } ifSet {
                            _bookmarks.value[it]?.recordType ifSet { return@match it }
                        }
                }

                if (_fieldsByType.isInitialized()) {
                    _fieldsByType.value
                            .filter { it.key.isType(value) }
                            .forEach {
                        val fields: List<Field> = it.value

                        fields.filter { it.looksLike(value) }
                              .forEach {
                            field: Field ->

                            if (_recordTypes.isInitialized()) {
                                _recordTypes.value[field] ifSet { return@match it }
                            }

                            if (_nextMatchers.isInitialized() && source.hasNext()) {
                                val nextValue: String = source.next()

                                _nextMatchers.value.keys
                                    .filter { it.matches(nextValue) }
                                    .first { return@match _nextMatchers.value[it]?.match(source, nextValue) }
                            }
                        }
                    }
                }
            }

            return null
        }

        private fun add(recordTypes: Array<out RecordType>): Unit = recordTypes.forEach { add(it) }

        private fun add(recordType: RecordType): Unit = add(recordType, LinkedList(recordType.fields))

        private fun add(recordType: RecordType, fields: LinkedList<Field>): Unit {

            if (fields.isEmpty()) { return@add }

            val field: Field = fields.remove()

            if (_bookmarks.isInitialized()) {
                val bookMark: Bookmark? = _bookmarks.value.remove(field)

                if (bookMark != null) {
                    registerField(field)

                    addChild(bookMark.recordType, bookMark.fields/*, field*/)
                }
            }

            // optimize only if more than one field remaining
            // and no fields have been registered
            // or this field has not been registered
            if (fields.size > 1 && (!_fieldsByType.isInitialized() || !(_fieldsByType.value[field.type]?.contains(field) ?: false)) ) {
                _bookmarks.value.put(field, Bookmark(recordType, fields.size))

                return@add
            }

            registerField(field)

            if (fields.isNotEmpty()) {
                addChild(recordType, fields/*, field*/)
            } else {

                if (_recordTypes.value[field] != null) {
                    TODO("Define Exceptions - duplicate ${recordType.type} for $field")
                }

                _recordTypes.value.put(field, recordType)
            }
        }

        private fun addChild(recordType: RecordType, fields: LinkedList<Field>/*, field: F*/) {

            val next: Field = fields.peek()

/*
            if (field.list == Field.VARIABLE_LIST) {
                TODO("Define Exception - Variable lists need to be the last field ${recordType.type} has $next after $field!")
            }
*/

            if (_nextMatchers.value.containsKey(next)) {
                _nextMatchers.value[next]!!.add(recordType, fields)
            } else {
                _nextMatchers.value.put(next, RecordMatcher(recordType, fields))
            }
        }

        private fun registerField(field: Field) {

            _fieldsByType.value.getOrPut(field.type) { mutableListOf() }

            _fieldsByType.value[field.type]!! apply {
                if (!contains(field)) {
                    add(field)
                }
            }
        }

        private fun buildString(builder: StringBuilder, level: Int) {

            with (builder) {
                val indent: String = " ".repeat(level * 3)

                if (_fieldsByType.isInitialized()) {
                    for ((_, fields: List<Field>) in _fieldsByType.value) {
                        fields.forEach {
                            append(indent)
                            append(it)

                            if (_recordTypes.isInitialized() && _recordTypes.value.isNotEmpty()) {
                                val recordType: RecordType? = _recordTypes.value[it]

                                append(" :: ")
                                append(recordType)
                            }

                            if (_nextMatchers.isInitialized() && _nextMatchers.value.isNotEmpty()) {
                                appendln()
                                for ((_, recordMatcher: RecordMatcher) in _nextMatchers.value) {
                                    recordMatcher.buildString(this, level + 1)
                                }
                            } else {
                                appendln()
                            }
                        }
                    }
                }

                if (_bookmarks.isInitialized() && _bookmarks.value.size > 0) {
                    append(indent)
                    appendln("BOOKMARKED")
                    append(" ".repeat(level * 3 + 3))

                    val thirdLevel: String = " ".repeat(level * 3 + 6)

                    for ((field: Field, bookmark: Bookmark) in _bookmarks.value) {
                        appendln(field)
                        appendln(bookmark.toString().split("\n").map { "$thirdLevel$it" }.joinToString("\n"))
                    }
                }
            }
        }

        override fun toString(): String = buildString { buildString(this, 0) }
    }

    private val _root: RecordMatcher

    init {

        val dupes: List<String>
            = recordTypes.groupBy { it.type }
                         .filter  { it.value.size > 1 }
                         .map     { it.key }

        if (dupes.isNotEmpty()) {
            throw IllegalArgumentException("Record types must be unique - Duplicate(s): '${dupes.joinToString(", ")}'!")
        }

        _root = RecordMatcher(recordTypes)
    }

    fun parse(source: String, delimiter: String = ","): Record? = parse(source.split(delimiter))

    fun parse(source: Iterable<String>): Record? {

        val match: RecordType? = _root.match(source)

        if (match != null) {
            val values: Iterator<String>                = source.iterator()
            val fields: ArrayList<Pair<String, String>> = ArrayList()

            match.fields.forEach {
                if (values.hasNext()) {
                    val value: String = values.next()

                    if (it.matches(value)) {
                        if (it.capture) {
                            fields.add(Pair(it.fieldName, it.type.adjust(value)))
                        }
                    } else {
                        return@parse null
                    }
                } else {
                    return@parse null
                }
            }

            return match.record(_dataMapper(mapOf(*fields.toTypedArray())))
        }

        return null
    }

    override fun toString(): String = _root.toString()
}

