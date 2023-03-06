package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.service.DefectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/api/defect")
@RequiredArgsConstructor
public class DefectController {
    private final DefectService defectService;


    @PostMapping("/save")
    ResponseEntity<DefectDto> defectsave ( @RequestBody DefectDto defectDto)throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName() + "Send defect save request");

        DefectDto defectdto = defectService.defectSave(defectDto);
        if (defectdto != null){

            return ResponseEntity.ok(defectDto);
        }

        return ResponseEntity.status(400).body(null);


    }

    @GetMapping(value = "/image/{defectId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @Transactional()
    public ResponseEntity<Resource> image(@PathVariable Long defectId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName() + "Send get image request : (defectId) " + defectId);

        ByteArrayResource byteArrayResource = defectService.getImage(defectId);
        if (byteArrayResource != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(byteArrayResource.contentLength())
                    .body(byteArrayResource);
        }
        log.error(auth.getName() + "defect Image Not Found ! : (defectId) " + defectId);

        return null;
    }

    @DeleteMapping(value = "/delete/{defectId}")
    public void defectDelete(@PathVariable Long defectId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName() + "send defect delete request ! : (defectId) " + defectId);

        if (defectService.defectDelete(defectId)){
            log.info(auth.getName() + "defect successfully deleted ! : (defectId) " + defectId);
            return;
        }

        log.error(auth.getName() + "defect not found!  : (defectId) " + defectId);


    }

    @GetMapping("/getdefect/{defectId}")
    public ResponseEntity<DefectDto> defectGet(@PathVariable Long defectId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName() + "send get defect request ! : (defectId) " + defectId);

        DefectDto defectDto = defectService.defectGet(defectId);
        if (defectDto != null){
            return ResponseEntity.ok(defectDto);
        }
        log.error(auth.getName() + "defect not found!  : (defectId) " + defectId);

        return ResponseEntity.status(400).body(null);
    }

    @PutMapping("/updatedefect")
    public ResponseEntity<DefectDto> defectUpdate( @RequestBody DefectDto defectDto)  {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName() + "send defect update request ! : (defectId) " + defectDto.getId());

        DefectDto defectDto1 = defectService.updateDefect(defectDto);
        if (defectDto1 != null){
            log.info(auth.getName() + "defect successfully updated ! : (defectId) " + defectDto.getId());

            return ResponseEntity.ok(defectDto);
        }
        log.error(auth.getName() + "defect not found ! : (defectId) " + defectDto.getId());

        return ResponseEntity.status(400).body(null);

    }



}