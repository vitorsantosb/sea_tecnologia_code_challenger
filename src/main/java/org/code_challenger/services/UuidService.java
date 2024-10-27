package org.code_challenger.services;

import java.util.UUID;

public class UuidService {
    public static String GenerateUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean IsValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
