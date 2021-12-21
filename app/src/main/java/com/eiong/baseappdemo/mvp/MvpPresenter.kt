package com.eiong.baseappdemo.mvp

import com.eiong.baseapp.mvp.BasePresenter

/**
 * MVP
 *
 * @author EIong
 */
class MvpPresenter : BasePresenter<MvpContract.MvpView>(), MvpContract.MvpPresenter {
    override fun requestData() {
        val color = (-(Math.random() * (16777216 - 1) + 1)).toInt()
        v.handleData(color)
    }
}