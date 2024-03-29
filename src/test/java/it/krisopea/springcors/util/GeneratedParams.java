package it.krisopea.springcors.util;

import java.util.UUID;

public class GeneratedParams {

    public static String generateNoticeId() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 11);
    }

    public static String generateIuv() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 11);
    }

}
