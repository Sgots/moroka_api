package com.springboot.backend.service.impl;

import com.springboot.backend.exception.ResourceNotFoundException;
import com.springboot.backend.model.Area;
import com.springboot.backend.model.Field;
import com.springboot.backend.model.Patch;
import com.springboot.backend.model.User;
import com.springboot.backend.repository.AreaRepository;
import com.springboot.backend.repository.FieldRepository;
import com.springboot.backend.repository.PatchRepository;
import com.springboot.backend.repository.UserRepository;
import com.springboot.backend.request.AddPatchRequest;
import com.springboot.backend.request.UpdatePatchRequest;
import com.springboot.backend.service.PatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatchServiceImpl implements PatchService {

    @Autowired
    private PatchRepository patchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private AreaRepository areaRepository;

    public PatchServiceImpl(PatchRepository patchRepository) {
        super();
        this.patchRepository = patchRepository;
    }

    @Override
    public Patch addPatch(AddPatchRequest addPatchRequest) {

        User user = userRepository.getUserById(addPatchRequest.getUser_id());
        Field field = fieldRepository.findFieldById(addPatchRequest.getField_id());
        Area area = areaRepository.findAreaById(addPatchRequest.getArea_id());
        Patch addPatch = new Patch();

        addPatch.setPatch_name(addPatchRequest.getPatch_name());
        addPatch.setPatch_size(addPatchRequest.getPatch_size());
        addPatch.setField(field);
        addPatch.setUser(user);
        addPatch.setArea(area);

        return patchRepository.save(addPatch);
    }

    @Override
    public List<Patch> getALlPatches() {
        return patchRepository.findAll();
    }

    @Override
    public Patch getPatchByID(long id) {
        return patchRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Patch", "ID",id));
    }

    @Override
    public Patch updatePatch(UpdatePatchRequest patch, long id) {
        Patch patchExists = patchRepository.findPatchByID(id);

        patchExists.setPatch_name(patch.getPatch_name());
        patchExists.setPatch_size(patch.getPatch_size());
        patchRepository.save(patchExists);

        return patchExists;
    }

    @Override
    public void deletePatchByID(long id) {
        //check if user exists with that ID
        patchRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Patch","Id",id));
        patchRepository.deleteById(id);
    }
}
