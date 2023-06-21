package application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

public class API {
    private static String token = null;
    private static int id = 0;
    //如果get/post 回傳的是Object，會存到這裡
    private static JSONObject result_object = null;
    //如果get 回傳的是Array，會存到這裡
    private static JSONArray result_array = null;

    /**
     * 取得 API 回傳的 JSONObject 型態的資料
     * @return
     */
    //這兩個public method重要，你會用來取得data
    public static JSONObject getResultObject() {
        return result_object;
    }

    /**
     * 取得 API 回傳的 JSONArray 型態的資料
     * @return
     */
    public static JSONArray getResultArray() {
        return result_array;
    }
    public static void main(String[] args){
        //這邊的Main是Demo用，你會在你的code的其他地方呼叫API.login(...)

        //範例1: 登入
        //第一步：呼叫login API（寫好了），回傳response code
    	int resCode = API.login("bob", "87654321");
        //在你的code裡，應該會根據這個resCode去決定你要做什麼下個動作

        //下一步：取得剛剛API互叫的結果資料
        // 看hackmd中response回傳的是object還是array(會用[]表示array)
        // 決定你們要呼叫getResultObject()還是getResultArray()

        //這邊用getResultObject()因為login會傳的是object
        //取得id這個attribute，因為type是int所以用getInt()
        //int id = API.getResultObject().getInt("id");
        //取得renter這個attribute(boolean)
       // boolean isRenter = API.getResultObject().getBoolean("renter");
       // System.out.println("id: "+id+", isRenter: "+isRenter);

        //範例2: 取得租借紀錄
        //這個要先登入才能呼叫（要在API註明需要token，下面會有）
        resCode = API.getRentRecord();
        //因為是回傳array所以使用getResultArray()
        //取得第0項紀錄的方法，每一項紀錄都是一個JSONObject
        JSONObject FirstObject = API.getResultArray().getJSONObject(0);
        int length = API.getResultArray().length();
        System.out.println(length);
        //JSONObject跟上面object的呼叫attribute的方法一樣
        System.out.println(FirstObject.getDouble("start_lat"));
        System.out.println(FirstObject.getDouble("end_lat"));
        System.out.println(FirstObject.getDouble("start_lng"));
        System.out.println(FirstObject.getDouble("end_lng"));
        System.out.println(FirstObject.getDouble("totalMinutes"));
        

//        int resCode = API.login("admin", "00001111");
//        resCode = API.getNoPowerCars();
//        JSONObject FirstObject1 = API.getResultArray().getJSONObject(0);
//        System.out.println(FirstObject1.getDouble("lat"));
//        resCode = API.getCarsStatus("BROKEN");
//        JSONObject FirstObject2 = API.getResultArray().getJSONObject(0);
//        System.out.println(FirstObject2.getString("carStatus"));
//        resCode = API.getCarsStatus("NORMAL");
//        JSONObject FirstObject3 = API.getResultArray().getJSONObject(0);
//        System.out.println(FirstObject3.getString("carStatus"));
//        resCode = API.getCarsStatus("REPAIR");
//        JSONObject FirstObject4 = API.getResultArray().getJSONObject(0);
//        System.out.println(FirstObject4.getString("carStatus"));
        
    }

    //APIs 請自行implement其他的
    //這邊我示範了兩個API的寫法，剩下都可以這樣寫
    public static int login(String username, String password){
        //把hackmd上的路徑寫在這
        String url = "/account/login";

        // Create a map to hold the parameters
        //如果有parameter，把它加進一個map（不用管get 或post，已經處理好）
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        //決定要用哪一個method，post就用post，get看他回傳array還是object決定要用getObject()還是getArray()
        //第二個parameter請填他需不需要token（如果後面寫/:userid就需要），login不用
        int resCode = post(url, false, params);
        //初始化token跟id (這邊不用管，寫其他API的時候不用寫)
        if(resCode == 200){
            setToken(API.getResultObject().getString("token"));
            setId(API.getResultObject().getInt("id"));
        }
        return resCode;
    }
    
