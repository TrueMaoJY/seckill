package com.maomao.seckill.Exception;

import com.maomao.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MaoJY
 * @create 2022-03-05 15:07
 * @Description:
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalException extends RuntimeException {
    private RespBeanEnum respBeanEnum;
}