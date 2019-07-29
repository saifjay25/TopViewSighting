package com.mycode.topviewproject.di;

import com.mycode.topviewproject.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

//for injecting into the activities
@Module
public abstract class ActivityBuildersModule {

    //pass fragments as a module inside of the main activity subcomponent
    //means that this fragment can only exist within the scope of the main activity subcomponent
    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract MainActivity contributesMainActivity();


}
