package com.example.gadgetariumb8.db.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse {
    private String fileName;
    private String downloadURL;
    private String fileType;
    private long fileSize;
}
