package com.gkftndltek.busapp;

public class BusData {
    String endvehicletime,endnodenm,intervaltime,intervalsettime,intervalsuntime,routeid;
    String routeno,routetp,startnodenm,startvehicletime;
    public String getEndnodenm() {
        return endnodenm;
    }
    public void setEndnodenm(String endnodenm) {
        this.endnodenm = endnodenm;
    }
    public String getEndvehicletime() {
        return endvehicletime;
    }
    public void setEndvehicletime(String endvehicletime) {
        this.endvehicletime = endvehicletime;
    }
    public String getIntervaltime() {
        return intervaltime;
    }
    public void setIntervaltime(String intervaltime) {
        this.intervaltime = intervaltime;
    }
    public String getIntervalsettime() {
        return intervalsettime;
    }
    public void setIntervalsettime(String intervalsettime) {
        this.intervalsettime = intervalsettime;
    }
    public String getIntervalsuntime() {
        return intervalsuntime;
    }
    public void setIntervalsuntime(String intervalsuntime) {
        this.intervalsuntime = intervalsuntime;
    }
    public String getRouteid() {
        return routeid;
    }
    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }
    public String getRouteno() {
        return routeno;
    }
    public void setRouteno(String routeno) {
        this.routeno = routeno;
    }
    public String getRoutetp() {
        return routetp;
    }
    public void setRoutetp(String routetp) {
        this.routetp = routetp;
    }
    public String getStartnodenm() {
        return startnodenm;
    }
    public void setStartnodenm(String startnodenm) {
        this.startnodenm = startnodenm;
    }
    public String getStartvehicletime() {
        return startvehicletime;
    }
    public void setStartvehicletime(String startvehicletime) {
        this.startvehicletime = startvehicletime;
    }

    public String getData(){
        return endvehicletime +" "+ endnodenm +" "+ intervaltime +" "+ intervalsettime +
                " "+  intervalsuntime + " "+routeid + " "+routeno + " "+
                routetp + " "+startnodenm +" "+ startvehicletime;
    }
}
