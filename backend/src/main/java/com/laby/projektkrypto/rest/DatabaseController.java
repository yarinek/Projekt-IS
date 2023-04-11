package com.laby.projektkrypto.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/rest/db")
@Slf4j
public class DatabaseController
{
    public static final String DUMP_PATH = "db_data/";
    private static final String DB_CONTAINER_NAME = "projektkrypto_db_1";
    private static final String DATABASE = "baza_krypto";
    private static final String USER = "postgres";

    @GetMapping("/export")
    public ResponseEntity<UrlResource> exportDatabase() throws InterruptedException, IOException
    {
        var command = String.format("docker exec -i %s pg_dump -c -U %s %s", DB_CONTAINER_NAME, USER, DATABASE);
        var path = Paths.get(DUMP_PATH + "dump-" + UUID.randomUUID() + ".sql");

        var pb = new ProcessBuilder("cmd", "/c", command);
        var result = path.toAbsolutePath().toFile();
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(result));
        Process process = pb.start();
        process.waitFor();
        if (process.exitValue() != 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=db_dump.sql")
                .body(resource);
    }

    @PostMapping("/import")
    public ResponseEntity<Integer> importDatabase(@RequestParam("file") MultipartFile file) throws InterruptedException, IOException {
        var fileName = "import-" + UUID.randomUUID() + ".sql";
        var path = Paths.get(DUMP_PATH + fileName);

        try {
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }

            var command = String.format("docker exec -i %s psql -U %s %s < %s", DB_CONTAINER_NAME, USER, DATABASE, fileName);
            log.info("Starting command: {}", command);
            var pb = new ProcessBuilder("cmd", "/c", command);
            pb.directory(Paths.get(DUMP_PATH).toFile());
            Process process = pb.start();
            process.waitFor();

            if (process.exitValue() != 0) {
                log.info("Command ({}), failed! [exitValue=[{}]]", command, process.exitValue());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } finally {
            Files.delete(path);
        }
    }
}
