package system.gc.utils;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

public final class TextUtils {

    public static final ZoneId zoneManaus = ZoneId.of("America/Manaus");
    public static Gson GSON = new Gson();

    public static final String ORGANIZATION = "GCSYSTEM";

    public static String generateTXID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static OffsetDateTime beginningOfTheDay()
    {
        LocalDate localDate = LocalDate.now(zoneManaus);
        return OffsetDateTime.of(localDate.getYear(),
                localDate.getMonthValue(),
                localDate.getDayOfMonth(), 0,0,0, 0, ZoneOffset.UTC);
    }

    public static OffsetDateTime currentDateAndTime()
    {
        return OffsetDateTime.now(zoneManaus)
                .withOffsetSameInstant(ZoneOffset.UTC);
    }

}
