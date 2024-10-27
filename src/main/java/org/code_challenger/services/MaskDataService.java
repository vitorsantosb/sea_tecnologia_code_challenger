package org.code_challenger.services;

public class MaskDataService {
    public String ApplyStringMask(String input, MaskType maskType) {
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

    private String formatCellPhone(String input) {
        return input.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }

    private String formatCommercial(String input) {
        return input.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
    }

    private String formatResidential(String input) {
        return input.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
    }

    private String formatCpf(String input) {
        return input.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
