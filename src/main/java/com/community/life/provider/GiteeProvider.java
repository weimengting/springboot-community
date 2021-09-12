package com.community.life.provider;

import com.alibaba.fastjson.JSON;
import com.community.life.dto.AccessToken;
import com.community.life.dto.AccessTokenDto;
import com.community.life.dto.GiteeUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component  //不需要实例化对象，直接调用（IOC）
public class GiteeProvider {

    //做post请求，将accessToken的一系列信息传入GitHub，来换取登录信息（传入的accessToken和返回的不一样）
    public String getAccessToken(AccessTokenDto accessTokenDto) {
        MediaType mediaType = MediaType.Companion.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.Companion.create(JSON.toJSONString(accessTokenDto), mediaType);
        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token?grant_type=authorization_code")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String code = response.body().string(); //此处得到的类型仍然是json格式，需要进一步解析得到access_token
            System.out.println(code); //防止响应为空
            AccessToken accessToken = JSON.parseObject(code, AccessToken.class);
            return accessToken.getAccess_token();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //做get请求，根据上个方法拿到的accessToken向GitHub索要对应的user信息
    public GiteeUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token=" + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String resp = response.body().string();
            //将resp的json对象解析成GitHubuser的类对象，这样就不用自己去分析resp了（okhttp已经说明返回是一个json的string）
            GiteeUser giteeUser = JSON.parseObject(resp, GiteeUser.class);
            return giteeUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
