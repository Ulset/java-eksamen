package no.kristiania;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringDecodeTest {
    @Test
    void shouldDecodeNorwegianLetters() throws UnsupportedEncodingException {
        String testString = "%C3%A6";
        assertEquals("æ", URLDecoder.decode(testString, "UTF-8"));
    }
    @Test
    void ShouldFixØ() throws UnsupportedEncodingException {
        String testString = "%C3%B8";
        assertEquals("ø", URLDecoder.decode(testString, "UTF-8"));
    }

    @Test
    void shouldFixÅ() throws UnsupportedEncodingException {
        String testString = "%C3%A5";
        assertEquals("å", URLDecoder.decode(testString, "UTF-8"));
    }

    @Test
    void shouldFixPlusSignToWhiteSpace() throws UnsupportedEncodingException {
        String testString = "+";
        assertEquals(" ", URLDecoder.decode(testString, "UTF-8"));
    }

    @Test
    void shouldFixPlusSignWithText() throws UnsupportedEncodingException {
        String testString = "Thomas+Sourianos";
        assertEquals("Thomas Sourianos", URLDecoder.decode(testString, "UTF-8"));
    }

    @Test
    void shouldReplaceØÆÅEvenIfTheyAreAfterEachOther() throws UnsupportedEncodingException {
        String testString = "%C3%A6%C3%B8%C3%A5";
        assertEquals("æøå", URLDecoder.decode(testString, "UTF-8"));
    }
}
