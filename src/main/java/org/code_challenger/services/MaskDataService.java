package org.code_challenger.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class MaskDataService {

    private MaskType maskType;
    public static String ApplyStringMask(String input, MaskType maskType) {
        switch (maskType) {
            case CELLPHONE:
                return formatCellPhone(input);
            case COMMERCIAL:
                return formatCommercial(input);
            case RESIDENTIAL:
                return formatResidential(input);
            case CPF:
                return formatCpf(input);
            default:
                throw new IllegalArgumentException("Invalid mask type.");
        }
    }

    private static String formatCellPhone(String input) {
        if(input == null) {
            throw new IllegalArgumentException("CPF cannot be null");
        }
        return input.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }

    private static String formatCommercial(String input) {
        return input.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
    }

    private static String formatResidential(String input) {
        return input.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
    }

    private static String formatCpf(String input) {
        if(input == null) {
            return "";
        }
        return input.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
