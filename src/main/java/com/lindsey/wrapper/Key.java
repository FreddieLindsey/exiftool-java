package com.lindsey.wrapper;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Key {

    // Exif

    CREATEDATE("CreateDate", String.class, "called DateTimeDigitized by the EXIF spec"),
    DATETIMEORIGINAL("DateTimeOriginal", String.class, "date/time when original image was taken"),
    INTEROPINDEX("InteropIndex", String.class, "'R03' = R03 - DCF option file (Adobe RGB), 'R98' = R98 - DCF basic file (sRGB), 'THM' = THM - DCF thumbnail file"),
    MODIFYDATE("ModifyDate", String.class, "called DateTime by the EXIF spec"),
    OFFSETTIME("OffsetTime"	, String.class, "time zone for ModifyDate"),
    OFFSETTIMEORIGINAL("OffsetTimeOriginal", String.class, "time zone for DateTimeOriginal"),

    ;

    private static final Map<String, Key> ENTRY_MAP = Arrays.stream(Key.values()).collect(Collectors.toMap(Key::getName, k -> k));

    private final String notes;
    private final Class<?> clazz;
    private final String name;

    Key(String name, Class<?> clazz, String notes) {
        this.name = name;
        this.clazz = clazz;
        this.notes = notes;
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(Key key, String value) {
        Class<?> type = key.clazz;
        if (Boolean.class.isAssignableFrom(type)) {
            return (T) Boolean.valueOf(value);
        } else if (Integer.class.isAssignableFrom(type)) {
            return (T) Integer.valueOf(value);
        } else if (Long.class.isAssignableFrom(type)) {
            return (T) Long.valueOf(value);
        } else if (Double.class.isAssignableFrom(type)) {
            return (T) Double.valueOf(value);
        } else if (String.class.isAssignableFrom(type)) {
            return (T) value;
        }

        throw new UnsupportedOperationException(String.format("Parsing not implemented for ExifTool name %s with class %s.", key.name, type));
    }

    public static Optional<Key> findKeyWithName(String name) {
        return ENTRY_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().equals(name))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public static String getName(Key key) {
        return key.name;
    }

    public static String getNotes(Key key) {
        return key.notes;
    }

}
