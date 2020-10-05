package com.example.dyno.VO

class MedicineVO {
    constructor()
    constructor(mCode:String,count:Int,amount:Int,detail:String,total:Int,ingredient:String){
        this.mCode=mCode
        this.count=count
        this.amount=amount
        this.detail=detail
        this.total=total
        this.ingredient=ingredient
    }
    var mCode:String=""//약코드
    var count:Int=0//1회투여횟수
    var amount:Int=0//1회 투약량
    var detail:String=""//세부정보
    var total:Int=0//총투약일수
    var ingredient:String=""//주성분
}