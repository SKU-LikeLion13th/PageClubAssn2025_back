package likelion13.page.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"defalt", "build"})
public class BuildConfiguration {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("page")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://puzzle.likelion12.kro.kr/").description("Default Server URL"));
    }
}
