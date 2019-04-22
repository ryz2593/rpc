package com.ryz2593.rpc.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ryz2593
 * @date 2019/4/19
 * @desc
 */
@Data
public class RequestData implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class<?>[] parametersTypes;
    private Object[] parameters;
}
