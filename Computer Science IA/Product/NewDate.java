/*
 * Date class
 * by mdv 2016, 2019, 2020
 *
 * TESTING STRONGLY RECOMMENDED-NO WARRANTIES!!!
 *
 * Ideas taken from
 * http://www.java2s.com/Tutorial/Java/0040__Data-Type/Getcurrentdateyearandmonth.htm
 * http://www.sunshine2k.de/articles/coding/datediffindays/calcdiffofdatesindates.html
 * the other way would be...
 * http://javabeginnerstutorial.com/code-base/how-to-calculate-between-two-dates-in-java/
 * using Java's built-in classes
 */

/*
 * Date class
 * by mdv 2016, 2019, 2020
 *
 * TESTING STRONGLY RECOMMENDED-NO WARRANTIES!!!
 *
 * Ideas taken from
 * http://www.java2s.com/Tutorial/Java/0040__Data-Type/Getcurrentdateyearandmonth.htm
 * http://www.sunshine2k.de/articles/coding/datediffindays/calcdiffofdatesindates.html
 * the other way would be...
 * http://javabeginnerstutorial.com/code-base/how-to-calculate-between-two-dates-in-java/
 * using Java's built-in classes
 */

import java.io.Serializable;
import java.util.Scanner;

public class NewDate implements Serializable
{
    private int day;
    private int month;
    private  int year;
    public NewDate(String dateString) throws Exception
    {
        int d = 0, m = 0, y = 0;
        dateString = dateString.replace('/', ' ');
        Scanner scanner = new Scanner(dateString);
        try
        {
            d = scanner.nextInt();
            m = scanner.nextInt();
            y = scanner.nextInt();
            scanner.close();
        }
        catch (Exception e)
        {
            throw new Exception("Invalid date format");
        }
        if (isValidDate(d, m, y))
        {
            day = d;
            month = m;
            year = y;
        } else
        {
            throw new Exception("Invalid date");
        }
    }

    public int getDay()
    {
        return day;
    }

    public int getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }

    public String toString()
    {
        String output = day + "/" + month + "/" + year;
        return output;
    }

    public static boolean isValidMonth(int month)
    {
        return (month >= 1 && month <= 12);
    }

    public static boolean isValidYear(int year)
    {
        return (year >= 1582);
    }
    public static boolean isLeapYear(int year)
    {
        boolean divisibleBy4 = (year % 4 == 0);
        boolean notDivBy100 = (year % 100 != 0);
        boolean divBy100and400 = (year % 100 == 0 && year % 400 == 0);
        return divisibleBy4 && (notDivBy100 || divBy100and400);
    }

    public static int daysinMonth(int month, int year)
    {
        if (month == 1)
        {
            return 31;
        } else if (month == 2 && isLeapYear(year))
        {
            return 29;
        } else if (month == 2 && !isLeapYear(year))
        {
            return 28;
        } else if (month == 3)
        {
            return 31;
        } else if (month == 4)
        {
            return 30;
        } else if (month == 5)
        {
            return 31;
        } else if (month == 6)
        {
            return 30;
        } else if (month == 7)
        {
            return 31;
        } else if (month == 8)
        {
            return 31;
        } else if (month == 9)
        {
            return 30;
        } else if (month == 10)
        {
            return 31;
        } else if (month == 11)
        {
            return 30;
        } else if (month == 12)
        {
            return 31;
        } else
        {
            return -1; // invalid DOTM
        }
    }

    public static boolean isValidDay(int day, int month, int year)
    {
        return (day >= 1 && day <= daysinMonth(month, year));
    }

    public static boolean isValidDate(int day, int month, int year)
    {
        return (isValidYear(year) && isValidMonth(month) && isValidDay(day, month, year));
    }


    public boolean equals(NewDate date2)
    {
        boolean equal = false;
        if (this.getDay() == date2.getDay()
                && this.getMonth() == date2.getMonth()
                && this.getYear() == date2.getYear())
        {
            equal = true;
        }
        return equal;
    }

    public int compareTo(NewDate date2)
    {
        if (this.getYear() != date2.getYear())
        {
            return this.getYear() - date2.getYear();
        } else if (this.getMonth() != date2.getMonth())
        {
            return this.getMonth() - date2.getMonth();
        } else
        {
            return this.getDay() - date2.getDay();
        }
    }
}
