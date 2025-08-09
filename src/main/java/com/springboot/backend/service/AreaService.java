package com.springboot.backend.service;

import com.springboot.backend.model.Area;
import com.springboot.backend.model.Field;
import com.springboot.backend.request.AddAreaRequest;
import com.springboot.backend.request.UpdateAreaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface AreaService {

    Area addArea(Area area);
    List<Area> getALlAreas();
    Area getAreaByID(int id);
    Area updateArea(UpdateAreaRequest updateAreaRequest, int id);

     List <Area> findAreabyFiedID(long field_id);

    Field findFieldAreaByAreaId(int id);

    Area saveArea(AddAreaRequest addAreaRequest);
}
