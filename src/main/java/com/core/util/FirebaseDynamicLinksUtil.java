package com.core.util;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

@Component
public class FirebaseDynamicLinksUtil {

	/**
	 * 建立短網址用
	 */
	private static final String FIREBASE_DYNAMIC_LINK_BUILD_URL = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=";
	
	/**
	 * 分析短網址用
	 */
	private static final String FIREBASE_DYNAMIC_LINK_ANALYTICS_URL = "https://firebasedynamiclinks.googleapis.com/v1/%s/linkStats?durationDays=%s";
	
	/**
	 * api key
	 */
	private static String FIREBASE_API_KEY;
	
	@Value("${firebase.api.key}")
	private void serFirebaseApiKey(String firebaseApiKey){
		FIREBASE_API_KEY = firebaseApiKey;
	}
	
	/**
	 * google 帳號資料位置
	 */
	private static String GOOGLE_DATA_PATH;
	
	@Value("${google.data.path}")
	private void serGoogleDataPath(String googleDataPath){
		GOOGLE_DATA_PATH = googleDataPath;
	}
	
	/**
	 * 取得 google firebase token
	 * 
	 * @param googleDataPath (ex. src/main/resources/bonjourchen-test-firebase-adminsdk.json)
	 * @return
	 */
	public static String getGoogleToken(String googleDataPath){
		String token = null;
		
		try {
			// Load the service account key JSON file
			FileInputStream serviceAccount = new FileInputStream(googleDataPath);

			// Authenticate a Google credential with the service account
			GoogleCredential googleCred = GoogleCredential.fromStream(serviceAccount);
	
			// Add the required scope to the Google credential
			GoogleCredential scoped = googleCred.createScoped(
			    Arrays.asList(
			      "https://www.googleapis.com/auth/firebase"
			    )
			);
	
			// Use the Google credential to generate an access token
			scoped.refreshToken();
			token = scoped.getAccessToken();
	
			// Include the access token in the Authorization header.
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return token;
	}
	
	/**
	 * 使用 firebase Dynamic Links 提供之 restful api 建立短網址
	 * (rstful 資料文件 https://firebase.google.com/docs/reference/dynamic-links/link-shortener)
	 * 
	 * @param domain 建立短網址的網域
	 * @param url 要編成短網址的內容
	 * @param title 標題
	 * @param description 細節
	 * @param imageLink 圖片網址
	 * @param suffixIsShort 後綴網址是否用短的(4碼)(長為17碼)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String buildDynamicLinkByFirebase(String domain, String url, 
			String title, String description, String imageLink, boolean suffixIsShort){
		String shortLink = null;
		
		if(! StringUtils.isEmpty(domain) && ! StringUtils.isEmpty(url)){
			RestTemplate restTemplate = new RestTemplate();
			
			// 參數範例
			//  {
			//	  "dynamicLinkInfo": {
			//	    "domainUriPrefix": "bonjourtest.page.link",
			//	    "link": "https://www.google.com.tw/",
		    //      "socialMetaTagInfo": {
			//          "socialTitle": "這是標題",
			//          "socialDescription": "這是細節文字",
		    //          "socialImageLink": "這是圖片網址"
		    //      }
			//	  },
			//    "suffix": {
			//      "option": "SHORT" or "UNGUESSABLE"
			//    }
			//	}
			Map<String, Object> dynamicLinkInfo = new HashMap<String, Object>(); 
			dynamicLinkInfo.put("domainUriPrefix", domain);
			dynamicLinkInfo.put("link", url);
			
			// 細節資訊 非必填
			if(! StringUtils.isEmpty(title) || ! StringUtils.isEmpty(description) || ! StringUtils.isEmpty(imageLink)){
				Map<String, String> socialMetaTagInfo = new HashMap<String, String>();
				if(! StringUtils.isEmpty(title)){
					socialMetaTagInfo.put("socialTitle", title);
				}
				if(! StringUtils.isEmpty(description)){
					socialMetaTagInfo.put("socialDescription", description);
				}
				if(! StringUtils.isEmpty(imageLink)){
					socialMetaTagInfo.put("socialImageLink", imageLink);
				}
				dynamicLinkInfo.put("socialMetaTagInfo", socialMetaTagInfo);
			}
			
			Map<String, Object> suffix = new HashMap<String, Object>();
			suffix.put("option", suffixIsShort ? "SHORT" : "UNGUESSABLE"); // SHORT為隨機四碼 UNGUESSABLE為17碼
			
			Map<String, Map<String, Object>> params = new HashMap<String, Map<String, Object>>();
			params.put("dynamicLinkInfo", dynamicLinkInfo);
			params.put("suffix", suffix);
			
			// 呼叫 POST 建立短網址
			Map<String, Object> resultMap = restTemplate.postForObject(FIREBASE_DYNAMIC_LINK_BUILD_URL + FIREBASE_API_KEY, params, Map.class);
			shortLink = String.valueOf(resultMap.get("shortLink"));
		}
		
		return shortLink;
	}
	
	/**
	 * 使用 firebase Dynamic Links 提供之 restful api 建立短網址
	 * (rstful 資料文件 https://firebase.google.com/docs/reference/dynamic-links/link-shortener)
	 * 
	 * @param domain 建立短網址的網域
	 * @param url 要編成短網址的內容
	 * @param suffixIsShort 後綴網址是否用短的(4碼)(長為17碼)
	 * @return
	 */
	public static String buildDynamicLinkByFirebase(String domain, String url, boolean suffixIsShort){
		return buildDynamicLinkByFirebase(domain, url, null, null, null, suffixIsShort);
	}
	
	/**
	 * 使用 firebase Dynamic Links 提供之 restful api 建立短網址
	 * (rstful 資料文件 https://firebase.google.com/docs/reference/dynamic-links/link-shortener)
	 * 
	 * @param domain 建立短網址的網域
	 * @param url 要編成短網址的內容
	 * @return
	 */
	public static String buildDynamicLinkByFirebase(String domain, String url){
		return buildDynamicLinkByFirebase(domain, url, true);
	}
	
	/**
	 * 查詢 firebase Dynamic Links 點擊數資料
	 * (rstful 參考文件 https://firebase.google.com/docs/reference/dynamic-links/analytics)
	 * 
	 * @param shortLink 查詢網址
	 * @param durationDays 查詢期限
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> analyticsFirebaseDynamicLink(String shortLink, String durationDays){
		Map<String, Object> result = null;
		
		if(! StringUtils.isEmpty(shortLink)){
			RestTemplate restTemplate = new RestTemplate();
			
			// 轉換特殊符號
			shortLink = shortLink.replaceAll(":", "%3A");
			shortLink = shortLink.replaceAll("/", "%2F");
			
			// 分析的網址
			// ex. https://firebasedynamiclinks.googleapis.com/v1/https%3A%2F%2Fbonjourtest.page.link%2Ff8UftHSWUiWoFAFE8/linkStats?durationDays=9999
			URI uri = URI.create(String.format(FIREBASE_DYNAMIC_LINK_ANALYTICS_URL, shortLink, durationDays));
			
			// 將 token 放入 header
			// header: Authorization Bearer (token)
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Authorization", "Bearer " + getGoogleToken(GOOGLE_DATA_PATH));

			// 呼叫 GET 分析短網址
			ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity(httpHeaders), Map.class);
			result = response.getBody();
		}
		
		return result;
	}
}
