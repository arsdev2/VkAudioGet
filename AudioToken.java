import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class AudioToken {
    public static void main(String[] args) throws Exception{
        System.out.println(getToken("217132304").length());
    }
    public static String getToken(String uid) throws Exception{
        URL url = new URL("https://android.clients.google.com/c2dm/register3");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        String sj = "X-subtype=54740537194&X-X-subscription=54740537194&X-X-subtype=54740537194&X-app_ver=431&X-kid=%7CID%7C2%7C&X-osv=22&X-sig=V0K0RL_JPZgbLTx3N6329klqHFx-Ih8Tm_Lv8JYmpS07fFkqGa-WHfqFUgH2kL7sWW9Qb-nyKwlS0HsW4qj_pJYnrErdQ3-6spEQIQ_yTdVg328op3tbPLicLIkIFtF3OJV_Re6AEf87aiLUC6qbg0lYZrQ2R6EigSc8EMrcDE2VDLk-OLEcDY70rJZlDBuHT0nHATvKJuY0fcVZiBJQTE5OZHRw07dpzgJ4Is8KSasGK0mE4kdBd-2APU2aFJWe-mtup-fM85mS5B9uJSBr0NwmW73EDarD_qBUp5qzF_J6MUQJ5E78tWt3Ntuajmj4nPk4iL8x5QrqJCPm1InBbw&X-cliv=iid-9452000&X-gmsv=11975236&X-pub2=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxzYzGXl3cum_2eQzOSPPMbL4wREqTJ4kbcrrPuzK49iU5eylb84CJrhS_8f7yqPHHAaIRCakJlIeOk_xV4HQs9qgenIGThphG87nKl3cefra-LfXIVs0dPWx_k7lJnM5_JfWxZisMU-FO448ZohRhffQN_ibnH58zLuDapVqOrW6rii30ABKJitU6H9FmXy7eSv3LmFsatsu1uVnSxAcRhhvW5G8peDprQ0Fbl1-XN0F9LHZMMnXi2drCKvZOeelsnfLmLvGeURTIApOGTF2ml9-5iBYd3NVa8heH6WpbtacdMcubjeXxbMgdheMvakgJWlQg2Y_LByvhTpV_iK_0QIDAQAB&X-X-kid=%7CID%7C2%7C&X-appid=egc2JXpLa7M&X-scope=id217132304&X-subscription=54740537194&X-app_ver_name=48.1&app=com.perm.kate&sender=54740537194&device=4018038774292298290&cert=ca7036ce4c5abe56b9f4439ea275171ceb0d35a4&app_ver=431&info=w815UAJ94VYfsMmGZHNgy8Tl9t_1DhY&gcm_ver=11975236";
        byte[] out = sj.getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        connection.setRequestProperty("User-Agent", "Android-GCM/1.5 (m0 LMY48Y)");
        connection.addRequestProperty("Gcm-cert", "ca7036ce4c5abe56b9f4439ea275171ceb0d35a4");
        connection.addRequestProperty("app", "com.perm.kate");
        connection.addRequestProperty("Gcm-ver", "11975236");
        connection.addRequestProperty("Authorization", "AidLogin 4018038774292298290:8687452178090903598");
        connection.setFixedLengthStreamingMode(length);
        connection.setDoOutput(true);
        connection.connect();
        try(OutputStream os = connection.getOutputStream()) {
            os.write(out);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String  body = "", output;
        while ((output = br.readLine()) != null) {
            body = body.concat(output);
        }
        System.out.println(body);
        return body.replaceAll("token=\\|ID\\|", "").substring(3).replaceAll("\n", "").replaceAll(" ","");
    }
}
