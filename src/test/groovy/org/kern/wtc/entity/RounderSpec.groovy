package org.kern.wtc.entity

import spock.lang.Specification
import spock.lang.Unroll
import spock.util.mop.ConfineMetaClassChanges

import java.time.LocalTime
import java.time.format.DateTimeFormatter

@ConfineMetaClassChanges(String)
class RounderSpec extends Specification {

    def setupSpec(){
        String.metaClass.with {
            toTime = { DateTimeFormatter dtf ->
                LocalTime.parse(delegate, dtf)
            }

            toTime = {
                ->
                delegate.toTime(DateTimeFormatter.ofPattern("H:mm"))
            }
        }
    }

    @Unroll
    def "#roundeeを#roundUnit分単位で丸めると#result"() {
        setup:
        def rounded = Rounder.of(roundee, roundUnit).round()

        expect:
        result == rounded

        where:
        roundee          | roundUnit || result
        "8:00".toTime()  | 5         || "8:00".toTime()
        "8:02".toTime()  | 5         || "8:00".toTime()
        "8:03".toTime()  | 5         || "8:05".toTime()
        "8:05".toTime()  | 5         || "8:05".toTime()
        "23:59".toTime() | 5         || "23:59".toTime()
        "8:00".toTime()  | 10        || "8:00".toTime()
        "8:05".toTime()  | 10        || "8:00".toTime()
        "8:06".toTime()  | 10        || "8:10".toTime()
        "8:10".toTime()  | 10        || "8:10".toTime()
        "23:59".toTime() | 10        || "23:59".toTime()
        "8:00".toTime()  | 15        || "8:00".toTime()
        "8:07".toTime()  | 15        || "8:00".toTime()
        "8:08".toTime()  | 15        || "8:15".toTime()
        "8:15".toTime()  | 15        || "8:15".toTime()
        "23:59".toTime() | 15        || "23:59".toTime()
    }
}