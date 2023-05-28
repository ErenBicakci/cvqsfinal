package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.log.CustomLogInfo;
import com.toyota.cvqsfinal.log.CustomLogInfoWithoutParameters;
import com.toyota.cvqsfinal.service.DefectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/api/v1/defect")
@RequiredArgsConstructor
public class DefectController {
    private final DefectService defectService;


    @CustomLogInfoWithoutParameters
    @PostMapping
    ResponseEntity<Boolean> defectsave ( @RequestBody DefectDto defectDto){
        boolean bool = defectService.defectSave(defectDto);
        if (bool){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(400).body(false);


    }
    @CustomLogInfoWithoutParameters
    @GetMapping(value = "/image/{defectId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @Transactional()
    public ResponseEntity<Resource> image(@PathVariable Long defectId) {


        ByteArrayResource byteArrayResource = defectService.getImage(defectId);
        if (byteArrayResource != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(byteArrayResource.contentLength())
                    .body(byteArrayResource);
        }

        return null;
    }

    @CustomLogInfo
    @DeleteMapping(value = "/{defectId}")
    public ResponseEntity<Boolean> defectDelete(@PathVariable Long defectId){

        if (defectService.defectDelete(defectId)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(400).body(false);
    }

    @CustomLogInfo
    @GetMapping("/{defectId}")
    public ResponseEntity<DefectDto> defectGet(@PathVariable Long defectId) {

        DefectDto defectDto = defectService.defectGet(defectId);
        if (defectDto != null){
            return ResponseEntity.ok(defectDto);
        }

        return ResponseEntity.status(400).body(null);
    }

    @CustomLogInfoWithoutParameters
    @PutMapping
    public ResponseEntity<DefectDto> defectUpdate( @RequestBody DefectDto defectDto)  {
        DefectDto defectDto1 = defectService.updateDefect(defectDto);
        if (defectDto1 != null){

            return ResponseEntity.ok(defectDto);
        }
        return ResponseEntity.status(400).body(null);
    }
}