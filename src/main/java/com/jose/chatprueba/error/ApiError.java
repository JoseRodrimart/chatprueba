package com.jose.chatprueba.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ApiError {
    @NonNull
    private HttpStatus estado;
    @NonNull
    private String mensaje;
    @JsonFormat(shape = Shape.STRING, pattern="dd/MM/yyyy hh:mm:ss")
    private LocalDateTime fecha = LocalDateTime.now();
}
