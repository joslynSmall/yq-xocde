package com.yq.xcode.common.base;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class XBaseView implements Serializable{

}
