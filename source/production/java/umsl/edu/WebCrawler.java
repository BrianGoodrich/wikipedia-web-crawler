package umsl.edu;

import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.net.URL;


public class WebCrawler {

    public static void main (String[] args){

        String startingUrl;
        Scanner input = new Scanner(System.in);

        System.out.println("Enter a Wikipedia URL to start crawling.");

        startingUrl = input.nextLine();

        System.setProperty("http.agent", "Chrome");

        crawler(startingUrl);

    }

    public static void crawler(String startingUrl){

        ArrayList<String> pendingUrl = new ArrayList<>();
        ArrayList<String> traversedUrl = new ArrayList<>();

        pendingUrl.add(startingUrl);

        while(!pendingUrl.isEmpty() && traversedUrl.size() <= 1000){

            String urlString = pendingUrl.remove(0);

            if(!traversedUrl.contains(urlString)){
                traversedUrl.add(urlString);
            }

            try {
                for (String x : getSubURL(urlString)) {
                    Thread.sleep(50);
                    if (!traversedUrl.contains(x)) {
                        pendingUrl.add(x);
                    }

                }
            }
            catch(InterruptedException ex){
                System.out.println("Error:" + ex.getMessage());
            }
        }

        wordCounter(traversedUrl);
    }

    public static ArrayList<String> getSubURL (String urlString){

        ArrayList<String> list = new ArrayList<>();

        try{

            URL url = new URL(urlString);
            System.setProperty("http.agent", "Chrome");
            Scanner input = new Scanner(url.openStream());
            int current = 0;


            while (input.hasNext()){

                String line = input.nextLine();

                current = line.indexOf("http:", current);

                while(current > 0){
                    int endIndex = line.indexOf("\"", current);
                    if (endIndex > 0){
                        list.add(line.substring(current, endIndex));
                        current = line.indexOf("http:", endIndex);
                    }
                    else
                        current = -1;
                }
            }



        }
        catch(Exception ex){

            System.out.println("Error:" + ex.getMessage());
            return list;
        }

        return list;
    }


    public static void wordCounter (ArrayList<String> urlString){

        String tempUrl;
        String bodyText;
        ArrayList<String> countedWords = new ArrayList<>();

        while (!urlString.isEmpty()){

            tempUrl = urlString.remove(0);

            try {
                URL url = new URL(tempUrl);

                System.setProperty("http.agent", "Chrome");

                Document htmlDoc = Jsoup.parse(url, 5000);

                System.out.println(htmlDoc.title());

                bodyText = htmlDoc.text();

                bodyText = bodyText.replaceAll("[0-9]", "").toLowerCase();

                String [] textArray = bodyText.split("\\W+");

                for (int x = 0; x < textArray.length; x++){

                    int counter = 0;

                    if (!countedWords.contains(textArray[x]))
                        countedWords.add(textArray[x]);
                    else
                        continue;

                    for (int y = 0; y < textArray.length; y++){

                        if (Objects.equals(textArray[x], textArray[y]))
                            counter++;
                    }
                    System.out.println(textArray[x] + " : " + counter);
                }
            }
            catch (Exception ex){
            System.out.println("Error:" + ex.getMessage());
            }
        }
    }
}