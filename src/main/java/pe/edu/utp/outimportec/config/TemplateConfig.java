package pe.edu.utp.outimportec.config;

import freemarker.cache.ClassTemplateLoader;

import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TemplateConfig {
    @Bean
    public Configuration configuration() {
        Configuration config = new Configuration(Configuration.VERSION_2_3_31);
        ClassTemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/reporte");
        config.setTemplateLoader(templateLoader);
        config.setDefaultEncoding("UTF-8");
        return config;
    }

}
