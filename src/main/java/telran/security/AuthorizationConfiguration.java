package telran.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class AuthorizationConfiguration {

	@Autowired
	CustomWebSecurity customWebSecurity;

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults()).csrf(csfr -> csfr.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		http.addFilterBefore(new ExpiredPasswordFilter(), BasicAuthenticationFilter.class);

		http.authorizeHttpRequests(
				authorize -> authorize.requestMatchers( "/account/register", "/account/register/", "/model", "/model/", "/models", "/models/").permitAll()
						.requestMatchers(HttpMethod.PUT, "/account/revoke/*", "/account/activate/*").hasRole("ADMIN")
						.requestMatchers("/account/user/*/role/*").hasAnyRole("ADMIN", "SUPERADMIN")
//						.requestMatchers(HttpMethod.PUT,"/account/user/{login}").access("@customWebSecurity.checkOwner(#login)") -> v5
//						.requestMatchers(HttpMethod.PUT,"/account/user/{login}").access(new WebExpressionAuthorizationManager("@customWebSecurity.checkOwner(#login)")) -> v6.1
						.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
						.access((auth, context) -> new AuthorizationDecision(
								customWebSecurity.checkOwner(context.getVariables().get("login"))))
						.requestMatchers(HttpMethod.GET, "/account/*/{login}")
						.access(new WebExpressionAuthorizationManager(
								"#login == authentication.name or hasRole('ADMIN')"))
						.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
						.access(new WebExpressionAuthorizationManager(
								"#login == authentication.name or hasRole('ADMIN')"))
						.requestMatchers("/account/login", "/account/password", "/car/**").authenticated()
                         //============================RentCompany========================================		
						.requestMatchers(HttpMethod.POST, "/driver/add", "/car/rent", "/car/return").hasRole("CLERK")
						.requestMatchers(HttpMethod.GET, "cars/*").hasRole("CLERK")
						.requestMatchers("/drivers/active", "/models/popular", "/models/profitable/*/*").hasRole("STATIST")
						.requestMatchers("/car/add", "/model/add", "/car/remove", "/model/remove").hasRole("MANAGER")
						.requestMatchers(HttpMethod.GET, "/records/*/*").hasRole("TECHNICIAN")
						.requestMatchers("/driver", "/car/**", "/driver/**").hasAnyRole("DRIVER", "CLERK")
						.anyRequest().denyAll());
		return http.build();
	}

}
