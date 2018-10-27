import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;

public class MainClass {
    private static String MID = "";
    private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws Exception{
        try {
            String access_token = login(), audioJson;
            do {
                access_token = updateToken(access_token);
                System.out.println("New Access Token is: " + access_token);
                audioJson = get("https://api.vk.com/method/audio.get?access_token=" + access_token + "&owner_id=" + MID + "&v=5.68");
            } while (audioJson.contains("confirmation"));
            processAudioJson(audioJson);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            r.close();
        }

    }

    private static void processAudioJson(String audioJson) throws Exception{
        JSONObject jsonObject = new JSONObject(audioJson);
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray items = response.getJSONArray("items");
        int index = 1;
        for(Object itemObject : items){
            JSONObject item = (JSONObject) itemObject;
            String title = item.getString("title");
            String artist = item.getString("artist");
            int duration = item.getInt("duration");
            int minutes = duration /60, seconds = Integer.parseInt(new DecimalFormat("00").format(duration % 60));
            String sec;
            if(seconds < 10){
                sec = "0" + String.valueOf(seconds);
            }else{
                sec = String.valueOf(seconds);
            }
            String url = item.getString("url");
            if(!url.equals("")) {
                print(index + ": " + minutes + ":" + sec + " Url - " + url);
                index++;
            }
        }
        print("Select audio number to download. \n To download all - type \"all\". \n To download specific audio - type \"spec\" Example - 1,2,3,4... \n To download from some audio number to another - type \"range\"");
        String input = r.readLine();
        switch (input){
            case "all":
                print("Ok, i will download all.");
                print("To download to specific folder type it's absolute path.\nIf you want to download in this folder type folder name");
                String folderPath = r.readLine();
                File musicFolder;
                try{
                    musicFolder = new File(folderPath);
                    if(musicFolder.mkdir()) {
                        for (Object musicElementObject : items) {
                            JSONObject musicElement = (JSONObject) musicElementObject;
                            if (!musicElement.getString("url").equals("")) {
                                String title = musicElement.getString("title"),
                                        artist = musicElement.getString("artist"),
                                        url = musicElement.getString("url");
                                System.out.println("Downloading - " + title + " - " + artist);
                                File musicFile = new File(musicFolder.getAbsolutePath() + "/" + title + " - " + artist + ".mp3");
                                if (!musicFile.exists()) {
                                    if(!musicFile.createNewFile()){
                                        return;
                                    }
                                }
                                writeToFile(url, musicFile);
                            }
                        }
                    }
                }catch (Exception e){
                    print("Failed to find folder!\nExiting program!");
                    e.printStackTrace();
                    System.exit(1);
                }

                break;
            case "spec":

                break;
            case "range":

                break;
            default:
                print("Something is wrong. \n Bye!");
                break;
        }
    }

    private static void print(String print){
        System.out.println(print);
    }

    private static String updateToken(String oldToken) throws Exception{
        String audio_token = AudioToken.getToken(MainClass.MID);
        return new JSONObject(get("https://api.vk.com/method/auth.refreshToken?access_token=" + oldToken  + "&receipt=" + audio_token + "&v=5.68")).getJSONObject("response").getString("token");
    }

    private static String login() throws Exception{
        System.out.println("Enter login: ");
        String login = r.readLine();
        System.out.println("Enter Password: ");
        String pass = r.readLine();
        String loginUrl = "https://oauth.vk.com/token?grant_type=password&client_id=2685278&client_secret=lxhD8OD7dMsqtXIm5IUY&username=" + URLEncoder.encode(login, "UTF-8") + "&password=" + URLEncoder.encode(pass, "UTF-8") + "&v=5.68&scope=notify%2Cfriends%2Cphotos%2Caudio%2Cvideo%2Cdocs%2Cstatus%2Cnotes%2Cpages%2Cwall%2Cgroups%2Cmessages%2Coffline%2Cnotifications";
        String resp = get(loginUrl);
        String access_token;
        if(resp.contains("captcha")){
            String body = verifyCaptcha(resp, loginUrl);
            JSONObject object = new JSONObject(body);
            access_token = object.getString("access_token");
            MainClass.MID = String.valueOf(object.getInt("user_id"));
        }else{
            System.out.println(resp);
            JSONObject object = new JSONObject(resp);
            access_token = object.getString("access_token");
            MainClass.MID = String.valueOf(object.getInt("user_id"));
        }
        System.out.println("After login token is: " + access_token);
        return access_token;
    }

    private static String verifyCaptcha(String resp, String loginUrl) throws Exception{
        String key , id, toReturn;
        do {
            System.out.println("Captcha url: " + new JSONObject(resp).getString("captcha_img"));
            System.out.println("Enter key");
            key = r.readLine();
            id = new JSONObject(resp).getString("captcha_sid");
            toReturn = get(loginUrl + "&captcha_sid=" + id + "&captcha_key=" + key);
        }while(toReturn.contains("captcha"));
        return toReturn;
    }
    private static String get(String url){
        URL obj;
        HttpURLConnection con = null;
        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "KateMobileAndroid/50.2 lite-440 (Android 7.0; SDK 24; arm64-v8a; samsung SAMSUNG-SM-G935A; ru)");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }catch (Exception e){
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            }catch (Exception ignored){
                return "";
            }
        }

    }
    private static void writeToFile(String httpUrl, File file) throws Exception{
        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream stream = connection.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = stream.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
        }catch (Exception ignored){

        }
        baos.flush();
        byte[] result = baos.toByteArray();
        for (byte el : result){
            outputStream.write(el);
        }
        outputStream.flush();
        outputStream.close();
        stream.close();
        baos.close();
    }
}
