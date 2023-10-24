package ChessOnline.Config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("ChessOnline")
public class SpringConfig implements WebMvcConfigurer {
	private ApplicationContext context;
	
	SpringConfig(ApplicationContext context) {
		this.context = context;
	}
	
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver template = new SpringResourceTemplateResolver();
		
		template.setPrefix("/WEB-INF/templates/");
		template.setSuffix(".html");
		template.setApplicationContext(context);
		
		return template;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		
		engine.setTemplateResolver(templateResolver());
		engine.setEnableSpringELCompiler(true);
		
		return engine;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		ThymeleafViewResolver thymeleaf = new ThymeleafViewResolver();
		
		thymeleaf.setTemplateEngine(templateEngine());
		thymeleaf.setCharacterEncoding("UTF-8");
		
		registry.viewResolver(thymeleaf);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/index/**")
		        .addResourceLocations("classpath:/static/index/");
		
		registry.addResourceHandler("/lobby/**")
		        .addResourceLocations("classpath:/static/lobby/");
	}
}
