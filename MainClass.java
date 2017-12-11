import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
public class MainClass {
    public static String USER_AGENT = "KateMobileAndroid/43-414 (Android 5.1; SDK 22; x86; unknown ANDROID_LOLIPOPKA; en)", MID = "";
    public static void main(String[] args) throws Exception{
        String access_token = login(), audioJson= "";
        do{
            access_token = updateToken(access_token);
            System.out.println("New Access Token is: " + access_token);
            System.out.println("Retry.............");
            audioJson = get("https://api.vk.com/method/audio.get?access_token=" + access_token + "&owner_id=" + MID + "&v=5.68");
        }while (audioJson.contains("confirmation"));
        System.out.println("Json from Audio is: " + audioJson);
    }
    public static String updateToken(String oldToken) throws Exception{
        String audio_token = AudioToken.getToken(MainClass.MID);
        return new JSONObject(get("https://api.vk.com/method/auth.refreshToken?access_token=" + oldToken  + "&receipt=" + audio_token + "&v=5.68")).getJSONObject("response").getString("token");
    }
    public static String get(String uri)throws Exception{
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "KateMobileAndroid/43-414 (Android 5.1; SDK 22; x86; unknown ANDROID_LOLIPOPKA; en)");
        //connection.setRequestProperty("Accept-Encoding", "gzip");
        //connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.connect();
        CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
        int code = connection.getResponseCode();
        BufferedReader br = null;
        if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        String out = "", output;
        while ((output = br.readLine()) != null) {
            out += output;
        }
        return out;
    }
    public static String login() throws Exception{
        System.out.println("Enter login: ");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String login = r.readLine();
        System.out.println("Enter Password: ");
        String pass = r.readLine();
        String loginUrl = "https://oauth.vk.com/token?grant_type=password&client_id=2685278&client_secret=lxhD8OD7dMsqtXIm5IUY&username=" + login + "&password=" + pass + "&v=5.68&scope=notify%2Cfriends%2Cphotos%2Caudio%2Cvideo%2Cdocs%2Cstatus%2Cnotes%2Cpages%2Cwall%2Cgroups%2Cmessages%2Coffline%2Cnotifications";
        String resp = get(loginUrl);
        System.out.println(resp);
        String access_token = "";
        if(resp.contains("captcha")){
            System.out.println("Captcha json is: " + resp);
            System.out.println("Enter key");
            String key = r.readLine();
            String id = resp.split("sid=")[1].replaceAll("}", "").replaceAll("\"", "");
            System.out.println("Captcha id is: " + id);
            String body = get(loginUrl + "&captcha_sid=" + id + "&captcha_key=" + key);
            System.out.println("After login token json is: " + body);
            JSONObject object = new JSONObject(body);
            access_token = object.getString("access_token");
            MainClass.MID = String.valueOf(object.getInt("user_id"));
        }else{
            JSONObject object = new JSONObject(resp);
            access_token = object.getString("access_token");
            MainClass.MID = String.valueOf(object.getInt("user_id"));
        }
        r.close();
        return access_token;
    }

}
