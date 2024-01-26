import com.controller.ApiController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@EnableJpaRepositories("com.dao")
@EntityScan("com.model")
@ComponentScan(basePackageClasses = ApiController.class, basePackages = {"com.dao", "com.mapper", "com.bo", "com.config"})
public class MainApiApplication {
    public static void main(String[] args){
        SpringApplication.run(MainApiApplication.class, args);
    }
}
