package com.eiong.baseappdemo.mvc

import android.os.Bundle
import android.view.MenuItem
import com.eiong.baseapp.mvc.BaseActivity
import com.eiong.baseappdemo.databinding.ActivityMvcFraBinding

/**
 * MVC
 *
 * @author EIong
 */
class MvcFraActivity : BaseActivity<ActivityMvcFraBinding>() {
    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        supportActionBar?.apply {
            title = "MVC"
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