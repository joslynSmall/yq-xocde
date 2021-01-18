package com.yq.xcode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yq.xcode.security.access.PermissionVoter;


@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class XCodeApplication {

	@Bean
	public AffirmativeBased AffirmativeBased(){
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
		PermissionVoter permissionVoter = new PermissionVoter();
		decisionVoters.add(permissionVoter);
		
		AffirmativeBased AffirmativeBased = new AffirmativeBased(decisionVoters);
		return AffirmativeBased;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(XCodeApplication.class, args);
	}

}
