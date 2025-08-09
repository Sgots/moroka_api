package com.springboot.backend.controller;

import com.springboot.backend.exception.MessageResponse;
import com.springboot.backend.model.Device;
import com.springboot.backend.model.Patch;
import com.springboot.backend.repository.PatchRepository;
import com.springboot.backend.request.AddPatchRequest;
import com.springboot.backend.request.UpdatePatchRequest;
import com.springboot.backend.service.PatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class PatchController {

    @Autowired
    private PatchService patchService;
    @Autowired
    private PatchRepository patchRepository;

    public PatchController(PatchService patchService) {
        super();
        this.patchService = patchService;
    }
    //add patch REST API
    //http://localhost:8080//api/add-patch
    @PostMapping("/add-patch")
    public ResponseEntity<?> addField(@Valid @RequestBody AddPatchRequest addPatchRequest) throws SQLIntegrityConstraintViolationException {
        if (patchRepository.existsById(addPatchRequest.getPatch_id())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Patch exists!"));
        }
        return new ResponseEntity<Patch>(patchService.addPatch(addPatchRequest), HttpStatus.OK);
    }
    //create get patch REST API
    //http://localhost:8080//api/get-patch
    @GetMapping("/get-patches")
    public List<?> getPatches(){
        return patchService.getALlPatches();
    }


    //create get patch by ID REST API
    //http://localhost:8080/api/patch/1
    @GetMapping("/get-patch/{id}")
    public ResponseEntity<Patch> getPatchByID(@PathVariable("id") int patchId){
        return new ResponseEntity<Patch>(patchRepository.findPatchById(patchId),HttpStatus.OK);
    }
    @GetMapping("/get-patch-area/{id}")
    public List<Patch>  getPatchByAreaID(@PathVariable("id") long areaID){
        return patchRepository.findPatchByAreaID(areaID);

    }
    //update patch by ID REST API
    //http://localhost:8080/api/patch/1
    @PostMapping("/update-patch/{id}")
    public ResponseEntity<Patch> updatePatch(@PathVariable ("id") int patchID,
                                           @RequestBody UpdatePatchRequest patch){
        return new ResponseEntity<Patch>(patchService.updatePatch(patch, patchID), HttpStatus.OK);
    }
    //delete patch by ID REST API
    //http://localhost:8080/api/patch/1
    @DeleteMapping("/delete-patch/{id}")
    public ResponseEntity<String> deletePatch(@PathVariable("id") int patchID){
        //delete area from DB
        patchService.deletePatchByID(patchID);
        return new ResponseEntity<String>("Patch Deleted Successfully",HttpStatus.OK);
    }

    @PostMapping("/delete-patch/{id}")
    public ResponseEntity<String> deletePatchByID(@PathVariable("id") long patchID){
        //delete area from DB
        patchRepository.markPatchAsDeleted(patchID);
        //areaService.deleteAreaByID(areaID);
        return new ResponseEntity<String>("Patch Deleted Successfully",HttpStatus.OK);
    }
}
