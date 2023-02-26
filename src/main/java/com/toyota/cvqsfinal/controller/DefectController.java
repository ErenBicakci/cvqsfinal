package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.repository.ImageRepository;
import com.toyota.cvqsfinal.service.DefectService;
import com.toyota.cvqsfinal.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/defect")
@RequiredArgsConstructor
public class DefectController {
    private final DefectService defectService;
    private final JwtService jwtService;
    private final ImageRepository imageRepository;


    @PostMapping("/save/{vehicleId}")
    ResponseEntity<DefectDto> defectsave (@PathVariable String vehicleId, @RequestBody DefectDto defectDto, @RequestHeader (name="Authorization") String token)throws Exception{

        DefectDto defectdto = defectService.defectSave(jwtService.findUsername(token.substring(7)),Long.parseLong(vehicleId),defectDto);
        if (defectdto != null){
            return ResponseEntity.ok(defectDto);
        }
        return ResponseEntity.status(400).body(null);


    }

    @GetMapping(value = "/image/{defectId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @Transactional()
    public ResponseEntity<Resource> image(@PathVariable Long defectId, @RequestHeader (name="Authorization") String token) throws IOException {

        ByteArrayResource byteArrayResource = defectService.getImage(defectId);
        if (byteArrayResource != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(byteArrayResource.contentLength())
                    .body(byteArrayResource);
        }
        return null;
    }

    @GetMapping(value = "/delete/{defectId}")
    public void defectDelete(@PathVariable Long defectId, @RequestHeader (name="Authorization") String token) throws IOException {

       defectService.defectDelete(defectId,token.substring(7));
    }

    @GetMapping("/getdefect/{defectId}")
    public ResponseEntity<DefectDto> defectGet(@PathVariable Long defectId, @RequestHeader (name="Authorization") String token) throws IOException {

        DefectDto defectDto = defectService.defectGet(defectId);
        if (defectDto != null){
            return ResponseEntity.ok(defectDto);
        }
        return ResponseEntity.status(400).body(null);
    }



}
