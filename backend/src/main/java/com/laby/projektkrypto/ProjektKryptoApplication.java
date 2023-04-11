package com.laby.projektkrypto;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.SneakyThrows;

import static com.laby.projektkrypto.rest.DatabaseController.DUMP_PATH;
import static com.laby.projektkrypto.rest.EventController.EXPORT_PATH;

@SpringBootApplication
public class ProjektKryptoApplication {
	@SneakyThrows
	public static void main(String[] args) {
		Files.createDirectories(Paths.get(DUMP_PATH));
		Files.createDirectories(Paths.get(EXPORT_PATH));
		SpringApplication.run(ProjektKryptoApplication.class, args);
	}
}
