package ir.co.sadad.investment.date;

import ir.co.sadad.hambaam.persiandatetime.PersianUTC;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.format.DateTimeFormatter;

@ActiveProfiles(profiles = {"qa"})
public class DateTest {


    @Test
    void shouldConvertWithoutTime() {


        String s = PersianUTC.toUTCDateOfBeginningOfDay("1402/12/15", DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss")).toString();
//        assertEquals("1401/09/12", DateTimeFormatHelper.getCurrentJalaliDate());
        //"2023-01-20T16:15:07.110+03:30"
        System.out.println(s);
    }

    @Test
    void shouldConvertWithTime() {
        String s = PersianUTC.toUTCDateTime("1403/01/26 10:04", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")).toString();
        System.out.println(s);
    }

    @Test
    void shouldCheckTokenExpireDate()
    {
      boolean result=  1727729369000L < System.currentTimeMillis() ;
        System.out.println(result);
        System.out.println(System.currentTimeMillis());
    }

}
