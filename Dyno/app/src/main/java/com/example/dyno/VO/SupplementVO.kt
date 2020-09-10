package com.example.dyno.VO

class SupplementVO {

    constructor()

    constructor(no: Int, name: String, company: String, num: Int, date: String){
        this.m_no=no
        this.m_name=name
        this.m_company=company
        this.m_num=num
        this.m_date=date
    }

    var m_no : Int = 0
    var m_name: String = ""
    var m_company: String = ""
    var m_num: Int = 0
    var m_date: String = ""

}