    public static int registration(String username, String password, String cardnumber, String safenumber, String phonenumber, String email) {
        
        String url = "/account/signup";

        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("cardNumber", cardnumber);
        params.put("safeNumber", safenumber);
        params.put("phoneNumber", phonenumber);
        params.put("email", email);
        
        int resCode = post(url, false, params);
        return resCode;

    }
    
    public static int logout() {
    	String url = "/account/logout/"+getId();
    	Map<String,Object> params = new HashMap<>();
    	int resCode = post(url, true, params);
    	return resCode;
    }
    
    public static int info() {
    	String url = "/account/info/"+getId();
    	Map<String,Object> params = new HashMap<>();
    	int resCode = getObject(url, true, params);
    	return resCode;
    }
    

    public static int getRentRecord(){
        //把hackmd上的路徑寫在這，因為有/:userid，所以加上getId()，其他API照著寫即可
        String url = "/renting/records/"+getId();
        //沒有param，傳入空的map就好
        Map<String, Object> params = new HashMap<>();
        //因為有/:userid 所以第二格填true
        int resCode = getArray(url, true, params);
        return resCode;
    }
    
    public static int getCarsStatus(String CarStatus) {
    	String url = "/repairing/cars";
    	Map<String, Object> params = new HashMap<>();
    	params.put("carStatus", CarStatus);
    	int resCode = getArray(url, false, params);
    	return resCode;
    }
          
    public static int getNoPowerCars() {
    	String url = "/repairing/noPowerCars";
    	Map<String, Object> params = new HashMap<>();
    	int resCode = getArray(url, true, params);
    	return resCode;
    }
    
    public static int changeStatus(String CarStatus,String carNo) {
    	String url = "/repairing/status/"+getId();
    	Map<String, Object> params = new HashMap<>();
        params.put("carNo", carNo);
        params.put("status", CarStatus);
    	int resCode = post(url, true, params);
    	return resCode;
    }
    
    public static int  charge(String carNo) {
    	
    	String url = "/repairing/charge/"+getId();
    	Map<String, Object> params = new HashMap<>();
    	params.put("carNo", carNo);
    	int resCode = post(url, true, params);
    	return resCode;
    }
    
    public static int getCars(double lat, double lng, double range){
        //把hackmd上的路徑寫在這，因為有/:userid，所以加上getId()，其他API照著寫即可
        String url = "/renting/cars";
        //沒有param，傳入空的map就好
        Map<String, Object> params = new HashMap<>();
        params.put("latitude", lat);
        params.put("longitude", lng);
        params.put("range", range);
        //因為有/:userid 所以第二格填true
        int resCode = getArray(url, false, params);
        return resCode;

    }
    
    public static int getCharges(double lat, double lng, double range){
        //把hackmd上的路徑寫在這，因為有/:userid，所以加上getId()，其他API照著寫即可
        String url = "/renting/chargeStation";
        //沒有param，傳入空的map就好
        Map<String, Object> params = new HashMap<>();
        params.put("latitude", lat);
        params.put("longitude", lng);
        params.put("range", range);
        //因為有/:userid 所以第二格填true
        int resCode = getArray(url, false, params);
        return resCode;

    }
    public static int rentingStatus() {
    	String url = "/renting/status/"+getId();
    	Map<String, Object> params = new HashMap<>();
        int resCode = getObject(url, true, params);
        return resCode;
    }
    public static int renting(String carNo) {
    	String url = "/renting/rent/"+getId();
    	
        Map<String, Object> params = new HashMap<>();
        params.put("carNo", carNo);

        int resCode = post(url, true, params);
        
        return resCode;
    }
    
