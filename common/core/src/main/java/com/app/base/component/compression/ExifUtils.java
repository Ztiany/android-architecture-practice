package com.app.base.component.compression;

import androidx.exifinterface.media.ExifInterface;

import java.io.IOException;

import timber.log.Timber;

/**
 * @author Ztiany
 */
class ExifUtils {

    static void copyExif(String originalPath, String newPath) {
        try {

            String[] attributes = new String[]
                    {
                            ExifInterface.TAG_DATETIME,
                            ExifInterface.TAG_DATETIME_DIGITIZED,
                            ExifInterface.TAG_EXPOSURE_TIME,
                            ExifInterface.TAG_FLASH,
                            ExifInterface.TAG_FOCAL_LENGTH,
                            ExifInterface.TAG_GPS_ALTITUDE,
                            ExifInterface.TAG_GPS_ALTITUDE_REF,
                            ExifInterface.TAG_GPS_DATESTAMP,
                            ExifInterface.TAG_GPS_LATITUDE,
                            ExifInterface.TAG_GPS_LATITUDE_REF,
                            ExifInterface.TAG_GPS_LONGITUDE,
                            ExifInterface.TAG_GPS_LONGITUDE_REF,
                            ExifInterface.TAG_GPS_PROCESSING_METHOD,
                            ExifInterface.TAG_GPS_TIMESTAMP,
                            ExifInterface.TAG_MAKE,
                            ExifInterface.TAG_MODEL,
                            ExifInterface.TAG_SUBSEC_TIME,
                            ExifInterface.TAG_WHITE_BALANCE,
                            ExifInterface.TAG_EXIF_VERSION,
                            ExifInterface.TAG_X_RESOLUTION,
                            ExifInterface.TAG_Y_RESOLUTION
                    };

            ExifInterface oldExif = new ExifInterface(originalPath);
            ExifInterface newExif = new ExifInterface(newPath);

            for (String attribute : attributes) {
                String value = oldExif.getAttribute(attribute);
                Timber.d(attribute + " = " + value);
                if (value != null) {
                    newExif.setAttribute(attribute, value);
                }
            }

            newExif.saveAttributes();
        } catch (IOException e) {
            Timber.e(e, "copyExif");
        }
    }

}
