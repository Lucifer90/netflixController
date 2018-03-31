package it.fanciullini.netflixapi;

import it.fanciullini.data.entity.Credentials;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Controller
public class NetflixApi {

    private MultiValueMap<String, String> generatePayload(String username, String password){
        MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();

        param.put("email", Collections.singletonList(username));
        param.put("password", Collections.singletonList(password));
        param.put("rememberMe", Collections.singletonList("true"));
        param.put("flow", Collections.singletonList("websiteSignUp"));
        param.put("mode", Collections.singletonList("login"));
        param.put("action", Collections.singletonList("loginAction"));
        param.put("withFields", Collections.singletonList("email,password,rememberMe,nextPag"));
        //param.put("authURL", Collections.singletonList("BelecthorVSTheDeath"));
        param.put("nextPage", Collections.singletonList(""));

        return param;
    }

    public String netflixLogin(){
        RestTemplate template = new RestTemplate();
        Credentials cred = new Credentials();
        cred.setUserName("");
        cred.setPassword("");
        String url = "https://www.netflix.com/it/login";

        HttpEntity<Credentials> request = new HttpEntity<>(cred);
        HttpEntity<String> response = template.exchange(url, HttpMethod.POST, request, String.class);
        HttpHeaders headers = response.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        return set_cookie;
    }

    public ResponseEntity<String> getInfoAccount(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.netflix.com/YourAccount";
        ResponseEntity<String> response = restTemplate.getForEntity( url, null , String.class );
        return response;
    }

    public String getInfoBilling(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.netflix.com/BillingActivity";
        String response = restTemplate.getForObject( url, String.class );
        return response;
    }

    public static void main(String[] args){
        NetflixApi netflixApi = new NetflixApi();
        String cookie = netflixApi.netflixLogin();
        netflixApi.getInfoAccount();
        netflixApi.getInfoBilling();
    }

}
