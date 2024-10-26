package org.code_challenger.services;

import java.util.UUID;

public class UuidService {
    public static String GenerateUUID() {
        return UUID.randomUUID().toString();
    }
}
