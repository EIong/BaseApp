package com.eiong.baseappdemo.mvp

import com.eiong.baseapp.mvp.BaseContract

/**
 * MVP
 *
 * @author EIong
 */
interface MvpContract {
    interface MvpPresenter : BaseContract.BasePresenter {
        /**
         * 请求数据
         */
        fun requestData()
    }

    interface MvpView : BaseContract.BaseView {
        /**
         * 处理数据
         */
        fun handleData(data: Int)
    }
}