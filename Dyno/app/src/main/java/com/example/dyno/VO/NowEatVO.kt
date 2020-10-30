package com.example.dyno.VO

class NowEatVO(){

    // 아이템 타입 (1이면 질병, 2면 건강기능식품)
    var type = -1

    // 아이템 id (room의 key값 : 질병 - 등록날짜, 건강기능식품 - 제품명) for 디테일이동
    var item_id : String =""

    // 아이템 이름 (질병 - 질병명, 건강기능식품 - 제품명)
    var item_name : String = ""

    // 아이템 등록 날짜 (YYYY-mm-dd 형태)
    var item_date : String = ""

    // 아이템 리스트 (질병 - 의약품리스트, 건강기능식품 - 기능성원재료리스트)
    var item_list_data : String = ""

    constructor(type: Int, item_id : String, item_name : String, item_date : String, item_list_data : String) : this() {
        this.type = type
        this.item_id=item_id
        this.item_name=item_name
        this.item_date=item_date
        this.item_list_data=item_list_data

    }

}

