package community2.demo.provier;


import com.alibaba.fastjson.JSON;
import community2.demo.dto.AccessTakenDto;
import community2.demo.dto.GithubUser;
import okhttp3.*;
import okhttp3.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
* 用来获取github第三方登录的信息*/
@Component
public class GithubProvider {
    public String getaccesstaken(AccessTakenDto accesstakendto){

       MediaType mediaType = MediaType.get("application/json; charset=utf-8");
       OkHttpClient client = new OkHttpClient();

       RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accesstakendto));
       Request request = new Request.Builder()
               .url("https://github.com/login/oauth/access_token")
               .post(body)
               .build();
       try (Response response = client.newCall(request).execute()) {
           //return response.body().string();
           String string =response.body().string();
           String token = string.split("&")[0].split("=")[1];;
           return token;
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
    }


//    public GithubUser getuser(String accesstoken){
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                //github新的获取到token的方法,这样更安全
//                .url("https://api.github.com/user")
//                .header("Authorization","token"+accesstoken)
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            assert response.body() != null;
//            String string = response.body().toString();
//            System.out.println(string);
//            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
//            return githubUser;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    /*
    因为okhttp3会出现connect reset，所以使用更为稳定的HttpClients
    * */
    public GithubUser getUser(String accessToken) throws Exception {
        int timeout = 60;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://api.github.com/user");
        httpget.addHeader(new BasicHeader("Authorization", "token "+accessToken));
        httpget.setProtocolVersion(HttpVersion.HTTP_1_0);
        httpget.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        httpget.setConfig(defaultRequestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            //System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            GithubUser user = JSON.parseObject(entity.getContent(),GithubUser.class);
            EntityUtils.consume(entity);
            response.close();
            return  user;
        } catch (Exception e){
            throw e;
        } finally{
            if(response != null){
                response.close();
            }
            if(httpclient!=null){
                httpclient.close();
            }
        }
    }

}
