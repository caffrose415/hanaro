package com.hana7.hanaro.config;

import static org.springframework.http.MediaType.*;

import java.util.List;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		Server devServer = new Server();
		Server prodServer = new Server();
		devServer.setUrl("/");
		prodServer.setUrl("/api");

		return new OpenAPI()
			.servers(List.of(devServer, prodServer))
			.info(getInfo())
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
			.components(new Components()
				.addSecuritySchemes("bearerAuth",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				)
			);

	}

	@Bean
	public OpenApiCustomizer globalErrorResponses() {
		return openApi -> openApi.getPaths().values().forEach(pathItem ->
			pathItem.readOperations().forEach(op -> {
				ApiResponses rs = op.getResponses();
				rs.addApiResponse("400", apiResp("Bad Request", Map.of(
					"timestamp","2025-08-12T10:22:33.123Z",
					"path","/admin/items",
					"method","POST",
					"code","C001",
					"message","요청 값이 올바르지 않습니다.",
					"errors", List.of(
						Map.of("field","name", "reason","상품명은 필수입니다.")
					),
					"detail", Map.of()
				)));
				rs.addApiResponse("404", apiResp("Not Found", Map.of(
					"timestamp","2025-08-12T10:22:33.123Z",
					"path","/user/items/999",
					"method","GET",
					"code","C404",
					"message","리소스를 찾을 수 없습니다.",
					"errors", List.of(),
					"detail", Map.of("resource","Item","id",999)
				)));
				rs.addApiResponse("500", apiResp("Internal Server Error", Map.of(
					"timestamp","2025-08-12T10:22:33.123Z",
					"path","/any",
					"method","GET",
					"code","S001",
					"message","서버 내부 오류",
					"errors", List.of(),
					"detail", Map.of("traceId","0000000000000000")
				)));
			})
		);
	}

	private ApiResponse apiResp(String desc, Object example) {
		var media = new MediaType();
		media.schema(new io.swagger.v3.oas.models.media.Schema<>().$ref("#/components/schemas/ErrorResponse"));
		media.setExample(example); // << Example Value 영역에 그대로 노출
		return new ApiResponse().description(desc)
			.content(new io.swagger.v3.oas.models.media.Content()
				.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, media));
	}
	private Info getInfo() {
		return new Info()
			.version("0.1.0")
			.title("Hanaro APIs")
			.description("Hanaro 7 Project API Documents");
	}

}
