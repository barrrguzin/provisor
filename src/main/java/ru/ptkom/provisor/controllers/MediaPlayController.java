package ru.ptkom.provisor.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.ptkom.provisor.service.MediaStreamLoader;
import ru.ptkom.provisor.service.MediaStreamLoaderImpl;

@RestController
public class MediaPlayController
{

    private MediaStreamLoaderImpl mediaLoaderService;

    public MediaPlayController(MediaStreamLoaderImpl mediaLoaderService)
    {
        this.mediaLoaderService = mediaLoaderService;
    }

    @GetMapping(value = "/play/media/v01/{vid_id}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> playMediaV01(
            @PathVariable("vid_id")
            String video_id,
            @RequestHeader(value = "Range", required = false)
            String rangeHeader)
    {
        try
        {
            StreamingResponseBody responseStream;
            String filePathString = "C:\\Users\\AOSunitskii\\Desktop\\Eltex\\888111.mp4";
            Path filePath = Paths.get(filePathString);
            Long fileSize = Files.size(filePath);
            byte[] buffer = new byte[1024];
            final HttpHeaders responseHeaders = new HttpHeaders();

            if (rangeHeader == null)
            {
                responseHeaders.add("Content-Type", "video/mp4");
                responseHeaders.add("Content-Length", fileSize.toString());
                responseStream = os -> {
                    RandomAccessFile file = new RandomAccessFile(filePathString, "r");
                    try (file)
                    {
                        long pos = 0;
                        file.seek(pos);
                        while (pos < fileSize - 1)
                        {
                            file.read(buffer);
                            os.write(buffer);
                            pos += buffer.length;
                        }
                        os.flush();
                    } catch (Exception e) {}
                };

                return new ResponseEntity<StreamingResponseBody>(responseStream, responseHeaders, HttpStatus.OK);
            }

            String[] ranges = rangeHeader.split("-");
            Long rangeStart = Long.parseLong(ranges[0].substring(6));
            Long rangeEnd;
            if (ranges.length > 1)
            {
                rangeEnd = Long.parseLong(ranges[1]);
            }
            else
            {
                rangeEnd = fileSize - 1;
            }

            if (fileSize < rangeEnd)
            {
                rangeEnd = fileSize - 1;
            }

            String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            responseHeaders.add("Content-Type", "video/mp4");
            responseHeaders.add("Content-Length", contentLength);
            responseHeaders.add("Accept-Ranges", "bytes");
            responseHeaders.add("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize);
            final Long _rangeEnd = rangeEnd;
            responseStream = os -> {
                RandomAccessFile file = new RandomAccessFile(filePathString, "r");
                try (file)
                {
                    long pos = rangeStart;
                    file.seek(pos);
                    while (pos < _rangeEnd)
                    {
                        file.read(buffer);
                        os.write(buffer);
                        pos += buffer.length;
                    }
                    os.flush();
                }
                catch (Exception e) {}
            };

            return new ResponseEntity<StreamingResponseBody>(responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT);
        }
        catch (FileNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/play/media/v02/{vid_id}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> playMediaV02(
            @PathVariable("vid_id")
            String video_id,
            @RequestHeader(value = "Range", required = false)
            String rangeHeader)
    {
        try
        {
            String filePathString = "C:\\Users\\AOSunitskii\\Desktop\\Eltex\\888.mp4";
            ResponseEntity<StreamingResponseBody> retVal = mediaLoaderService.loadPartialMediaFile(filePathString, rangeHeader);

            return retVal;
        }
        catch (FileNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
