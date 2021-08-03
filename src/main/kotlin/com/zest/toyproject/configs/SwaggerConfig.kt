package com.zest.toyproject.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket


@Configuration
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false)
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
}