package com.ooadprojectserver.restaurantmanagement.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableInfoResponse {
    private Integer id;
    private Integer number;
    private Integer status;
}
