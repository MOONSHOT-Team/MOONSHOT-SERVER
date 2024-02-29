package org.moonshot.validator;

public class IndexValidator {

    public static boolean isSameIndex(final int prevIdx, final int idx) {
        return prevIdx == idx;
    }

    public static boolean isIndexIncreased(final int prevIdx, final int idx) {
        return prevIdx < idx;
    }

}
