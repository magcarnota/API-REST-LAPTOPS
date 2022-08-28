package com.openbootcamp.ejercicios;

import com.openbootcamp.ejercicios.entities.Laptop;
import com.openbootcamp.ejercicios.repositories.LaptopRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EjerciciosApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(EjerciciosApplication.class, args);

		LaptopRepository laptopRepository = context.getBean(LaptopRepository.class);

		Laptop laptop1 = new Laptop(null, "Asus", "RTX 2000" , 1000.00);
		Laptop laptop2 = new Laptop(null, "Apple", "iMac" , 1500.00);

		laptopRepository.save(laptop1);
		laptopRepository.save(laptop2);

	}

}
