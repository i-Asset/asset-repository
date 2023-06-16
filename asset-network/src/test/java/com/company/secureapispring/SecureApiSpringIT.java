package com.company.secureapispring;

import com.company.secureapispring.config.TestSecurityConfig;

import at.srfg.iasset.network.AssetNetworkApplication;

import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {AssetNetworkApplication.class, TestSecurityConfig.class})
public @interface SecureApiSpringIT {
}
