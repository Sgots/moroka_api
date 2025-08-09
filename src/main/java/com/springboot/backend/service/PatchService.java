package com.springboot.backend.service;

import com.springboot.backend.model.Area;
import com.springboot.backend.model.Patch;
import com.springboot.backend.request.AddPatchRequest;
import com.springboot.backend.request.UpdatePatchRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatchService {

    Patch addPatch(AddPatchRequest addPatchRequest);
    List<Patch> getALlPatches();
    Patch getPatchByID(long id);
    Patch updatePatch(UpdatePatchRequest patch, long id);
    void deletePatchByID(long id);
}
