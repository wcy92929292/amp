package com.udbac.model;

import java.math.BigDecimal;
import java.util.Date;

public class TbMaEffectData {
    private Integer id;

    private Date createDate;

    private String mic;

    private String micCode;

    private String exposure;

    private String click;

    private String visit;

    private String pv;

    private String visitor;

    private String bounceVisit;

    private String bounceRate;

    private String viewtime;

    private String exposureNum;

    private Integer clickPv;

    private Integer clickUv;

    private Integer exposurePvMedia;

    private Integer exposurePvMonitor;

    private BigDecimal exposurePvGap;

    private Integer exposureUvMedia;

    private Integer exposureUvMonitor;

    private BigDecimal exposureUvGap;

    private Integer clickPvMedia;

    private Integer clickPvMonitor;

    private BigDecimal clickPvGap;

    private Integer clickUvMedia;

    private Integer clickUvMonitor;

    private BigDecimal clickUvGap;

    private Integer exposurePvFinal;

    private Integer exposureUvFinal;

    private Integer clickPvFinal;

    private Integer clickUvFinal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic == null ? null : mic.trim();
    }

    public String getMicCode() {
        return micCode;
    }

    public void setMicCode(String micCode) {
        this.micCode = micCode == null ? null : micCode.trim();
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure == null ? null : exposure.trim();
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click == null ? null : click.trim();
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit == null ? null : visit.trim();
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv == null ? null : pv.trim();
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor == null ? null : visitor.trim();
    }

    public String getBounceVisit() {
        return bounceVisit;
    }

    public void setBounceVisit(String bounceVisit) {
        this.bounceVisit = bounceVisit == null ? null : bounceVisit.trim();
    }

    public String getBounceRate() {
        return bounceRate;
    }

    public void setBounceRate(String bounceRate) {
        this.bounceRate = bounceRate == null ? null : bounceRate.trim();
    }

    public String getViewtime() {
        return viewtime;
    }

    public void setViewtime(String viewtime) {
        this.viewtime = viewtime == null ? null : viewtime.trim();
    }

    public String getExposureNum() {
        return exposureNum;
    }

    public void setExposureNum(String exposureNum) {
        this.exposureNum = exposureNum == null ? null : exposureNum.trim();
    }

    public Integer getClickPv() {
        return clickPv;
    }

    public void setClickPv(Integer clickPv) {
        this.clickPv = clickPv;
    }

    public Integer getClickUv() {
        return clickUv;
    }

    public void setClickUv(Integer clickUv) {
        this.clickUv = clickUv;
    }

    public Integer getExposurePvMedia() {
        return exposurePvMedia;
    }

    public void setExposurePvMedia(Integer exposurePvMedia) {
        this.exposurePvMedia = exposurePvMedia;
    }

    public Integer getExposurePvMonitor() {
        return exposurePvMonitor;
    }

    public void setExposurePvMonitor(Integer exposurePvMonitor) {
        this.exposurePvMonitor = exposurePvMonitor;
    }

    public BigDecimal getExposurePvGap() {
        return exposurePvGap;
    }

    public void setExposurePvGap(BigDecimal exposurePvGap) {
        this.exposurePvGap = exposurePvGap;
    }

    public Integer getExposureUvMedia() {
        return exposureUvMedia;
    }

    public void setExposureUvMedia(Integer exposureUvMedia) {
        this.exposureUvMedia = exposureUvMedia;
    }

    public Integer getExposureUvMonitor() {
        return exposureUvMonitor;
    }

    public void setExposureUvMonitor(Integer exposureUvMonitor) {
        this.exposureUvMonitor = exposureUvMonitor;
    }

    public BigDecimal getExposureUvGap() {
        return exposureUvGap;
    }

    public void setExposureUvGap(BigDecimal exposureUvGap) {
        this.exposureUvGap = exposureUvGap;
    }

    public Integer getClickPvMedia() {
        return clickPvMedia;
    }

    public void setClickPvMedia(Integer clickPvMedia) {
        this.clickPvMedia = clickPvMedia;
    }

    public Integer getClickPvMonitor() {
        return clickPvMonitor;
    }

    public void setClickPvMonitor(Integer clickPvMonitor) {
        this.clickPvMonitor = clickPvMonitor;
    }

    public BigDecimal getClickPvGap() {
        return clickPvGap;
    }

    public void setClickPvGap(BigDecimal clickPvGap) {
        this.clickPvGap = clickPvGap;
    }

    public Integer getClickUvMedia() {
        return clickUvMedia;
    }

    public void setClickUvMedia(Integer clickUvMedia) {
        this.clickUvMedia = clickUvMedia;
    }

    public Integer getClickUvMonitor() {
        return clickUvMonitor;
    }

    public void setClickUvMonitor(Integer clickUvMonitor) {
        this.clickUvMonitor = clickUvMonitor;
    }

    public BigDecimal getClickUvGap() {
        return clickUvGap;
    }

    public void setClickUvGap(BigDecimal clickUvGap) {
        this.clickUvGap = clickUvGap;
    }

    public Integer getExposurePvFinal() {
        return exposurePvFinal;
    }

    public void setExposurePvFinal(Integer exposurePvFinal) {
        this.exposurePvFinal = exposurePvFinal;
    }

    public Integer getExposureUvFinal() {
        return exposureUvFinal;
    }

    public void setExposureUvFinal(Integer exposureUvFinal) {
        this.exposureUvFinal = exposureUvFinal;
    }

    public Integer getClickPvFinal() {
        return clickPvFinal;
    }

    public void setClickPvFinal(Integer clickPvFinal) {
        this.clickPvFinal = clickPvFinal;
    }

    public Integer getClickUvFinal() {
        return clickUvFinal;
    }

    public void setClickUvFinal(Integer clickUvFinal) {
        this.clickUvFinal = clickUvFinal;
    }
}