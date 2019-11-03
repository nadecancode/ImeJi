package me.allen.imeji.util.id;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class IdentifierGenerator {

    private static char[] alphabets = "abcdefghijklmnopqrstuvwxy0123456789".toCharArray();

    public static String getId() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, alphabets, 7);
    }

}
