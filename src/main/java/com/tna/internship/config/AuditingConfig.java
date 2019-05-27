package com.tna.internship.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tna.internship.security.UserPrincipal;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
	@Bean
	public AuditorAware<Long> auditorProvider() {
		return new SpringSecurityAuditAwareImpl();
	}

	class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {

		@Override
		public Optional<Long> getCurrentAuditor() {
			// trả về user hiện tại
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null) {
				return Optional.empty();
			}

			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

			return Optional.ofNullable(userPrincipal.getId());
		}
	}
}
