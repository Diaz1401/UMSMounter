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
import com.jimzrt.umsmounter.fragments.SlideSecondFragment
import com.jimzrt.umsmounter.fragments.SlideThirdFragment


class SlidersIntroAdapter : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        addSlide(SlideFirstFragment())
        addSlide(SlideSecondFragment())
        addSlide(SlideThirdFragment())
        addSlide(AppIntroFragment.createInstance(
            "Welcome!",
            "This is a demo of the AppIntro library, with permissions being requested on a slide!",
            imageDrawable = R.drawable.ic_logo))

        addSlide(AppIntroFragment.createInstance(
            "Permission Request",
            "In order to access your camera, you must give permissions.",
            imageDrawable = R.drawable.ic_logo))

        addSlide(AppIntroFragment.createInstance(
            "Simple, yet Customizable",
            "The library offers a lot of customization, while keeping it simple for those that like simple.",
            imageDrawable = R.drawable.ic_logo))

        addSlide(AppIntroFragment.createInstance(
            "Explore",
            "Feel free to explore the rest of the library demo!",
            imageDrawable = R.drawable.ic_logo))

        // Here we ask for camera permission on slide 2
        //askForPermissions(arrayOf(Manifest.permission.CAMERA), 2)
    }



    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }
}

class MyFragment : Fragment(), SlidePolicy {

    // If user should be allowed to leave this slide
    override val isPolicyRespected: Boolean
        get() = false // Your custom logic here.

    override fun onUserIllegallyRequestedNextPage() {
        // User illegally requested next slide.
        // Show a toast or an informative message to the user.
    }
}