package com.example.woholi.db.exchange

data class ExchangeRateItem(
    val bkpr: String,
    val cur_nm: String,
    val cur_unit: String,
    val deal_bas_r: String,
    val kftc_bkpr: String,
    val kftc_deal_bas_r: String,
    val result: Int,
    val ten_dd_efee_r: String,
    val ttb: String,
    val tts: String,
    val yy_efee_r: String
)