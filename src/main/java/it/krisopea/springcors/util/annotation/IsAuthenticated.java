package it.krisopea.springcors.util.annotation;

import it.krisopea.springcors.util.constant.RoleConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('" + RoleConstants.ROLE_ADMIN + "','" + RoleConstants.ROLE_USER + "')")
public @interface IsAuthenticated {}
