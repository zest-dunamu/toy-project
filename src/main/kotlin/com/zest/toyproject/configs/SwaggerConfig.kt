package com.zest.toyproject.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket


@Configuration
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false)
            .securityContexts(arrayListOf(securityContext()))
            .securitySchemes(arrayListOf<SecurityScheme>(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.zest.toyproject.controllers"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Zest Toy-Project API 문서")
            .description("반갑습니다.토이프로젝트 API 문서입니다. :)")
            .version("1.0")
            .contact(
                Contact(
                    "zest",
                    "https://github.com/zest-dunamu",
                    "zest@dunamu.com",
                )
            )
            .build()
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build()
    }

    private fun defaultAuth(): List<SecurityReference?>? {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return arrayListOf(SecurityReference("JWT", authorizationScopes))
    }

    private fun apiKey(): ApiKey {
        return ApiKey("JWT", "Authorization", "header")
    }
}