package com.kh.start.token.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.start.auth.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
	
	private final JwtUtil tokenUitl;
	
	@Override
	public Map<String, String> generateToken(String username) {
		
		// 1, 2번
		Map<String, String> tokens = createToken(username);
		
		// 3번
		
		
	
		
		
		return tokens;
	}
	
	private Map<String, String> createToken(String username) {
		String accessToken = tokenUitl.getAccessToken(username);
		String refreshToken = tokenUitl.getRefreshToken(username);
		
		Map<String, String> loginResponse = new HashMap();
		loginResponse.put("accessToken", accessToken);
		loginResponse.put("refreshToken", refreshToken);
		
		return loginResponse;
	}
}
