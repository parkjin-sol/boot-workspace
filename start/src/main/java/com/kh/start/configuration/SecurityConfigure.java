package com.kh.start.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.kh.start.configuration.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfigure {
	
	private final JwtFilter filter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		//return httpSecurity.formLogin().disable().build();
		/*
		return httpSecurity.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>> () {
			@Override
			public void customize(FormLoginConfigurer<HttpSecurity> t) {
				t.disable();
			}
			
		}).build();
		
		Cross Site Request Forgery
		
		<img src="http://우리도메인/logout" />
		
		<form action="http://우리도메인/logout" action="post">
			<input type="hidden" value="admin" name="userId" />
			<button>호호호 눌러보세요한번~~</button>
		</form>
		
		*/
		return httpSecurity.formLogin(AbstractHttpConfigurer::disable)
						   .httpBasic(AbstractHttpConfigurer::disable)
						   .csrf(AbstractHttpConfigurer::disable)
						   .authorizeHttpRequests(requests -> {
							   requests.requestMatchers(HttpMethod.POST, "/auth/login","/auth/refresh", "/members").permitAll();
							   requests.requestMatchers("/admin/**").hasRole("ADMIN");
							   requests.requestMatchers(HttpMethod.PUT, "/members").authenticated();
							   requests.requestMatchers(HttpMethod.DELETE, "/members").authenticated();
						   })
						   /*
						    * sessionManagement : 세션을 어떻게 관리할것인지를 지정할 수 있음
						    * sessionCreationPolicy : 세션 사용 정책을 설정
						    */
						   .sessionManagement(manager ->
								   				manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						   .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
						   .build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}