package com.example.currencyconverter.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.entity.Currency

internal val currencyUiDataMap: Map<Currency, CurrencyUIData> = mapOf(
    Currency.EUR to CurrencyUIData(R.string.currency_eur, R.string.symbol_eur),
    Currency.AUD to CurrencyUIData(R.string.currency_aud, R.string.symbol_aud),
    Currency.BGN to CurrencyUIData(R.string.currency_bgn, R.string.symbol_bgn),
    Currency.GBP to CurrencyUIData(R.string.currency_gbp, R.string.symbol_gbp),
    Currency.USD to CurrencyUIData(R.string.currency_usd, R.string.symbol_usd),
    Currency.BRL to CurrencyUIData(R.string.currency_brl, R.string.symbol_brl),
    Currency.CAD to CurrencyUIData(R.string.currency_cad, R.string.symbol_cad),
    Currency.CHF to CurrencyUIData(R.string.currency_chf, R.string.symbol_chf),
    Currency.CNY to CurrencyUIData(R.string.currency_cny, R.string.symbol_cny),
    Currency.CZK to CurrencyUIData(R.string.currency_czk, R.string.symbol_czk),
    Currency.DKK to CurrencyUIData(R.string.currency_dkk, R.string.symbol_dkk),
    Currency.HKD to CurrencyUIData(R.string.currency_hkd, R.string.symbol_hkd),
    Currency.HRK to CurrencyUIData(R.string.currency_hrk, R.string.symbol_hrk),
    Currency.HUF to CurrencyUIData(R.string.currency_huf, R.string.symbol_huf),
    Currency.IDR to CurrencyUIData(R.string.currency_idr, R.string.symbol_idr),
    Currency.ILS to CurrencyUIData(R.string.currency_ils, R.string.symbol_ils),
    Currency.INR to CurrencyUIData(R.string.currency_inr, R.string.symbol_inr),
    Currency.ISK to CurrencyUIData(R.string.currency_isk, R.string.symbol_isk),
    Currency.JPY to CurrencyUIData(R.string.currency_jpy, R.string.symbol_jpy),
    Currency.KRW to CurrencyUIData(R.string.currency_krw, R.string.symbol_krw),
    Currency.MXN to CurrencyUIData(R.string.currency_mxn, R.string.symbol_mxn),
    Currency.MYR to CurrencyUIData(R.string.currency_myr, R.string.symbol_myr),
    Currency.NOK to CurrencyUIData(R.string.currency_nok, R.string.symbol_nok),
    Currency.NZD to CurrencyUIData(R.string.currency_nzd, R.string.symbol_nzd),
    Currency.PHP to CurrencyUIData(R.string.currency_php, R.string.symbol_php),
    Currency.PLN to CurrencyUIData(R.string.currency_pln, R.string.symbol_pln),
    Currency.RON to CurrencyUIData(R.string.currency_ron, R.string.symbol_ron),
    Currency.RUB to CurrencyUIData(R.string.currency_rub, R.string.symbol_rub),
    Currency.SEK to CurrencyUIData(R.string.currency_sek, R.string.symbol_sek),
    Currency.SGD to CurrencyUIData(R.string.currency_sgd, R.string.symbol_sgd),
    Currency.THB to CurrencyUIData(R.string.currency_thb, R.string.symbol_thb),
    Currency.TRY to CurrencyUIData(R.string.currency_try, R.string.symbol_try),
    Currency.ZAR to CurrencyUIData(R.string.currency_zar, R.string.symbol_zar)
)

data class CurrencyUIData(
    @StringRes val fullNameResId: Int,
    @StringRes val symbolResId: Int
)


