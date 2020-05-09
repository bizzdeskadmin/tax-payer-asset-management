package com.bizzdesk.group.taxpayer.asset.management;

import com.bizzdesk.group.taxpayer.asset.management.kafka.AddAssetRequestChannel;
import com.bizzdesk.group.taxpayer.asset.management.kafka.AddAssetResponseChannel;
import com.google.common.base.Predicate;
import com.gotax.framework.library.filter.LogFilter;
import com.gotax.framework.library.helpers.GoTaxLogHandler;
import com.gotax.framework.library.kafka.GoTaxLogChannel;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@EnableBinding({GoTaxLogChannel.class, AddAssetRequestChannel.class, AddAssetResponseChannel.class})
public class TaxPayerAssetManagementConfiguration {

    @Bean
    public GoTaxLogHandler customHandlerInterceptor() {
        return new GoTaxLogHandler();
    }

    @Bean
    public Filter LogsFilter() {
        return new LogFilter(customHandlerInterceptor());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths())
                .build();

    }
    @Bean
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Tax Payer Management")
                .description("Tax Payer Management")
                .termsOfServiceUrl("")
                .license("")
                .licenseUrl("")
                .version("1.0")
                .build();
    }
    //Here is an example where we select any api that matches one of these paths
    private static Predicate<String> paths() {
        return or(
                regex("/v1.*")
        );
    }
}
