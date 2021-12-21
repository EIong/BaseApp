package com.eiong.baseappdemo.mvp

import android.os.Bundle
import android.view.MenuItem
import com.eiong.baseapp.mvc.BaseActivity
import com.eiong.baseappdemo.databinding.ActivityMvpFraBinding

/**
 * MVP
 *
 * @author EIong
 */
class MvpFraActivity : BaseActivity<ActivityMvpFraBinding>() {
    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        supportActionBar?.apply {
            title = "MVP"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}