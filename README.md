# TenorGIFJava

This is a super simple Tenor GIF wrapper for Java. 

It can do 3 main things:
1. return the first gif urls of a result from your query
2. return a random gif urls (and you can specify how many max results you want it to pick from)
3. return list of gif urls based on query and specified max.

Please set the API key before using, otherwise it will not work

Example code: 


```
import java.util.ArrayList;
import com.charizard.TenorGIF.Tenor;
public class Example {
    public static void main(String[] args) {
        Tenor.setAPIKey("Your Key Here");
        System.out.println(Tenor.returnRandomGIF("Charizard", 10));
        System.out.println(Tenor.returnGIF("Charizard"));
        ArrayList<String> list = Tenor.returnGIFs("Charizard", 10);
        String links = "";
        for (int i = 0; i < 5; i++) {
            links += list.get(i) + "\n";
        }
        System.out.println(links);
    }
}
```
