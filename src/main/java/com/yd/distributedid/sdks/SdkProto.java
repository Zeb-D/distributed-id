package com.yd.distributedid.sdks;

/**
 * @author Yd
 */
public class SdkProto {

    private int rqid; //请求的ID
    private long did; //全局的ID
    private long id;//分布式Id

    public SdkProto(int rqid, long did, long id) {
        this.rqid = rqid;
        this.did = did;
        this.id = id;
    }

    public SdkProto(int rqid, long did) {
        this.rqid = rqid;
        this.did = did;
    }

    public int getRqid() {
        return rqid;
    }

    public void setRqid(int rqid) {
        this.rqid = rqid;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SdkProto{" +
                "rqid=" + rqid +
                ", did=" + did +
                ", id=" + id +
                '}';
    }
}
