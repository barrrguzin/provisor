package ru.ptkom.provisor.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface MediaStreamLoader
{
    ResponseEntity<StreamingResponseBody> loadEntireMediaFile(String localMediaFilePath) throws IOException;

    ResponseEntity<StreamingResponseBody> loadPartialMediaFile(String localMediaFilePath, String rangeValues) throws IOException;

    ResponseEntity<StreamingResponseBody> loadPartialMediaFile(String localMediaFilePath, long fileStartPos, long fileEndPos) throws IOException;
}
