package iocode.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point of the IO Bank application.
 * This class is annotated with {@link SpringBootApplication} which is a convenience annotation that adds all the following:
 * {@link Configuration}, {@link EnableAutoConfiguration}, {@link ComponentScan}.
 *
 * @author Tabnine
 * @since 1.0.0
 */
@SpringBootApplication
public class IobankApplication {

    /**
     * The main method to run the Spring Boot application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(IobankApplication.class, args);
    }

}
