package demo.services;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;


/**
 * Created by Stephanie Mertz on 5/30/2015.
 */

public class cardService {

    public static void testing(String expDate, String card, double amount) {


        String regexDate = "\\d\\d\\d\\d";
        String chargeResponse;
        //TODO: check that the date is valid (month is 1-12 and date is after today's date. Verify card number is valid and amount charged is a boolean in dollar format

        if(expDate.matches(regexDate)) {

                chargeResponse = chargeCard(card, expDate, amount);
        }
        throw new IllegalArgumentException("Expiration date not valid");
    }

    public static String chargeCard(String cardNum, String expDate, double charge){
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("https://gateway-sb.clearent.net/rest/v2/transactions");

            StringEntity input = new StringEntity("{\"api-key\":\"94a4d359977c47168ba4a9395496fa94\",\"card\":cardNum,\"exp-date\":expDate, \"amount\":charge, \"type\":\"sale\"}");
            input.setContentType("application/json");
            postRequest.setEntity(input);

            org.apache.http.HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {

                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode() + "error response " + response.getEntity().toString());
            }
            BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            httpClient.getConnectionManager().shutdown();
            return "Success";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}