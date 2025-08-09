package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    private long id;
    private long field_id;
    private long user_id;
}
