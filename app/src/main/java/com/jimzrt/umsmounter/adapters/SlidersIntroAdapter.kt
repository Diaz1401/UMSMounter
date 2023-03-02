package com.jimzrt.umsmounter.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroFragment
import com.github.appintro.SlidePolicy
import com.jimzrt.umsmounter.R
import com.jimzrt.umsmounter.fragments.SlideFirstFragment
import com.github.appintro.AppIntroCustomLayoutFragment.Companion.newInstance
import com.jimzrt.umsmounter.fragments.SlideFourthFragment
import com.jimzrt.umsmounter.fragments.SlideSecondFragment
import com.jimzrt.umsmounter.fragments.SlideThirdFragment


class SlidersIntroAdapter : AppIntro() {
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
        finish()
    }
}