package com.jose.chatprueba.controller;

import java.io.IOException;
import java.nio.file.Files;


import com.jose.chatprueba.services.IFicheroServices;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FicherosController {

    //private static final Logger logger = LoggerFactory.getLogger(FicherosController.class);

    private final IFicheroServices IFicherosServicio;

    @GetMapping(value="/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(
            @PathVariable String filename) {
        Resource file = IFicherosServicio.loadAsResource(filename);
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException ex) {
            System.err.println("Ha habido un error determinando el tipo del fichero");
        }

        if(contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}
