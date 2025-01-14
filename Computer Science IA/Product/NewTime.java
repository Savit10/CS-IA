import java.io.Serializable;
import java.util.Scanner;

public class NewTime implements Serializable
{
    private int hours;
    private int minutes;
    public NewTime(String time) throws Exception{
        time = time.replace(':', ' ');
        Scanner sc = new Scanner(time);
        try {
            this.hours = Integer.parseInt(sc.next());
            this.minutes = Integer.parseInt(sc.next());
        } catch (Exception e) {
            throw new Exception("Invalid time format");
        }
    }

    public int getHours()
    {   return this.hours;
    }

    public void setHours(int h)
    {   if ( h < 0 )
    {   h = h * -1;
    }
        if(h > 23)
        {   h = h % 24;
        }
        this.hours = h;
    }

    public int getMinutes()
    {   return this.minutes;
    }

    public void setMinutes(int m)
    {   int h = this.hours;
        if ( m < 0 )
        {   m = m * -1;
        }
        if(m > 59)
        {   h = h + m/60;
            m = m % 60;
        }
        this.minutes = m;
        this.setHours(h);
    }

    public int toMinutes()
    {   return this.getHours()*60 + this.getMinutes();
    }

    public String toString()
    {   String output ="";
        if(this.getHours() < 10)
        {   output = output + "0";
        }
        output = output + this.getHours() + ":";
        if(this.getMinutes() < 10)
        {   output = output + "0";
        }
        output = output + this.getMinutes();
        return output;
    }

    public boolean equals(NewTime another)
    {   boolean result = false;
        if(this.toMinutes() == another.toMinutes())
        {   result = true;
        }
        return result;
    }

    public int compareTo(NewTime another)
    {   return this.toMinutes() - another.toMinutes();
    }


}
