package vn.utc.hotelmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(
		basePackages = "vn.utc.clients"
)
public class HotelmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelmanagerApplication.class, args);
	}

}
