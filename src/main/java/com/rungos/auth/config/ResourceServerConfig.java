package com.rungos.auth.config;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 资源服务器配置
 * @date 2019年8月14日 下午4:03:27
 * @author Zero
 */
@Configuration
@EnableResourceServer
@RestController
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.requestMatchers().antMatchers("/api/**").and().authorizeRequests().antMatchers("/api/**").authenticated();
		// @formatter:on
	}

	@GetMapping("/api/principal")
	public Principal Principal(Principal principal) {
		return principal;
	}
	
	@GetMapping("/api/user")
	public Map<String, Object> user() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", "rungos");
		map.put("timestamp", System.currentTimeMillis());
		return map;
	}

}
