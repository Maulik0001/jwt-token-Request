package com.example.demo;

import java.nio.charset.Charset;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date; 

public class ApiCall {
	
	public static String callAPI(String callUrl) {
		
		
		String urlCall= callUrl+"rest/jwtSecurityReceive";
		
		String output = callGetApi(urlCall);
		
		return output;
		
	}

	private static String callGetApi(String urlCall) {
		
		 long timeMillis = System.currentTimeMillis();
		
		 String id = UUID.randomUUID().toString().replace("-", "");
		String jwtOutput = createJWT(id, "eSense", "updateData", timeMillis);
		
		System.out.println("jwtOutput::"+jwtOutput);
		
		String param = "{'jwtToken':'"+jwtOutput+"'}";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
			HttpEntity<String> entity = new HttpEntity<String>(param,headers);
			if(entity.hasBody()) {
				
			}
			HttpHeaders httpHeaders = entity.getHeaders();
			for(Entry<String, List<String>> obj :httpHeaders.entrySet() ) {
				
			}
			
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			List<HttpMessageConverter<?>> c = restTemplate.getMessageConverters();
			for(HttpMessageConverter<?> mc : c) {
				if(mc instanceof StringHttpMessageConverter) {
					StringHttpMessageConverter mcc = (StringHttpMessageConverter) mc;
					mcc.setWriteAcceptCharset(false);
				}
			}
			
			ResponseEntity<String> response = restTemplate.exchange(urlCall, HttpMethod.POST,entity,String.class);
			
			if(response.getStatusCode() == HttpStatus.OK) {
				
				/*System.out.println("response.getStatusCode()::"+response.getStatusCode());
				System.out.println("response.getBody()::"+response.getBody().toString());*/
				return response.getBody().toString();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	private static String createJWT(String id, String issuer, String subject, long ttlMillis) {

	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);

	    //We will sign our JWT with our ApiKey secret
	    @SuppressWarnings("restriction")
	    
	      final String secret = "JwtQwx@PeS!ense#22011992$MbPtl21";
	 //   final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
	 //   final byte[] secretBytes = secret.getEncoded();
	 //   final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

	    
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(id)
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .setIssuer(issuer)
	                                .signWith(signatureAlgorithm, signingKey);

	    //if it has been specified, let's add the expiration
	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }

	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	
}
