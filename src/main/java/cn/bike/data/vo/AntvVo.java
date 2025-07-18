package cn.bike.data.vo;

import io.swagger.annotations.Api;
import lombok.Data;

import java.math.BigDecimal;


@Api(tags = "图表VO类")
@Data
public class AntvVo {
    private String title;
    private String type;
    private BigDecimal value;
}

