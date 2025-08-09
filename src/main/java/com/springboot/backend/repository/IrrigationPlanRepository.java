package com.springboot.backend.repository;

import com.springboot.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface IrrigationPlanRepository extends JpaRepository<IrrigationPlan, Long> {

    IrrigationPlan findIrrigationPlanById(long id);

    //IrrigationPlan addPlan(IrrigationPlan irrigationPlan, IrrigationPlanPeriod irrigationPlanPeriod, IrrigationPlanPeriodTimes irrigationPlanPeriodTimes);

    //IrrigationPlan fi
    @Query("SELECT d FROM IrrigationPlan d JOIN d.fields f WHERE f.id = :id")
    List<IrrigationPlan> getFieldPlan(long id);

    @Query("SELECT d FROM IrrigationPlan d WHERE d.user.id = :id and d.delete_status=0")
    List<IrrigationPlan> getUserPlans(long id);


    IrrigationPlan findAllByUserId(long id);

  /*  @Query( value = "select * from tbl_irrigation_plan where user_id =?",
            nativeQuery = true)
    Collection <IrrigationPlan> findIrrigationPlanByUserId(long user_id);*/

    @Transactional
    @Modifying
    @Query("update IrrigationPlan set delete_status = 1 where id =:id")
    void markIrrigationPlanAsDeleted(@Param("id") Long id);

   /* @Transactional
    @Modifying
    @Query("update IrrigationPlan set plan_name =:plan_name, plan_description = :plan_description where id =:id")
    void updatePlan(@Param("plan_name") String plan_name, @Param("plan_description") String plan_description,  @Param("id") Long id);*/

  /*  @Modifying
    @Query("insert into Person (id,name,age) select :id,:name,:age")
    public int modifyingQueryInsertPerson(@Param("id")Long id, @Param("name")String name, @Param("age")Integer age);*/

}

