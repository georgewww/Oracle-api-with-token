package com.auth0.samples.authapi.security;

public class SecurityConstants {
	public static final String SECRET = "";
	public static final long EXPIRATION_TIME = 64_000_000; // 18 hours
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
}
