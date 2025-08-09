package com.springboot.backend.service.impl;

import com.springboot.backend.exception.ResourceNotFoundException;
import com.springboot.backend.model.Area;
import com.springboot.backend.model.Field;
import com.springboot.backend.model.User;
import com.springboot.backend.repository.AreaRepository;
import com.springboot.backend.repository.FieldRepository;
import com.springboot.backend.repository.UserRepository;
import com.springboot.backend.request.AddAreaRequest;
import com.springboot.backend.request.UpdateAreaRequest;
import com.springboot.backend.request.UpdateFieldRequest;
import com.springboot.backend.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FieldRepository fieldRepository;

    public AreaServiceImpl(AreaRepository areaRepository) {
        super();
        this.areaRepository = areaRepository;
    }
    @Override
    public Area addArea(Area area) {

        User user = new User();
        Field field = new Field();
        Area addArea = new Area();


        addArea.setArea_name(area.getArea_name());
        addArea.setArea_size(area.getArea_size());

        return areaRepository.save(addArea);
    }
    @Override
    public List<Area> getALlAreas() {
        return areaRepository.findAll();
    }
    @Override
    public Area getAreaByID(int id) {
        return areaRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Area", "ID",id));
    }

    @Override
    public List <Area> findAreabyFiedID(long area_id) {
        return areaRepository.findAreabyFiedID(area_id);
    }

    @Override
    public Field findFieldAreaByAreaId(int id) {
        return areaRepository.findFieldAreaByAreaId(id);
    }

    @Override
    public Area updateArea(UpdateAreaRequest updateAreaRequest, int id) {

        //Field field = fieldRepository.findFieldById(updateAreaRequest.getField_id());

        Area areaExists = areaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("area", "Id", id));

        areaExists.setArea_name(updateAreaRequest.getArea_name());
        areaExists.setArea_size(updateAreaRequest.getArea_size());
        //areaExists.setField(field);
        areaRepository.save(areaExists);

        return areaExists;
    }

    @Override
    public Area saveArea(AddAreaRequest addAreaRequest) {

        User user = userRepository.getUserById(addAreaRequest.getUser_id());
        Field field = fieldRepository.findFieldById(addAreaRequest.getField_id());
        Area addArea = new Area();

        addArea.setArea_name(addAreaRequest.getArea_name());
        addArea.setArea_size(addAreaRequest.getArea_size());
        addArea.setField(field);
        addArea.setUser(user);
        return areaRepository.save(addArea);
    }


}
