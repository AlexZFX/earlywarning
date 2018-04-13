package com.alexzfx.earlywarninguser.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class PageUtil<T> {

    @Expose
    @SerializedName("page_size")
    private Integer pageSize;
    @SerializedName("page_num")
    @Expose
    private Integer pageNum;
    @SerializedName("page_total")
    @Expose
    private Integer pageTotal;
    @SerializedName("total_num")
    @Expose
    private Long totalNum;
    @SerializedName("list")
    @Expose
    private List<T> list;

    public PageUtil() {
    }


}
