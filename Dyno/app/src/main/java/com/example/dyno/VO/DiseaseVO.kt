package com.example.dyno.VO

class DiseaseVO {
    constructor()
    constructor(dCode:String,dName:String,date:String){
        this.dCode=dCode
        this.dName=dName
        this.date=date
    }
    var dCode:String=""//질병코드
    var dName:String=""//질병명
    var date:String=""//처방날짜

}