import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AudioToken {
    public static void main(String[] args) throws Exception{
        System.out.println(getToken("217132304").length());
    }
    public static String getToken(String uid) throws Exception{
        URL url = new URL("https://android.clients.google.com/c2dm/register3");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        String sj = "X-subtype=54740537194&X-X-subscription=54740537194&X-X-subtype=54740537194&X-app_ver=414&X-kid=%7CID%7C1%7C&X-osv=22&X-sig=hFPgsFXRSlTbShE30_-1ip5gDphq_xQiJYKppnD0ZEs4aZ5eRERU4HMEMCBPGAo6a75gxAWa2s3Xvz5_owUhaUSxiXHRTYzzc8PVyJZocrV_mdPDfOdGdNmlCAyZ_cq9oAXwNp-xxPFiwqGfbNc4JyhCMbbLkQ35NOwqBNRUdFpYpDu9dv9Z0XzhMR3-tRZ6akTJRj8Fm2agKvvxZwMt35xvzliUc5Z_dRph04i8qQ78yCpt12AkFJLUMxy47S-l4JOllcaIElZX9KHUXDNRWAcHkvmZbuXcNI3eDeSgquUpXqXwcy1_RQnuASAyZHZWfTwgJUybcNXXQ-4iFT1-Dg&X-cliv=iid-9452000&X-gmsv=11951270&X-pub2=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwg63eexVyrdV66YiH3R4a3De2_d3QwBsulptEenQ6pYzL3C1DP1t2Pt4VGC9ZmBXA1qUAe06i6kwViQzw2YkTtUGdbShsKzVh6aHPP0cpsDaMb8JYwVinuZWgYnrUBCOde-IJ0Y-f0Xer3gjDJQm-UZTW9PBvYCAdBty9BV73mQsoWkkn-yqlO8u9vch1kjfV8iF1GDS9mkiG1tCZcE4ZTUNMzasQ2WSuXXuyWOeGa0hIOfJe6DSIkzzrvrkQVErM5XQwjSJ5uQF0gcgfO_JFDrOwb8qN4xINZ1J1I-pGz0Lvev-ii3I5ERLnWqQThDRskk8oUoDnVho5ikbzo1rAQIDAQAB&X-X-kid=%7CID%7C1%7C&X-appid=fFcI2QYSt_k&X-scope=id" + uid + "&X-subscription=54740537194&X-app_ver_name=43&app=com.perm.kate&sender=54740537194&device=3744286266973960032&cert=ca7036ce4c5abe56b9f4439ea275171ceb0d35a4&app_ver=414&info=83nHWh1ZbN8UsMmGZHNgy8SlZbNx-hU&gcm_ver=11951270";
        byte[] out = sj.getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        connection.setRequestProperty("User-Agent", "Android-GCM/1.5 (vbox86p LMY47D)");
        connection.addRequestProperty("Gcm-cert", "ca7036ce4c5abe56b9f4439ea275171ceb0d35a4");
        connection.addRequestProperty("Gcm-ver", "11951270");
        connection.addRequestProperty("app", "com.perm.kate");
        connection.addRequestProperty("Authorization", "AidLogin 3744286266973960032:6946597583564230085");
        connection.setFixedLengthStreamingMode(length);
        connection.setDoOutput(true);
        connection.connect();
        try(OutputStream os = connection.getOutputStream()) {
            os.write(out);
        }
        int code = connection.getResponseCode();
        System.out.println(code);
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String  body = "", output;
        while ((output = br.readLine()) != null) {
            body += output;
        }
        Pattern pat = Pattern.compile(":");
        Matcher mat = pat.matcher(body);


        return body.replaceAll("token=\\|ID\\|", "").substring(3).replaceAll("\n", "").replaceAll(" ","");
    }
}
