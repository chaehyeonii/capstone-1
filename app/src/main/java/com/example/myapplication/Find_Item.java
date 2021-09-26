package com.example.myapplication;

public class Find_Item {

    String atcId;           //관리ID
    String clrNm;           //색상명
    String depPlace;        //보관장소
    String fdFilePathImg;   //습득물 사진 이미지
    String fdPrdtNm;        //물품명
    String fdSbjt;          //게시제목
    String fdSn;            //습득순번
    String fdYmd;           //습득일자
    String prdtClNm;        //물품분류명
    String rnum;            //일련번호


    // ----습득물 상세 조회 변수----//
    String csteSteNm;        //보관상태명
    String fdHor;            //습득시간
    String fdPlace;          //습득장소
    String fndKeepOrgnSeNm;  //습득물보관기관구분명
    String orgId;            //기관ID
    String orgNm;            //기관명
    String tel;              //기관전화번호
    String uniq;             //특이사항


    public String getatcId() { return atcId; }
    public String getclrNm() { return clrNm; }
    public String getdepPlace() { return depPlace; }
    public String getfdFilePathImg() { return fdFilePathImg; }
    public String getfdPrdtNm() { return fdPrdtNm; }
    public String getfdSbjt() { return fdSbjt; }
    public String getfdSn() { return fdSn; }
    public String getfdYmd() { return fdYmd; }
    public String getprdtClNm() { return prdtClNm; }
    public String getrnum() { return rnum; }

    public String getcsteSteNm() { return csteSteNm; }
    public String getfdHor() { return fdHor; }
    public String getfdPlace() { return fdPlace; }
    public String getfndKeepOrgnSeNm() { return fndKeepOrgnSeNm; }
    public String getorgNm() { return orgNm; }
    public String gettel() { return tel; }
    public String getuniq() { return uniq; }


    public void setatcId(String atcId) { this.atcId=atcId; }
    public void setclrNm() { this.clrNm=clrNm; }
    public void setdepPlace() { this.depPlace=depPlace; }
    public void setfdFilePathImg() { this.fdFilePathImg=fdFilePathImg; }
    public void setfdPrdtNm() { this.fdPrdtNm=fdPrdtNm; }
    public void setfdSbjt() { this.fdSbjt=fdSbjt; }
    public void setfdSn() { this.fdSn=fdSn; }
    public void setfdYmd() { this.fdYmd=fdYmd; }
    public void setprdtClNm() { this.prdtClNm=prdtClNm; }
    public void setrnum() { this.rnum=rnum; }

    public void setcsteSteNm() { this.csteSteNm=csteSteNm; }
    public void setfdHor() { this.fdHor=fdHor; }
    public void setfdPlace() { this.fdPlace=fdPlace; }
    public void setfndKeepOrgnSeNm() { this.fndKeepOrgnSeNm=fndKeepOrgnSeNm; }
    public void setorgNm() { this.orgNm=orgNm; }
    public void settel() { this.tel=tel; }
    public void setuniq() { this.uniq=uniq; }
}

