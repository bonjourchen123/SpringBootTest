package com.core.util;

import java.io.FileInputStream;
import java.util.Arrays;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class FirebaseDynamicLinksUtil {

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
	
	// REST
	// 建立 https://firebasedynamiclinks.googleapis.com/v1/shortLinks
	// params key
	// body 
	//	{
	//	  "dynamicLinkInfo": {
	//	    "dynamicLinkDomain": "bonjourtest.page.link",
	//	    "link": "https://www.google.com.tw/"
	//	  }
	//	}
	
	// 查詢點擊數 https://firebasedynamiclinks.googleapis.com/v1/ (link URL) /linkStats?durationDays=9999
	// ex. https://firebasedynamiclinks.googleapis.com/v1/https%3A%2F%2Fbonjourtest.page.link%2Ff8UftHSWUiWoFAFE8/linkStats?durationDays=9999
	// header: Authorization Bearer (token)
	
}