    public static int returnCar(double lat, double lng, double distance, int power, int chargeCount, boolean isUsedCoupon) {
    	String url = "/renting/return/"+getId();
        
        Map<String, Object> params = new HashMap<>();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("distance", distance);
        params.put("power", power);
        params.put("chargeCount", chargeCount);
        params.put("isUsedCoupon", isUsedCoupon);

        int resCode = post(url, true, params);
        
        return resCode;
    }
    
    public static double getDistance(double lat1, double lng1, double lat2, double lng2){
        String fullUrl = String.format("https://router.project-osrm.org/route/v1/driving/%.156f,%.16f;%.16f,%.16f?overview=false", lng1, lat1, lng2, lat2);


        try {
            URL apiUrl = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String resString = response.toString();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + resString);
            JSONObject res = new JSONObject(resString);
            double ans = res.getJSONArray("routes").getJSONObject(0).getDouble("distance");
            connection.disconnect();
            return ans;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Did not receive proper JSON response");
            e.printStackTrace(); // Add this line to print the stack trace for the JSONException
        }
        return -1;
    }
    
    //這邊新增其他API的method...

    ////////////////////////////////下面的code已經寫好，盡量不要動到/////////////////////////////////

    private static int getId(){
        return  id;
    }
    private static void setId(int value){
        id = value;
    }

    private static void setToken(String value) {
        token = value;
    }

    private static String getToken() {
        return token;
    }

    private static void setResult(JSONObject value) {
        result_object = value;
    }

    private static void setResult(JSONArray value) {
        result_array = value;
    }

    public static int getObject(String url, boolean requireToken, Map<String, Object> params) {
        String fullUrl = "http://localhost:8080" + url;
        if (params != null && !params.isEmpty()) {
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                }
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            fullUrl += "?" + queryString.toString();
        }

        if (requireToken && token == null) {
            System.out.println("Token is not set.");
            return 400;
        }

        try {
            URL apiUrl = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            if (requireToken) {
                connection.setRequestProperty("Authorization", getToken());
            }

            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String resString = response.toString();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + resString);
            setResult(new JSONObject(resString));
            connection.disconnect();
            return responseCode;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Did not receive proper JSON response");
            e.printStackTrace(); // Add this line to print the stack trace for the JSONException
        }
        return 400;
    }

    public static int getArray(String url, boolean requireToken, Map<String, Object> params) {
        String fullUrl = "http://localhost:8080" + url;
        if (params != null && !params.isEmpty()) {
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                }
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            fullUrl += "?" + queryString.toString();
        }

        if (requireToken && token == null) {
            System.out.println("Token is not set.");
            return 400;
        }

        try {
            URL apiUrl = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            if (requireToken) {
                connection.setRequestProperty("Authorization", getToken());
            }

            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String resString = response.toString();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + resString);
            if(responseCode == 200) {
            	setResult(new JSONArray(resString));
            }else {
            	setResult(new JSONObject(resString));
            }
            connection.disconnect();
            return responseCode;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Did not receive proper JSON response");
            e.printStackTrace(); // Add this line to print the stack trace for the JSONException
        }
        return 400;
    }

    private static String mapToJsonString(Map<String, Object> params) throws JSONException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
 
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toString();
    }

    public static int post(String url, boolean requireToken, Map<String, Object> params) {
        String fullUrl = "http://localhost:8080" + url;
        String requestBody = mapToJsonString(params);

        if (requireToken && token == null) {
            System.out.println("Token is not set.");
            return 400;
        }


        try {
            URL apiUrl = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            if (requireToken) {
                connection.setRequestProperty("Authorization", getToken());
            }
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String resString = response.toString();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + resString);
            setResult(new JSONObject(resString));
            connection.disconnect();
            return responseCode;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Did not receive proper JSON response");
            e.printStackTrace(); // Add this line to print the stack trace for the JSONException
        }
        return 400;
    }

    //上面都些好了，不用動，其他API都可以照著下面implement

}
