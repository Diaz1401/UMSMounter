package com.jimzrt.umsmounter.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.jimzrt.umsmounter.fragments.SlideFirstFragment
import com.jimzrt.umsmounter.fragments.SlideFourthFragment
import com.jimzrt.umsmounter.fragments.SlideSecondFragment
import com.jimzrt.umsmounter.fragments.SlideThirdFragment
import com.jimzrt.umsmounter.utils.SharedPrefsHelper


class SlidersIntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isWizardMode = true
        isSystemBackButtonLocked = true

        addSlide(SlideFirstFragment())
        addSlide(SlideSecondFragment())
        addSlide(SlideThirdFragment())
        addSlide(SlideFourthFragment())
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        SharedPrefsHelper.write(SharedPrefsHelper.IS_FIRST_RUN, false)
        // Reload main activity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}