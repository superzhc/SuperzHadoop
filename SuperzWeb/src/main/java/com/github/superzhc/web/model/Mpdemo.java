package com.github.superzhc.web.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;

/**
 * <p>
 * MyBatis-Plus示例表
 * </p>
 *
 * @author superz
 * @since 2020-09-10
 */
@TableName("mpdemo")
public class Mpdemo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String col;

    private Date d1;

    //private Blob blobcol;
    private String blobcol;

    /**
     * datetime类型的列
     */
    private Date dt;

    private BigDecimal col2;

    /**
     * double类型的列
     */
    private Double col3;

    /**
     * float类型的列
     */
    private Float col4;

    /**
     * text类型的列
     */
    private String col5;

    /**
     * time类型
     */
    private Date t1;

    /**
     * 时间戳
     */
    private Date t2;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public Date getD1() {
        return d1;
    }

    public void setD1(Date d1) {
        this.d1 = d1;
    }

//    public Blob getBlobcol() {
//        return blobcol;
//    }
//
//    public void setBlobcol(Blob blobcol) {
//        this.blobcol = blobcol;
//    }

    public String getBlobcol() {
        return blobcol;
    }

    public void setBlobcol(String blobcol) {
        this.blobcol = blobcol;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public BigDecimal getCol2() {
        return col2;
    }

    public void setCol2(BigDecimal col2) {
        this.col2 = col2;
    }

    public Double getCol3() {
        return col3;
    }

    public void setCol3(Double col3) {
        this.col3 = col3;
    }

    public Float getCol4() {
        return col4;
    }

    public void setCol4(Float col4) {
        this.col4 = col4;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public Date getT1() {
        return t1;
    }

    public void setT1(Date t1) {
        this.t1 = t1;
    }

    public Date getT2() {
        return t2;
    }

    public void setT2(Date t2) {
        this.t2 = t2;
    }

    @Override
    public String toString() {
        return "Mpdemo{" +
        "id=" + id +
        ", col=" + col +
        ", d1=" + d1 +
        ", blobcol=" + blobcol +
        ", dt=" + dt +
        ", col2=" + col2 +
        ", col3=" + col3 +
        ", col4=" + col4 +
        ", col5=" + col5 +
        ", t1=" + t1 +
        ", t2=" + t2 +
        "}";
    }
}
