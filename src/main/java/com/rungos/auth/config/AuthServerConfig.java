package com.rungos.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @Description 认证服务器配置
 * @date 2019年8月14日 下午4:03:49
 * @author Zero
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * @Deprecated 注入authenticationManager 来支持 password grant type
	 */
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;

	private TokenStore tokenStore = new InMemoryTokenStore();

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// @formatter:off
		endpoints.tokenStore(this.tokenStore).authenticationManager(authenticationManager)
				// 使用refresh_token 需要配置userDetailsService
				.userDetailsService(userDetailsService);
		// @formatter:on
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		// @formatter:off
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").allowFormAuthenticationForClients();
		// @formatter:on
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// @formatter:off
		clients.inMemory() // 内存配置
				.withClient("rungos_clients") // 客户端id
				.secret(passwordEncoder.encode("rungos")) // 客户端秘钥
				.redirectUris("https://www.baidu.com") // 回调地址
				.authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token", "password",
						"implicit") // 授权方式
				.scopes("all") // 权限范围
				// .autoApprove(true) // 同意授权
				.accessTokenValiditySeconds(60) // access_token 过期时间（秒）
				.refreshTokenValiditySeconds(3600); // refresh_token 过期时间（秒）
		// @formatter:on
	}

}
