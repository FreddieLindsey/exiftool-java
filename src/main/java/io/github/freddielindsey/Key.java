package io.github.freddielindsey;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Key {

    // Exif

    APERTURE("ApertureValue", Double.class),
    ARTIST("Artist", String.class),
    AUTHOR("XPAuthor", String.class),
    AVG_BITRATE("AvgBitrate", String.class),
    CAPTION_ABSTRACT("Caption-Abstract", String.class),
    COLOR_SPACE("ColorSpace", Integer.class),
    COMMENT("XPComment", String.class),
    CONTRAST("Contrast", Integer.class),
    COPYRIGHT("Copyright", String.class),
    COPYRIGHT_NOTICE("CopyrightNotice", String.class),
    CREATEDATE("CreateDate", String.class, "called DateTimeDigitized by the EXIF spec"),
    CREATION_DATE("CreationDate", String.class),
    CREATOR("Creator", String.class),
    DATETIMEORIGINAL("DateTimeOriginal", String.class, "date/time when original image was taken"),
    DIGITAL_ZOOM_RATIO("DigitalZoomRatio", Double.class),
    EXIF_VERSION("ExifVersion", String.class),
    EXPOSURE_COMPENSATION("ExposureCompensation", Double.class),
    EXPOSURE_PROGRAM("ExposureProgram", Integer.class),
    EXPOSURE_TIME("ExposureTime", Double.class),
    FILE_TYPE("FileType", String.class),
    FLASH("Flash", Integer.class),
    FOCAL_LENGTH("FocalLength", Double.class),
    FOCAL_LENGTH_35MM("FocalLengthIn35mmFormat", Integer.class),
    GPS_ALTITUDE("GPSAltitude", Double.class),
    GPS_ALTITUDE_REF("GPSAltitudeRef", Integer.class),
    GPS_BEARING("GPSDestBearing", Double.class),
    GPS_BEARING_REF("GPSDestBearingRef", String.class),
    GPS_LATITUDE("GPSLatitude", Double.class),
    GPS_LATITUDE_REF("GPSLatitudeRef", String.class),
    GPS_LONGITUDE("GPSLongitude", Double.class),
    GPS_LONGITUDE_REF("GPSLongitudeRef", String.class),
    GPS_PROCESS_METHOD("GPSProcessingMethod", String.class),
    GPS_SPEED("GPSSpeed", Double.class),
    GPS_SPEED_REF("GPSSpeedRef", String.class),
    GPS_TIMESTAMP("GPSTimeStamp", String.class),
    IMAGE_HEIGHT("ImageHeight", Integer.class),
    IMAGE_WIDTH("ImageWidth", Integer.class),
    INTEROPINDEX("InteropIndex", String.class, "'R03' = R03 - DCF option file (Adobe RGB), 'R98' = R98 - DCF basic file (sRGB), 'THM' = THM - DCF thumbnail file"),
    IPTC_KEYWORDS("Keywords", String.class),
    ISO("ISO", Integer.class),
    KEYWORDS("XPKeywords", String.class),
    LENS_ID("LensID", String.class),
    LENS_MAKE("LensMake", String.class),
    LENS_MODEL("LensModel", String.class),
    MAKE("Make", String.class),
    METERING_MODE("MeteringMode", Integer.class),
    MIME_TYPE("MIMEType", String.class),
    MODEL("Model", String.class),
    MODIFYDATE("ModifyDate", String.class, "called DateTime by the EXIF spec"),
    OBJECT_NAME("ObjectName", String.class),
    OFFSETTIME("OffsetTime", String.class, "time zone for ModifyDate"),
    OFFSETTIMEORIGINAL("OffsetTimeOriginal", String.class, "time zone for DateTimeOriginal"),
    ORIENTATION("Orientation", Integer.class),
    OWNER_NAME("OwnerName", String.class),
    RATING("Rating", Integer.class),
    RATING_PERCENT("RatingPercent", Integer.class),
    ROTATION("Rotation", Integer.class),
    SATURATION("Saturation", Integer.class),
    SENSING_METHOD("SensingMethod", Integer.class),
    SHARPNESS("Sharpness", Integer.class),
    SHUTTER_SPEED("ShutterSpeedValue", Double.class),
    SOFTWARE("Software", String.class),
    SUBJECT("XPSubject", String.class),
    SUB_SEC_TIME_ORIGINAL("SubSecTimeOriginal", Integer.class),
    TITLE("XPTitle", String.class),
    WHITE_BALANCE("WhiteBalance", Integer.class),
    X_RESOLUTION("XResolution", Double.class),
    Y_RESOLUTION("YResolution", Double.class);

    private static final Map<String, Key> ENTRY_MAP = Arrays.stream(Key.values()).collect(Collectors.toMap(Key::getName, k -> k));

    private final String notes;
    private final Class<?> clazz;
    private final String name;

    Key(String name, Class<?> clazz) {
        this(name, clazz, "");
    }

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
