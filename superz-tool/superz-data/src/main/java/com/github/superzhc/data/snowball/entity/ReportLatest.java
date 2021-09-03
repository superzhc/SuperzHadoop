package com.github.superzhc.data.snowball.entity;

/**
 * @author superz
 * @create 2021/8/3 10:42
 */
public class ReportLatest {
    private String title;
    private String rptComp;
    private String ratingDesc;
    private String targetPriceMin;
    private String targetPriceMax;
    private Integer pubDate;
    private Integer statusId;
    private Integer retweetCount;
    private Integer replyCount;
    private Integer likeCount;
    private String liked;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRptComp() {
        return this.rptComp;
    }

    public void setRptComp(String rptComp) {
        this.rptComp = rptComp;
    }

    public String getRatingDesc() {
        return this.ratingDesc;
    }

    public void setRatingDesc(String ratingDesc) {
        this.ratingDesc = ratingDesc;
    }

    public String getTargetPriceMin() {
        return this.targetPriceMin;
    }

    public void setTargetPriceMin(String targetPriceMin) {
        this.targetPriceMin = targetPriceMin;
    }

    public String getTargetPriceMax() {
        return this.targetPriceMax;
    }

    public void setTargetPriceMax(String targetPriceMax) {
        this.targetPriceMax = targetPriceMax;
    }

    public Integer getPubDate() {
        return this.pubDate;
    }

    public void setPubDate(Integer pubDate) {
        this.pubDate = pubDate;
    }

    public Integer getStatusId() {
        return this.statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getRetweetCount() {
        return this.retweetCount;
    }

    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Integer getReplyCount() {
        return this.replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getLiked() {
        return this.liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }
}
