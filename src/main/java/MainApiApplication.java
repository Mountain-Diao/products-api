import com.controller.ApiController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@ComponentScan(basePackageClasses = ApiController.class, basePackages = {"com.dao", "com.mapper", "com.bo", "com.config"})
public class MainApiApplication {
    public static void main(String[] args){
        SpringApplication.run(MainApiApplication.class, args);
    }
}
