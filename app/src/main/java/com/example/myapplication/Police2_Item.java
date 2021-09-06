package com.example.myapplication;

public class Police2_Item {

    String atcId;           //관리ID
    String lstPlace;        //분실지역명
    String lstPrdtNm;       //분실물명
    String lstSbjt;         //게시제목
    String lstYmd;          //게시일자, 분실일자
    String prdtClNm;        //물품분류명
    String rnum;            //일련번호

    // ----분실물 상세 조회 변수----//
    String lstFilePathImg;  //분실물이미지명
    String lstHor;          //분실시간
    String lstLctNm;        //분실지역명, 분실물 상세조회에서
    String lstPlaceSeNm;    //분실장소구분명
    String lstSteNm;        //분실물 상태명
    String orgId;           //기관ID
    String orgNm;           //기관명
    String tel;             //기관전화번호
    String uniq;            //특이사항


    public String getatcId() { return atcId; }
    public String getlstPlace() { return lstPlace; }
    public String getlstPrdtNm() { return lstPrdtNm; }
    public String getlstSbjt() { return lstSbjt; }
    public String getlstYmd() { return lstYmd; }
    public String getprdtClNm() { return prdtClNm; }
    public String getrnum() { return rnum; }
    public String getlstFilePathImg() { return lstFilePathImg; }
    public String getlstHor() { return lstHor; }
    public String getlstLctNm() { return lstLctNm; }
    public String getlstPlaceSeNm() { return lstPlaceSeNm; }
    public String getlstSteNm() { return lstSteNm; }
    public String getorgId() { return orgId; }
    public String getorgNm() { return orgNm; }
    public String gettel() { return tel; }
    public String getuniq() { return uniq; }


    public void setatcId(String atcId) { this.atcId=atcId; }
    public void setlstPlace(String lstPlace) { this.lstPlace= lstPlace; }
    public void setlstPrdtNm(String lstPrdtNm) { this.lstPrdtNm= lstPrdtNm; }
    public void setlstSbjt(String lstSbjt) { this.lstSbjt= lstSbjt; }
    public void setlstYmd(String lstYmd) { this.lstYmd= lstYmd; }
    public void setprdtClNm(String prdtClNm) { this.prdtClNm= prdtClNm; }
    public void setrnum(String rnum) { this.rnum= rnum; }
    public void setlstFilePathImg(String lstFilePathImg) { this.lstFilePathImg= lstFilePathImg; }
    public void setlstHor(String lstHor) { this.lstHor= lstHor; }
    public void setlstLctNm(String lstLctNm) { this.lstLctNm= lstLctNm; }
    public void setlstPlaceSeNm(String lstPlaceSeNm) { this.lstPlaceSeNm= lstPlaceSeNm; }
    public void setlstSteNm(String lstSteNm) { this.lstSteNm= lstSteNm; }
    public void setorgId(String orgId) { this.orgId= orgId; }
    public void setorgNm(String orgNm) { this.orgNm= orgNm; }
    public void settel(String tel) { this.tel= tel; }
    public void setuniq(String uniq) { this.uniq= uniq; }


}
