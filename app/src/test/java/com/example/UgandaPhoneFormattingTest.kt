package com.example

import com.example.ui.components.formatUgandaPhoneToInternational
import org.junit.Assert.assertEquals
import org.junit.Test

class UgandaPhoneFormattingTest {

    @Test
    fun testLocalUgandaPhoneStartingWithZero() {
        assertEquals("+256777992199", formatUgandaPhoneToInternational("0777992199"))
        assertEquals("+256701445921", formatUgandaPhoneToInternational("0701445921"))
    }

    @Test
    fun testUgandaPhoneWithSpacesAndDashes() {
        assertEquals("+256772458928", formatUgandaPhoneToInternational("+256 772 458 928"))
        assertEquals("+256701334812", formatUgandaPhoneToInternational("0701-334-812"))
    }

    @Test
    fun testUgandaPhoneAlreadyWithCountryCode() {
        assertEquals("+256777992199", formatUgandaPhoneToInternational("256777992199"))
        assertEquals("+256777992199", formatUgandaPhoneToInternational("+256777992199"))
    }

    @Test
    fun testNineDigitUgandaPhone() {
        assertEquals("+256777992199", formatUgandaPhoneToInternational("777992199"))
    }
}
